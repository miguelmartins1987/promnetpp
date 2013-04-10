/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.translation;

import com.googlecode.promnetpp.options.Options;
import com.googlecode.promnetpp.other.Utilities;
import com.googlecode.promnetpp.parsing.ASTNode;
import com.googlecode.promnetpp.parsing.AbstractSyntaxTree;
import com.googlecode.promnetpp.translation.templates.RoundBasedProtocolGeneric;
import com.googlecode.promnetpp.translation.templates.Template;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * The
 * <code>StandardTranslator</code> class represents the standard translation
 * procedure to C++ code that's runnable in OMNeT++
 *
 * @author Miguel Martins
 */
public class StandardTranslator implements Translator {

    /**
     * Indentation settings
     */
    private static final int SPACES_PER_TAB = 4;
    private int currentIndentationLevel = 0;
    /**
     * Whether or not there's an implicit init process
     */
    private boolean implicitInit = true;
    /**
     * The template to be used, if any
     */
    private Template template;
    /**
     * String writers for various purposes (one of them being type definitions)
     */
    private StringWriter typeDefinitions;
    private StringWriter globalDefinitions;
    /**
     * Other
     */
    private Map<String, String> macros, nonMacros;

    @Override
    public void init() {
        if (!(Options.outputDirectory.equals("."))) {
            Utilities.makeDirectory(Options.outputDirectory);
        }
        typeDefinitions = new StringWriter();
        globalDefinitions = new StringWriter();

        macros = new HashMap<String, String>();
        nonMacros = new HashMap<String, String>();
        
        Logger.getLogger(StandardTranslator.class.getName()).log(Level.INFO,
                "Translation process ready. Output directory: {0}",
                Options.outputDirectory);
    }

