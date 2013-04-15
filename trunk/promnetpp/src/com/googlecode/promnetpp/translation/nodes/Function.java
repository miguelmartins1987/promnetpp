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

/**
 *
 * @author Miguel Martins
 */
public class Function {
    private String name;
    private ASTNode parameters;
    private ASTNode instructions;
    private List<String> callers;

    public Function(String name) {
        this.name = name;
        callers = new ArrayList<String>();
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
    
    public void addCaller(String caller) {
        callers.add(caller);
    }
    
    public boolean hasSingleCaller() {
        return callers.size() == 1;
    }
    
    public String getFirstCaller() {
        return callers.get(0);
    }
}
