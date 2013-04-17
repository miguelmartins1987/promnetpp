/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.translation.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Miguel Martins
 */
public class Process {
    private String name;
    private Map<String, String> variables;
    private List<String> calledFunctions;

    public Process(String name) {
        this.name = name;
        variables = new HashMap<String, String>();
        calledFunctions = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }
    
    public void addVariable(String type, String name) {
        variables.put(name, type);
    }

    public Map<String, String> getVariables() {
        return variables;
    }
    
    public void addFunction(String functionName) {
        calledFunctions.add(functionName);
    }

    public List<String> getCalledFunctions() {
        return calledFunctions;
    }
}
