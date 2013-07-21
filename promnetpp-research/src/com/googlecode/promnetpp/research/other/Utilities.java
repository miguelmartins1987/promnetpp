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
    public static final String operatingSystemType;
    
    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("win") >= 0) {
            operatingSystemType = "windows";
        } else if (osName.indexOf("mac") >= 0) {
            operatingSystemType = "mac";
        } else {
            operatingSystemType = "linux";
        }
    }

    public static String getStreamAsString(InputStream stream) throws IOException {
        String output = "";
        byte[] buffer = new byte[4096];
        while (stream.read(buffer) > 0) {
            output = output + new String(buffer);
        }
        return output;
    }
}
