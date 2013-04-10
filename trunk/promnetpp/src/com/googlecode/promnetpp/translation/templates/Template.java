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
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Miguel Martins
 */
public abstract class Template {
    protected String name;
    protected List<String> fileNames;

    public Template() {
        fileNames = new ArrayList<String>();
    }
    
    public static Template getTemplate(String templateName) {
        if (templateName.equals("round_based_protocol_generic")) {
            return new RoundBasedProtocolGeneric();
        }
        assert false : "Unknown template: " + templateName + "!";
        return null;
    }

    public void copyFiles() throws IOException {
        for (String fileName : fileNames) {
            String fileLocation = MessageFormat.format("{0}/templates/{1}/{2}",
                    System.getProperty("promnetpp.home"), name, fileName);
            FileUtils.copyFile(new File(fileLocation), new File(
                    Options.outputDirectory + "/" + fileName));
        }
    }
}
