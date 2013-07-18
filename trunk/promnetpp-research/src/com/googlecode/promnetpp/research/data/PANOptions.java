/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.research.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Miguel Martins
 */
public class PANOptions {
    private static final Map<String, String> runtimeOptionMap;
    
    static {
        runtimeOptionMap = new HashMap<String, String>();
        runtimeOptionMap.put("NewOneThirdRule.pml", "");
        runtimeOptionMap.put("1-of-n.pml", "-E");
    }
    
    public static String getRuntimeOptionsFor(String fileName) {
        return runtimeOptionMap.get(fileName);
    }
    
}
