/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.utilities;

import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author Miguel Martins
 */
public class IndentedStringWriter extends IndentedWriter {

    public IndentedStringWriter() {
        super();
        writer = new StringWriter();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public String toString() {
        return ((StringWriter)writer).toString();
    }
}
