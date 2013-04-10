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

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The
 * <code>Utilities</code> class contains utility methods.
 *
 * @author Miguel Martins
 */
public class Utilities {

    public static void makeDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            boolean directoryCreated = directory.mkdir();
            assert directoryCreated : "Could not create output directory!";
        }
    }

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

    public static void writeWithIndentation(Writer writer, String message,
            int indentation) throws IOException {
        String indentationString = StringUtils.repeat(" ", indentation);
        message = message.replace("\n", "\n" + indentationString);
        if (message.endsWith(indentationString)) {
            message = message.substring(0, message.length()
                    - indentationString.length());
        }
        writer.write(indentationString);
        writer.write(message);
    }
}