    @Override
    public void finish() {
        String currentFileContents;
        try {
            //Write type definitions
            currentFileContents = FileUtils.readFileToString(new File(
                    System.getProperty("promnetpp.home")
                    + "/templates/private/types.h"));
            currentFileContents = MessageFormat.format(currentFileContents,
                    typeDefinitions.toString());
            FileUtils.writeStringToFile(new File(
                    Options.outputDirectory + "/types.h"), currentFileContents);
            //Write global definitions
            currentFileContents = FileUtils.readFileToString(new File(
                    System.getProperty("promnetpp.home")
                    + "/templates/private/global_definitions.h"));
            currentFileContents = MessageFormat.format(currentFileContents,
                    globalDefinitions.toString());
            FileUtils.writeStringToFile(new File(Options.outputDirectory
                    + "/global_definitions.h"), currentFileContents);
            //Copy files
            copyFiles();
        } catch (IOException ex) {
            Logger.getLogger(StandardTranslator.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }
    
    private void copyFiles() throws IOException {
        //omnetpp.ini
        FileUtils.copyFile(new File(System.getProperty("promnetpp.home")
                + "/templates/private/omnetpp.ini"),
                new File(Options.outputDirectory + "/omnetpp.ini"));
        //Process interface files
        FileUtils.copyFile(new File(System.getProperty("promnetpp.home")
                + "/templates/private/process_interface.h"),
                new File(Options.outputDirectory + "/process_interface.h"));
        FileUtils.copyFile(new File(System.getProperty("promnetpp.home")
                + "/templates/private/process_interface.cc"),
                new File(Options.outputDirectory + "/process_interface.cc"));
        //Template files
        if (template != null) {
            template.copyFiles();
        }
    }

    private void indent() {
        currentIndentationLevel += SPACES_PER_TAB;
    }

    private void dedent() {
        currentIndentationLevel -= SPACES_PER_TAB;
    }

    @Override
    public void translate(AbstractSyntaxTree abstractSyntaxTree) {
        ASTNode rootNode = abstractSyntaxTree.getRootNode();
        ASTNode specification = (ASTNode) rootNode.jjtGetChild(0);
        translateSpecification(specification);
    }

    private void translateSpecification(ASTNode specification) {
        for (int i = 0; i < specification.jjtGetNumChildren(); ++i) {
            ASTNode element = (ASTNode) specification.jjtGetChild(i);
            String elementType = element.getNodeName();
            assert elementType.equals("Unit") : "Expected to find a Unit!"
                    + "Found " + elementType + " instead!";
            translateUnit(element);
        }
    }

    private void translateUnit(ASTNode translationUnit) {
        ASTNode currentChild;
        String currentChildType;
        for (int i = 0; i < translationUnit.jjtGetNumChildren(); ++i) {
            currentChild = (ASTNode) translationUnit.jjtGetChild(i);
            currentChildType = currentChild.getNodeName();
            //Comment
            if (currentChildType.equals("Comment")) {
                handleComment(currentChild);
            } //#define's
            else if (currentChildType.equals("DefineDirective")) {
                handleDefineDirective(currentChild);
            } //Type definition (global)
            else if (currentChildType.equals("TypeDefinition")) {
                translateTypeDefinition(currentChild);
            } //Global declarations
            else if (currentChildType.equals("GlobalDeclaration")) {
                translateGlobalDeclaration(currentChild);
            } //Process definitions
            else if (currentChildType.equals("ProcessDefinition")) {
                translateProcessDefinition(currentChild);
            }
        }
    }

    private void translateTypeDefinition(ASTNode typeDefinition) {
        Logger.getLogger(StandardTranslator.class.getName()).log(Level.INFO,
                "Found type definition (typeName={0})",
                typeDefinition.getName());
        typeDefinitions.write("typedef struct {\n");
        indent();
        for (int i = 0; i < typeDefinition.jjtGetNumChildren(); ++i) {
            ASTNode currentChild = (ASTNode) typeDefinition.jjtGetChild(i);
            if (currentChild.getNodeName().equals("SimpleDeclaration")) {
                String typeName = currentChild.getTypeName();
                String name = currentChild.getName();
                boolean isArray = currentChild.getValueAsBoolean("isArray");
                if (!isArray) {
                    String message = MessageFormat.format("{0} {1};\n",
                            typeName, name);
                    try {
                        Utilities.writeWithIndentation(typeDefinitions, message,
                                currentIndentationLevel);
                    } catch (IOException ex) {
                        Logger.getLogger(StandardTranslator.class.getName()).
                                log(Level.SEVERE, null, ex);
                        System.exit(1);
                    }
                } else {
                    String message = MessageFormat.format("{0} {1}[{2}];\n",
                            typeName, name, currentChild.getValueAsString(
                            "arrayCapacity"));
                    try {
                        Utilities.writeWithIndentation(typeDefinitions, message,
                                currentIndentationLevel);
                    } catch (IOException ex) {
                        Logger.getLogger(StandardTranslator.class.getName()).
                                log(Level.SEVERE, null, ex);
                        System.exit(1);
                    }
                }
            }
        }
        dedent();
        typeDefinitions.write(MessageFormat.format("'}' {0};",
                typeDefinition.getName()));
        typeDefinitions.write("\n\n");
    }

    private void handleComment(ASTNode comment) {
        String commentAsString = comment.getValueAsString();
        assert commentAsString.startsWith("/*")
                && commentAsString.endsWith("*/");
        //Remove comment delimiters "/*" and "*/"
        commentAsString = commentAsString.replace("/*", "").replace("*/", "");
        String trimmedComment = commentAsString.trim();
        boolean isAnnotatedComment = trimmedComment.startsWith("@");
        if (isAnnotatedComment) {
            Logger.getLogger(StandardTranslator.class.getName()).log(Level.INFO,
                    "Found an annotated comment: {0}", trimmedComment);
            handleAnnotatedComment(trimmedComment);
        }
    }

    private void handleDefineDirective(ASTNode defineDirective) {
        String defineDirectiveAsString = defineDirective.getValueAsString();
        globalDefinitions.write(defineDirectiveAsString);

        Logger.getLogger(StandardTranslator.class.getName()).log(Level.INFO,
                "Handling define directive {0}", defineDirectiveAsString);
        //Remove the #define at the beginning and parse the directive itself
        defineDirectiveAsString = defineDirectiveAsString.substring(
                "#define".length()).trim();
        boolean isMacro = defineDirectiveAsString.contains("(");
        if (!isMacro) {
            String directiveName = defineDirectiveAsString.substring(0,
                    defineDirectiveAsString.indexOf(" "));
            String directiveValue = defineDirectiveAsString.substring(
                    defineDirectiveAsString.indexOf(" ")).trim();
            nonMacros.put(directiveName, directiveValue);
            //Template-related non-macros
            if (template instanceof RoundBasedProtocolGeneric) {
                if (directiveName.equals("NUMBER_OF_PROCESSES")) {
                    int numberOfParticipants = Integer.parseInt(directiveValue);
                    Logger.getLogger(StandardTranslator.class.getName()).log(
                            Level.INFO, "RoundBasedProtocolGeneric will have "
                            + "{0} participants.", numberOfParticipants);
                    ((RoundBasedProtocolGeneric) template).
                            setNumberOfParticipants(numberOfParticipants);
                }
            }
        }
    }

    private void translateGlobalDeclaration(ASTNode currentChild) {
    }

    private void translateProcessDefinition(ASTNode currentChild) {
    }

    private void handleAnnotatedComment(String comment) {
        assert comment.startsWith("@");
        comment = comment.substring("@".length());
        String directiveName = comment.replaceFirst("\\(.*\\)", "");
        if (directiveName.equalsIgnoreCase("USES_TEMPLATE")) {
            String parametersAsString = comment.substring(comment.indexOf("("),
                    comment.lastIndexOf(")")).substring("(".length());
            String[] parameters = parametersAsString.split(",");
            for (String parameter : parameters) {
                String[] parameterUnits = parameter.split("=");
                String parameterName = parameterUnits[0];
                String parameterValue = parameterUnits[1];
                if (parameterName.equalsIgnoreCase("template_name")) {
                    String templateName = parameterValue.replaceAll("\"", "");
                    Logger.getLogger(StandardTranslator.class.getName()).log(
                            Level.INFO, "Using template {0}", templateName);
                    template = Template.getTemplate(templateName);
                }
            }
        }
    }
}