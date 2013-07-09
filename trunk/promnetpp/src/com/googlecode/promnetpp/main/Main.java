/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.main;

import com.googlecode.promnetpp.verification.StandardVerifier;
import com.googlecode.promnetpp.options.Options;
import com.googlecode.promnetpp.other.Utilities;
import com.googlecode.promnetpp.parsing.AbstractSyntaxTree;
import com.googlecode.promnetpp.parsing.PROMELAParser;
import com.googlecode.promnetpp.parsing.ParseException;
import com.googlecode.promnetpp.translation.StandardTranslator;
import com.googlecode.promnetpp.translation.Translator;
import com.googlecode.promnetpp.verification.Verifier;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

/**
 * Main class for PROMNeT++.
 *
 * @author Miguel Martins
 */
public class Main {
    /*
     * We will require the use of assertions, for the time being.
     * Source: http://docs.oracle.com/javase/1.4.2/docs/guide/lang/assert.html
     */

    static {
        boolean assertsEnabled = false;
        assert assertsEnabled = true; //Intentional side effect
        if (!assertsEnabled) {
            throw new RuntimeException("PROMNeT++ requires assertions to run.\n"
                    + "Please make sure you invoked PROMNeT++ with the"
                    + " -enableassertions switch.");
        }
    }
    /**
     * The name/path to the input file (PROMELA specification)
     */
    private static String fileNameOrPath;
    /**
     * The name/path to the configuration file
     */
    private static String configurationFilePath;
    /**
     * Contents of the PROMELA file, as a string
     */
    private static String sourceCode;
    /**
     * The abstract syntax tree
     */
    private static AbstractSyntaxTree abstractSyntaxTree;

    /**
     * Main function (entry point for the tool).
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        //Prepare logging
        try {
            Handler fileHandler = new FileHandler("promnetpp-log.xml");
            Logger logger = Logger.getLogger("");
            logger.removeHandler(logger.getHandlers()[0]);
            logger.addHandler(fileHandler);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        String PROMNeTppHome = System.getenv("PROMNETPP_HOME");
        if (PROMNeTppHome == null) {
            String userDir = System.getProperty("user.dir");
            System.err.println("WARNING: PROMNETPP_HOME environment variable"
                    + " not set.");
            System.err.println("PROMNeT++ will assume " + userDir + " as"
                    + " home.");
            PROMNeTppHome = userDir;
        }
        System.setProperty("promnetpp.home", PROMNeTppHome);
        
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "PROMNeT++ home"
                + " set to {0}", System.getProperty("promnetpp.home"));
        if (args.length == 1) {
            fileNameOrPath = args[0];
            configurationFilePath = PROMNeTppHome +
                    "/default-configuration.xml";
        } else if (args.length == 2) {
            fileNameOrPath = args[0];
            configurationFilePath = args[1];
        } else {
            System.err.println("Invalid number of command-line arguments.");
            System.err.println("Usage #1: promnetpp.jar <PROMELA model>.pml");
            System.err.println("Usage #2: promnetpp.jar <PROMELA model>.pml"
                    + " <configuration file>.xml");
            System.exit(1);
        }
        //We must have a file name or path at this point
        assert fileNameOrPath != null : "Unspecified file name or"
                + " path to file!";

        //Log basic info
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "Running"
                + " PROMNeT++ from {0}", System.getProperty("user.dir"));
        //Final steps
        loadXMLFile();
        Verifier verifier = new StandardVerifier(fileNameOrPath);
        verifier.doVerification();
        assert verifier.isErrorFree() : "Errors reported during model"
                + " verification!";
        verifier.finish();
        buildAbstractSyntaxTree();
        Translator translator = new StandardTranslator();
        translator.init();
        translator.translate(abstractSyntaxTree);
        translator.finish();
    }

    private static void loadXMLFile() {
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "Loading "
                + "configuration from file {0}", configurationFilePath);
        File configurationFile = new File(configurationFilePath);
        assert configurationFile.exists() : "Configuration file "
                + configurationFilePath + " does not exist!";
        Document configurationDocument = Utilities.getDocumentFromFile(
                configurationFile);
        configurationDocument.getDocumentElement().normalize();
        String expectedRootElementName = "promnetppConfiguration";
        String actualRootElementName =
                configurationDocument.getDocumentElement().getNodeName();
        assert expectedRootElementName.equals(actualRootElementName) : ""
                + "Configuration document " + configurationFilePath + " is"
                + " malformed! Root element must be " + expectedRootElementName
                + ". Found: " + actualRootElementName;
        Options.fromDocument(configurationDocument);
    }

    private static void buildAbstractSyntaxTree() {
        //Read source code from file
        try {
            sourceCode = FileUtils.readFileToString(new File(fileNameOrPath));
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
        //Preprocess the source code, as necessary
        preprocessSourceCode();
        //Parse it!
        StringReader reader = new StringReader(sourceCode);
        PROMELAParser parser = new PROMELAParser(reader);
        try {
            abstractSyntaxTree = new AbstractSyntaxTree(parser.Start());
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
                    "Error while parsing the input file!", ex);
            System.err.println(ex);
            showOffendingLine(ex);
        }
        assert abstractSyntaxTree != null : "Could not build an"
                + " abstract syntax tree!";
    }

    private static void preprocessSourceCode() {
        StringBuilder sourceCodeAsBuilder = new StringBuilder(sourceCode);
        String commentRegex = "/[*].*?[*]/";
        Pattern commentPattern = Pattern.compile(commentRegex, Pattern.DOTALL);
        Matcher commentMatcher = commentPattern.matcher(sourceCodeAsBuilder);
        while (commentMatcher.find()) {
            String comment = commentMatcher.group().replace("/*", "")
                    .replace("*/", "").trim();
            boolean isAnnotatedComment = comment.startsWith("@");
            if (!isAnnotatedComment) {
                //Remove the comment and reset the matcher
                sourceCodeAsBuilder.delete(commentMatcher.start(),
                        commentMatcher.end());
                commentMatcher = commentPattern.matcher(sourceCodeAsBuilder);
            }
        }
        sourceCode = sourceCodeAsBuilder.toString();
    }

    private static void showOffendingLine(ParseException parseException) {
        int lineNumber = parseException.currentToken.next.beginLine;
        String line = sourceCode.split("\n")[lineNumber - 1];
        System.err.println("Line " + lineNumber + " is:");
        System.err.println(line);
    }
}
