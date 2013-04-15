/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.translation.templates;

/**
 *
 * @author Miguel Martins
 */
public class RoundBasedProtocolGeneric extends Template {
    private int numberOfParticipants;

    public RoundBasedProtocolGeneric() {
        super();
        name = "round_based_protocol_generic";
        addStaticFile("init_process.h");
        addStaticFile("init_process.cc");
        addDynamicFile("_process.h");
        addDynamicFile("_process.cc");
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }
}
