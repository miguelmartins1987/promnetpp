/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.translation.templates;

import com.googlecode.promnetpp.options.Options;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Miguel Martins
 */
public abstract class Template {
    protected String name;
    protected List<String> staticFileNames;
    protected List<String> dynamicFileNames;
    protected Map<String, String> dynamicFileContents;
    

    public Template() {
        staticFileNames = new ArrayList<String>();
        dynamicFileNames = new ArrayList<String>();
        dynamicFileContents = new HashMap<String, String>();
    }
    
    public static Template getTemplate(String templateName) {
        if (templateName.equals("round_based_protocol_generic")) {
            return new RoundBasedProtocolGeneric();
        }
        assert false : "Unknown template: " + templateName + "!";
        return null;
    }
    
    public void addStaticFile(String fileName) {
        staticFileNames.add(fileName);
    }
    
    public void addDynamicFile(String fileName) {
        dynamicFileNames.add(fileName);
        try {
            String fileLocation = MessageFormat.format("{0}/templates/{1}/{2}",
                        System.getProperty("promnetpp.home"), name, fileName);
            String contents = FileUtils.readFileToString(new File(fileLocation));
            dynamicFileContents.put(fileName, contents);
        } catch (IOException ex) {
            Logger.getLogger(Template.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }
    
    public void setDynamicFileParameters(String fileName,
            String... parameters) throws IOException {
        String contents = dynamicFileContents.get(fileName);
        contents = MessageFormat.format(contents, (Object[]) parameters);
        dynamicFileContents.put(fileName, contents);
    }

    public void copyStaticFiles() throws IOException {
        for (String fileName : staticFileNames) {
            String fileLocation = MessageFormat.format("{0}/templates/{1}/{2}",
                    System.getProperty("promnetpp.home"), name, fileName);
            FileUtils.copyFile(new File(fileLocation), new File(
                    Options.outputDirectory + "/" + fileName));
        }
    }

    public void writeDynamicFiles() throws IOException {
        for (String fileName : dynamicFileNames) {
            String contents = dynamicFileContents.get(fileName);
            FileUtils.writeStringToFile(new File(Options.outputDirectory + "/"
                    + fileName), contents);
        }
    }
}
