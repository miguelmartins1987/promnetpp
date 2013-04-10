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
import java.util.List;

/**
 *
 * @author Miguel Martins
 */
public class Process {

    private String name;
    private boolean active;
    private List<String> inputGates;

    public Process(String name, boolean active) {
        this.name = name;
        this.active = active;
        this.inputGates = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getInputGates() {
        return inputGates;
    }
    
    public String getInitInputGate() {
        for (String gate : inputGates) {
            if (gate.startsWith("init_")) {
                return gate;
            }
        }
        return null;
    }
    
    public void addInputGate(String gate) {
        inputGates.add(gate);
    }
}
