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

import com.googlecode.promnetpp.parsing.ASTNode;
import com.googlecode.promnetpp.utilities.IndentedStringWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Martins
 */
public class StandardTranslatorData {
    public static LocalVariableMap localVariables = new LocalVariableMap();
    public static List<String> specificFunctions = new ArrayList<String>();
    public static String messageVariable;
    public static List<String> externalDeclarations = new ArrayList<String>();

    public static String getSpecificFunctions(String processName) {
        IndentedStringWriter specificFunctionsWriter =
                new IndentedStringWriter();
        specificFunctionsWriter.indent();
        for (String header : specificFunctions) {
            try {
                specificFunctionsWriter.write(header + ";\n");
            } catch (IOException ex) {
                Logger.getLogger(StandardTranslatorData.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        specificFunctionsWriter.dedent();
        return specificFunctionsWriter.toString();
    }

    public static String getMessageVariable() {
        return "this->" + messageVariable;
    }

    public static void setMessageVariable(String messageVariable) {
        StandardTranslatorData.messageVariable = messageVariable;
    }

    public static void addSpecificFunction(String functionName,
            ASTNode functionParameters) {
        List<String> identifiers = functionParameters != null ? (List<String>)
                functionParameters.getValue("identifiers") :
                new ArrayList<String>();
        String functionHeader = "void " + functionName + "(";
        int identifierIndex = 0;
        for (String identifier : identifiers) {
            String identifierType = getParameterType(functionName, identifierIndex);
            functionHeader += identifierType + " " + identifier + ", ";
            ++identifierIndex;
        }
        int end = functionHeader.indexOf(", ");
        if (end >= 0) {
            functionHeader = functionHeader.substring(0, end);
        }
        functionHeader += ")";
        System.out.println(functionHeader);
        
        if (functionName.equals("system_init") ||
                functionName.equals("system_every_round")) {
            return;
        }
        specificFunctions.add(functionHeader);
    }

    public static void addSpecificFunction(String functionName) {
        addSpecificFunction(functionName, null);
    }

    private static String getParameterType(String functionName, int identifierIndex) {
        if (functionName.equals("compute_message")) {
            if (identifierIndex == 0) {
                return "message_t&";
            }
        }
        return "int";
    }

    public static String getParameters(String functionName) {
        for (String header : specificFunctions) {
            if (header.contains(functionName)) {
                String parameters = header.substring(header.indexOf("(") + 1);
                parameters = parameters.substring(0, parameters.indexOf(")"));
                return parameters;
            }
        }
        return "";
    }

    public static void addExternalDeclaration(String declaration) {
        externalDeclarations.add(declaration);
    }
    
    public static String getExternalDeclarations() {
        IndentedStringWriter writer = new IndentedStringWriter();
        for (String declaration : externalDeclarations) {
            String externalizedDeclaration = externalize(declaration);
            try {
                writer.write(externalizedDeclaration + "\n");
            } catch (IOException ex) {
                Logger.getLogger(StandardTranslatorData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return writer.toString();
    }

    private static String externalize(String declaration) {
        int end = declaration.indexOf(" = ");
        if (end >= 0) {
            declaration = declaration.substring(0, end);
        }
        if (!declaration.endsWith(";")) {
            declaration += ";";
        }
        return "extern " + declaration;
    }
}
