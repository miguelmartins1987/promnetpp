/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.other;

import com.googlecode.promnetpp.parsing.ASTNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The
 * <code>Utilities</code> class contains utility methods.
 *
 * @author Miguel Martins
 */
public class Utilities {

    /**
     * Creates a directory given a directory name. If the corresponding
     * directory already exists, nothing happens.
     *
     * @param directoryName the name of the directory
     * @see File#mkdir()
     */
    public static void makeDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            boolean directoryCreated = directory.mkdir();
            assert directoryCreated : "Could not create output directory!";
        }
    }

    /**
     * Builds a
     * <code>Document</code> object from a file. Exceptions that may occur are
     * not thrown, but logged instead.
     *
     * @param file the file containing an XML document
     * @return the XML document, as a <code>Document</code> object, if
     * successful. Otherwise, <code>null</code> is returned.
     */
    public static Document getDocumentFromFile(File file) {
        try {
            DocumentBuilderFactory documentBuilderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder =
                    documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(file);
        } catch (SAXException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return null;
    }

    public static List<String> searchForFunctionCalls(ASTNode node) {
        ASTNode child;
        List<String> functionNames = new ArrayList<String>();
        for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
            child = (ASTNode) node.jjtGetChild(i);
            if (child.getNodeName().equals("FunctionCall")) {
                functionNames.add(child.getValueAsString("functionName"));
            } else {
                functionNames.addAll(searchForFunctionCalls(child));
            }
        }
        return functionNames;
    }

    public static String externalizeGlobalDeclarations(String
            globalDeclarations) {
        globalDeclarations = globalDeclarations.replaceAll(" =.*", ";");
        //Add the "extern" keyword to all global declarations
        globalDeclarations = "extern " + globalDeclarations;
        globalDeclarations = globalDeclarations.replaceAll("\n", "\nextern ");
        //Remove the extra "extern " at the end
        if (globalDeclarations.endsWith("extern ")) {
            globalDeclarations = globalDeclarations.substring(0,
                    globalDeclarations.lastIndexOf("extern "));
        }
        return globalDeclarations;
    }
}
