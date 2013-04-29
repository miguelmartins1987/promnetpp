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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Miguel Martins
 */
public class IndentedFileWriter extends IndentedWriter {

    public IndentedFileWriter(File file) throws IOException {
        writer = new FileWriter(file);
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
}
