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

import java.util.List;

/**
 *
 * @author Miguel Martins
 */
public class Channel {

    private String name;
    private int capacity;
    private List<String> typeNames;

    public Channel(String name, int capacity, List<String> typeNames) {
        this.name = name;
        this.capacity = capacity;
        this.typeNames = typeNames;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getTypeNames() {
        return typeNames;
    }
}
