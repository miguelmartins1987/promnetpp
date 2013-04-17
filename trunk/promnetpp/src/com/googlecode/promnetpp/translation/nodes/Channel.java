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

import com.googlecode.promnetpp.parsing.ASTNode;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Miguel Martins
 */
public class Channel {
    private String identifier;
    private String capacity;
    private List<String> typeNames;

    public Channel(String identifier) {
        this.identifier = identifier;
        typeNames = new ArrayList<String>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void buildFromInitialization(ASTNode channelInitialization) {
        capacity = channelInitialization.getValueAsString("capacity");
        for (int i = 0; i < channelInitialization.jjtGetNumChildren(); ++i) {
            ASTNode typeName = (ASTNode) channelInitialization.jjtGetChild(i);
            typeNames.add(typeName.getName());
        }
    }
    
    private String getConcatenatedTypeNamesAsString(String case_) {
        String result = "";
        for (String typeName : typeNames) {
            if (case_.equalsIgnoreCase("TitleCase")) {
                result += StringUtils.capitalize(typeName);
            } else if (case_.equalsIgnoreCase("UpperCaseWithUnderscores")) {
                result += typeName.toUpperCase() + "_";
            } else if (case_.equalsIgnoreCase("LowerCaseWithUnderscores")) {
                result += typeName.toLowerCase() + "_";
            }
        }
        return result;
    }
    
    public String toCppClassName() {
        return getConcatenatedTypeNamesAsString("TitleCase") + "Channel";
    }
    
    public String toCppHeaderName() {
        return getConcatenatedTypeNamesAsString("UpperCaseWithUnderscores") +
                "CHANNEL_H_";
    }
    
    public String toCppHeaderFileName() {
        return getConcatenatedTypeNamesAsString("LowerCaseWithUnderscores") +
                "channel.h";
    }
    
    public String toCppSourceFileName() {
        return getConcatenatedTypeNamesAsString("LowerCaseWithUnderscores") +
                "channel.cc";
    }
}
