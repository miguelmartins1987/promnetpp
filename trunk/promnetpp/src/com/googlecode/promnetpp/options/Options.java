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
 *
 * @author Miguel Martins
 */
public class Options {

    public static boolean perChannelTypeClasses = false;
    public static String outputDirectory = System.getProperty("user.dir");

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

    private static void handleSimpleOption(Element simpleOption) {
        String optionName = simpleOption.getAttribute("name");
        String optionValue = simpleOption.getAttribute("value");

        if (optionName.equals("outputDirectory")) {
            outputDirectory = optionValue;
            Logger.getLogger(Options.class.getName()).log(Level.INFO, "Will "
                    + "output files to directory {0}", outputDirectory);
        } else if (optionName.equals("perChannelTypeClasses")) {
            if (optionValue.equals("true")) {
                perChannelTypeClasses = true;
            }
        }
    }
}
