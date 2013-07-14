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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Miguel Martins
 */
public class LocalVariableMap extends HashMap<String, ArrayList<String>> {
    public static final int DECLARATIONS_WITHOUT_INITIALIZERS = 1;
    public static final int DECLARATIONS_WITHOUT_TYPE_NAMES = 2;

    public void putVariable(String processName, String variableDeclaration) {

        ArrayList<String> declarations = this.get(processName);
        if (declarations == null) {
            declarations = new ArrayList<String>();
        }
        declarations.add(variableDeclaration);
        this.put(processName, declarations);
    }

    public ArrayList<String> getVariables(String processName) {
        return this.get(processName);
    }
    
    public String getVariablesAsString(String processName, int format) {
        LocalVariableMap map = (LocalVariableMap) this.clone();
        if (format == DECLARATIONS_WITHOUT_INITIALIZERS) {
            map.removeInitializers(processName);
        } else if (format == DECLARATIONS_WITHOUT_TYPE_NAMES) {
            map.removeNonInitializableTypes(processName);
            map.addInitializers(processName);
            map.removeTypeNames(processName);
        }
        
        String ret = "";
        ArrayList<String> declarations = map.get(processName);
        for (String variable : declarations) {
            ret += "    ";
            ret += variable;
            ret += ";\n";
        }
        return ret;
    }

    private void removeInitializers(String processName) {
        ArrayList<String> declarations = (ArrayList<String>)
                this.get(processName).clone();
        for (int i = 0; i < declarations.size(); ++i) {
            String variable = declarations.get(i);
            boolean containsAssignment = variable.contains("=");
            if (containsAssignment) {
                variable = variable.substring(0, variable.indexOf("=")).trim();
            }
            declarations.set(i, variable);
        }
        this.put(processName, declarations);
    }

    private void addInitializers(String processName) {
        ArrayList<String> declarations = this.get(processName);
        for (int i = 0; i < declarations.size(); ++i) {
            String variable = declarations.get(i);
            boolean containsAssignment = variable.contains("=");
            if (!containsAssignment) {
                String variableType = variable.split(" ")[0];
                if (variableType.equals("byte")) {
                    variable += " = 0";
                } else if (variableType.equals("bool")) {
                    variable += " = false";
                } else if (variableType.equals("int")) {
                    variable += " = 0";
                }
            }
            declarations.set(i, variable);
        }
    }

    private void removeTypeNames(String processName) {
        ArrayList<String> declarations = this.get(processName);
        for (int i = 0; i < declarations.size(); ++i) {
            String variable = declarations.get(i);
            String variableType = variable.split(" ")[0];
            variable = variable.substring(variableType.length()).trim();
            declarations.set(i, variable);
        }
    }

    private void removeNonInitializableTypes(String processName) {
        ArrayList<String> declarations = (ArrayList<String>)
                this.get(processName).clone();
        for (int i = 0; i < declarations.size(); ++i) {
            String variable = declarations.get(i);
            String variableType = variable.split(" ")[0];
            boolean isInitializableType = variableType.equals("byte") ||
                    variableType.equals("bool") || variableType.equals("int");
            if (!isInitializableType) {
                declarations.remove(i);
                --i;
            }
        }
        this.put(processName, declarations);
    }

    public void normalize() {
        for (String key : this.keySet()) {
             ArrayList<String> declarations = this.get(key);
             for (int i = 0; i < declarations.size(); ++i) {
                 String variable = declarations.get(i);
                 String variableType = variable.split(" ")[0];
                 if (variableType.equals("message")) {
                     variable = variable.replace("message ", "message_t ");
                 }
                 declarations.set(i, variable);
             }
        }
    }
}
