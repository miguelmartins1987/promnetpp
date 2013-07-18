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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Miguel Martins
 */
public class Protocol {

    private static final Map<String, List<Integer>> irrelevantLineNumbers;

    static {
        irrelevantLineNumbers = new HashMap<String, List<Integer>>();
        //NewOneThirdRule
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(173);
        numbers.add(194);
        irrelevantLineNumbers.put("NewOneThirdRule.pml", numbers);
        //1-of-n
        numbers = new ArrayList<Integer>();
        numbers.add(215);
        numbers.add(236);
        irrelevantLineNumbers.put("1-of-n.pml", numbers);
    }

    public static String excludeIrrelevantLineNumbers(String fileName,
            String lineNumbers) {
        List<Integer> localIrrelevant = irrelevantLineNumbers.get(fileName);
        List<Integer> localAll = toIntegerList(lineNumbers);
        for (int i = 0; i < localAll.size(); ++i) {
            if (localIrrelevant.contains(localAll.get(i))) {
                localAll.remove(i);
                --i;
            }
        }
        return toString(localAll);
    }

    private static List<Integer> toIntegerList(String lineNumbers) {
        List<Integer> ret = new ArrayList<Integer>();
        String[] lineNumbersAsArray = lineNumbers.split(",");
        for (String line : lineNumbersAsArray) {
            ret.add(Integer.parseInt(line));
        }
        return ret;
    }

    private static String toString(List<Integer> localAll) {
        String ret = "";
        for (Integer i : localAll) {
            ret += i + ",";
        }
        if (ret.isEmpty()) {
            return ret;
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }
}
