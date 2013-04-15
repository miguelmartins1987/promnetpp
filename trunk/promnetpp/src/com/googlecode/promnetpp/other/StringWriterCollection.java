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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Miguel Martins
 */
public class StringWriterCollection {

    private Map<String, StringWriter> writers;

    public StringWriterCollection() {
        writers = new HashMap<String, StringWriter>();
    }

    public void addWriter(String name) {
        writers.put(name, new StringWriter());
    }

    public StringWriter getWriter(String name) {
        return writers.get(name);
    }
}
