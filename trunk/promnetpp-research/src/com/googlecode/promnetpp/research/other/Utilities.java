/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.research.other;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Miguel Martins
 */
public class Utilities {

    public static String getStreamAsString(InputStream stream) throws IOException {
        String output = "";
        byte[] buffer = new byte[4096];
        while (stream.read(buffer) > 0) {
            output = output + new String(buffer);
        }
        return output;
    }
}
