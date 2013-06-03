/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.options;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The
 * <code>Options</code> class contains PROMNeT++'s configuration, and is thus
 * used to specify some of its behaviors, including (but not limited to) the
 * directory where the resulting files from the translation process should go
 * to, referred to as <code>outputDirectory</code>.
 *
 * @author Miguel Martins
 */
public class Options {
    
    /**
     * Directory where the resulting files from the translation process should
     * go to
     */
    public static String outputDirectory = System.getProperty("user.dir");
    public static boolean skipVerification = false;
    public static String spinHome = "";
    public static String panCompiler = "gcc";

    /**
     * Sets the options given an XML configuration document.
     *
     * @param configurationDocument the XML configuration document
     */
    public static void fromDocument(Document configurationDocument) {
        Element rootElement = configurationDocument.getDocumentElement();
        NodeList children = rootElement.getChildNodes();
        Element currentChild;
        for (int i = 0; i < children.getLength(); ++i) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                currentChild = (Element) node;
                if (currentChild.getNodeName().equals("simpleOption")) {
                    handleSimpleOption(currentChild);
                }
            }
        }
    }

    /**
     * Handles a <code>simpleOption</code> element within the XML configuration
     * document. <code>simpleOption</code> elements should be in the form:
     * <pre>
     * &lt;simpleOption name="nameOfTheOption" value="valueOfTheOption" /&gt;
     * </pre>
     *
     * Both the <code>name</code> and <code>value</code> attributes of the
     * <code>simpleOption</code> element are to be (initially) interpreted as
     * strings, but can be further interpreted as any other type of object. For
     * instance, an option with a value of "false" can later be converted to a
     * <code>bool</code> type.
     * 
     * @param simpleOption the <code>simpleOption</code> element
     */
    private static void handleSimpleOption(Element simpleOption) {
        String optionName = simpleOption.getAttribute("name");
        String optionValue = simpleOption.getAttribute("value");

        if (optionName.equals("outputDirectory")) {
            outputDirectory = optionValue;
            Logger.getLogger(Options.class.getName()).log(Level.INFO, "Will "
                    + "output files to directory {0}", outputDirectory);
        } else if (optionName.equals("spinHome")) {
            spinHome = optionValue;
            Logger.getLogger(Options.class.getName()).log(Level.INFO, "Spin is "
                    + " located in {0} according to configuration file.",
                    spinHome);
        }
    }
}
