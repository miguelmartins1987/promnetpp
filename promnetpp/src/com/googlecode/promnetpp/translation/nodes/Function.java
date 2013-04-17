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

import com.googlecode.promnetpp.parsing.ASTNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Miguel Martins
 */
public class Function {

    private String name;
    private ASTNode parameters;
    private ASTNode instructions;
    private List<String> callers;
    private List<String> callerTypes;

    public Function(String name) {
        this.name = name;
        callers = new ArrayList<String>();
        callerTypes = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public ASTNode getParameters() {
        return parameters;
    }

    public void setParameters(ASTNode parameters) {
        this.parameters = parameters;
    }

    public ASTNode getInstructions() {
        return instructions;
    }

    public void setInstructions(ASTNode instructions) {
        this.instructions = instructions;
    }

    public void addCaller(String caller, String callerType) {
        callers.add(caller);
        callerTypes.add(callerType);
    }

    public boolean hasSingleCaller() {
        return callers.size() == 1;
    }

    public String getFirstCaller() {
        if (callers.size() > 0) {
            return callers.get(0);
        }
        return null;
    }

    public String getFirstCallerType() {
        if (callers.size() > 0) {
            return callerTypes.get(0);
        }
        return null;
    }

    public void normalize(Map<String, Function> functions) {
        for (int i = 0; i < callers.size(); ++i) {
            if (callerTypes.get(i).equals("function")) {
                normalizeCaller(i, functions);
            }
        }
    }

    private void normalizeCaller(int i, Map<String, Function> functions) {
        String callerName = callers.get(i);
        Function caller = functions.get(callerName);
        if (caller.hasSingleCaller()) {
            if (caller.getFirstCallerType().equalsIgnoreCase("process")) {
                callers.set(i, caller.getFirstCaller());
                callerTypes.set(i, "process");
            }
        }
    }
}
