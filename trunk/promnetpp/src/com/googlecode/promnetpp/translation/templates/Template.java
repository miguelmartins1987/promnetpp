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
import com.googlecode.promnetpp.parsing.ASTNode;
import com.googlecode.promnetpp.utilities.IndentedStringWriter;
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
    protected String currentBlock;
    //Meant for specific functions
    protected Map<String, IndentedStringWriter> specificFunctionWriters;
    //Local variable declarations for each type of process
    protected Map<String, IndentedStringWriter> localVariableDeclarations;
    

    public Template() {
        staticFileNames = new ArrayList<String>();
        dynamicFileNames = new ArrayList<String>();
        dynamicFileContents = new HashMap<String, String>();
        currentBlock = "main";
        
        specificFunctionWriters = new HashMap<String, IndentedStringWriter>();
        localVariableDeclarations = new HashMap<String, IndentedStringWriter>();
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

    public void setCurrentBlock(String blockName) {
        currentBlock = blockName;
    }
    
    public IndentedStringWriter getSpecificFunctionWriter(String functionName) {
        IndentedStringWriter writer = specificFunctionWriters.get(functionName);
        if (writer == null) {
            writer = new IndentedStringWriter();
            specificFunctionWriters.put(functionName, writer);
        }
        return writer;
    }
    
    public IndentedStringWriter getLocalVariableDeclarationWriter(String
            processName) {
        IndentedStringWriter writer = localVariableDeclarations.get(processName);
        if (writer == null) {
            writer = new IndentedStringWriter();
            localVariableDeclarations.put(processName, writer);
        }
        return writer;
    }

    public void handleTemplateParameter(ASTNode directive,
            String parameterName) {
        throw new UnsupportedOperationException("Must be overridden!");
    }
    
    public String getGlobalDeclarations() {
        throw new UnsupportedOperationException("Must be overridden!");
    }
    
    
    public void writeMessageDefinitionFiles() throws IOException {
        throw new UnsupportedOperationException("Must be overridden!");
    }
    
    public void writeNEDFile() throws IOException {
        throw new UnsupportedOperationException("Must be overridden!");
    }
}
