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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Miguel Martins
 */
public class StackManager {

    private List<Stack<Integer>> integerStacks;
    private List<Stack<String>> stringStacks;

    public StackManager() {
        integerStacks = new ArrayList<Stack<Integer>>();
        stringStacks = new ArrayList<Stack<String>>();
    }

    public Stack<Integer> getIntegerStack(int index) {
        if (index == integerStacks.size()) {
            Stack<Integer> stack = new Stack<Integer>();
            integerStacks.add(stack);
        }
        return integerStacks.get(index);
    }

    public Stack<String> getStringStack(int index) {
        if (index == stringStacks.size()) {
            Stack<String> stack = new Stack<String>();
            stringStacks.add(stack);
        }
        return stringStacks.get(index);
    }
}
