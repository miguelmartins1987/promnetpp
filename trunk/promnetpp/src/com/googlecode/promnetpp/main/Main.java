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

import com.googlecode.promnetpp.options.Options;
import com.googlecode.promnetpp.other.Utilities;
import com.googlecode.promnetpp.parsing.AbstractSyntaxTree;
import com.googlecode.promnetpp.parsing.PROMELAParser;
import com.googlecode.promnetpp.parsing.ParseException;
import com.googlecode.promnetpp.translation.StandardTranslator;
import com.googlecode.promnetpp.translation.Translator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        assert assertsEnabled = true; // Intentional side effect!!!
        if (!assertsEnabled) {
            throw new RuntimeException("Asserts must be enabled!!!");
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
            System.setProperty("promnetpp.home",
                    System.getProperty("user.dir"));
        } else {
            System.setProperty("promnetpp.home", PROMNeTppHome);
        }
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "PROMNeT++ home"
                + " set to {0}", System.getProperty("promnetpp.home"));
        if (args.length == 1) {
            fileNameOrPath = args[0];
            configurationFilePath = "default-configuration.xml";
        } else if (args.length == 2) {
            fileNameOrPath = args[0];
            configurationFilePath = args[1];
        } else {
            System.err.println("Invalid number of command-line arguments!");
            System.exit(1);
        }
        //We must have a file name or path at this point
        assert fileNameOrPath != null : "Unspecified file name or"
                + " path to file!";
        
        //Log basic info
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "Running"
                + " PROMNeT++ from {0}", System.getProperty("user.dir"));
        //Load and parse the XML for the configuration file
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
        StringReader reader = null;
        try {
            String sourceCode = FileUtils.readFileToString(new File(
                    fileNameOrPath));
            reader = new StringReader(sourceCode);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        PROMELAParser parser = new PROMELAParser(reader);
        try {
            abstractSyntaxTree = new AbstractSyntaxTree(parser.Start());
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
                    "Error while parsing the input file!", ex);
            System.err.println(ex);
        }
        assert abstractSyntaxTree != null : "Could not build an"
                + " abstract syntax tree!";
        Translator translator = new StandardTranslator();
        translator.init();
        translator.translate(abstractSyntaxTree);
        translator.finish();
    }
}
