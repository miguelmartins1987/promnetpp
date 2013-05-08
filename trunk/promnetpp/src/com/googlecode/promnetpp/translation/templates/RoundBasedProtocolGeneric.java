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

import com.googlecode.promnetpp.parsing.ASTNode;
import java.io.IOException;

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
        addStaticFile("_process.h");
        addDynamicFile("_process.cc");
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
        System.out.println("Round-based protocol will have "
                + numberOfParticipants + " participants.");
    }

    public boolean inGenericPartBlock() {
        return currentBlock.equalsIgnoreCase("generic_part");
    }

    @Override
    public void handleTemplateParameter(ASTNode directive,
            String parameterName) {
        String directiveAsString = directive.getValueAsString().trim();
        System.out.println("The directive below shall be regarded as a template"
                + " parameter.");
        System.out.println(directiveAsString);
        directiveAsString = directiveAsString.substring("#define".length())
                .trim();
        if (parameterName.equalsIgnoreCase("numberOfParticipants")) {
            numberOfParticipants = Integer.parseInt(directiveAsString.split(" ")
                    [1]);
            System.out.println("Round-based protocol will have " +
                    numberOfParticipants + " participants.");
        }
    }

    @Override
    public void writeDynamicFiles() throws IOException {
        String handleMessageCode = "//TODO";
        String computeMessageCode = getSpecificFunctionWriter(
                "compute_message").toString();
        String stateTransitionCode = getSpecificFunctionWriter(
                "state_transition").toString();
        setDynamicFileParameters("_process.cc", handleMessageCode,
                computeMessageCode, stateTransitionCode);
        super.writeDynamicFiles();
    }
}
