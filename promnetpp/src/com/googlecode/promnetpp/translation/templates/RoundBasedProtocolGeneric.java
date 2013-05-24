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

import com.googlecode.promnetpp.options.Options;
import com.googlecode.promnetpp.parsing.ASTNode;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Miguel Martins
 */
public class RoundBasedProtocolGeneric extends Template {

    private int numberOfParticipants;

    public RoundBasedProtocolGeneric() {
        super();
        name = "round_based_protocol_generic";
        addDynamicFile("init_process.h");
        addDynamicFile("init_process.cc");
        addDynamicFile("_process.h");
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
    public String getGlobalDeclarations() {
        return "byte number_of_processes_in_current_round = 0;";
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
        //Handle header files first
        String initProcessVariables = getLocalVariableDeclarationWriter("init")
                .toString();
        String processVariables = getLocalVariableDeclarationWriter("Process")
                .toString();
        setDynamicFileParameters("init_process.h", initProcessVariables);
        setDynamicFileParameters("_process.h", processVariables);
        //Handle .cc files now
        String computeMessageCode = getSpecificFunctionWriter(
                "compute_message").toString();
        String stateTransitionCode = getSpecificFunctionWriter(
                "state_transition").toString();
        String systemEveryRoundCode = getSpecificFunctionWriter(
                "system_every_round").toString();
        String systemInitCode = getSpecificFunctionWriter(
                "system_init").toString();
        setDynamicFileParameters("init_process.cc", systemEveryRoundCode,
                systemInitCode);
        setDynamicFileParameters("_process.cc", computeMessageCode,
                stateTransitionCode);
        super.writeDynamicFiles();
    }

    @Override
    public void writeMessageDefinitionFiles() throws IOException {
        String messageTemplatePath = System.getProperty("promnetpp.home")
                + "/templates/" + name + "/message.msg";
        String messageFileContents = FileUtils.readFileToString(new File(
                messageTemplatePath));
        messageFileContents = MessageFormat.format(messageFileContents,
                "message_t");
        FileUtils.writeStringToFile(new File(Options.outputDirectory + "/"
                    + "message.msg"), messageFileContents);
    }

    @Override
    public void writeNEDFile() throws IOException {
        String NEDTemplatePath = System.getProperty("promnetpp.home")
                + "/templates/" + name + "/network.ned";
        String NEDFileContents = FileUtils.readFileToString(new File(
                NEDTemplatePath));
        NEDFileContents = MessageFormat.format(NEDFileContents,
                numberOfParticipants);
        FileUtils.writeStringToFile(new File(Options.outputDirectory + "/"
                    + "network.ned"), NEDFileContents);
    }
}
