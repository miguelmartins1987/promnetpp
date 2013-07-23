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
import com.googlecode.promnetpp.other.Utilities;
import com.googlecode.promnetpp.parsing.ASTNode;
import com.googlecode.promnetpp.translation.LocalVariableMap;
import com.googlecode.promnetpp.translation.StandardTranslatorData;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Miguel Martins
 */
public class RoundBasedProtocolGeneric extends Template {

    private int numberOfParticipants = -1;
    private String numberOfParticipantsName;

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
            String[] components = directiveAsString.split(" ");
            numberOfParticipantsName = components[0];
            numberOfParticipants = Integer.parseInt(components[1]);
            System.out.println("Round-based protocol will have " +
                    numberOfParticipants + " participants.");
        }
    }

    @Override
    public void writeDynamicFiles() throws IOException {
        StandardTranslatorData.localVariables.normalize();
        //Handle header files first
        String initProcessVariables = StandardTranslatorData.localVariables.
                getVariablesAsString("init",
                LocalVariableMap.DECLARATIONS_WITHOUT_INITIALIZERS);
        String processFunctions = StandardTranslatorData.getSpecificFunctions("Process");
        String processVariables = StandardTranslatorData.localVariables.
                getVariablesAsString("Process",
                LocalVariableMap.DECLARATIONS_WITHOUT_INITIALIZERS);
        setDynamicFileParameters("init_process.h", initProcessVariables);
        setDynamicFileParameters("_process.h", processFunctions,
                processVariables, numberOfParticipantsName);
        //Handle .cc files now
        initProcessVariables = StandardTranslatorData.localVariables.
                getVariablesAsString("init",
                LocalVariableMap.DECLARATIONS_WITHOUT_TYPE_NAMES);
        processVariables = StandardTranslatorData.localVariables.
                getVariablesAsString("Process",
                LocalVariableMap.DECLARATIONS_WITHOUT_TYPE_NAMES);
        String computeMessageCode = getSpecificFunctionWriter(
                "compute_message").toString();
        String stateTransitionCode = getSpecificFunctionWriter(
                "state_transition").toString();
        String globalDeclarationsFromPROMELAModel = Utilities.
                externalizeGlobalDeclarations(globalDeclarationsWriter.
                toString());
        String systemEveryRoundCode = getSpecificFunctionWriter(
                "system_every_round").toString();
        String systemInitCode = getSpecificFunctionWriter(
                "system_init").toString();
        setDynamicFileParameters("init_process.cc",
                globalDeclarationsFromPROMELAModel, initProcessVariables,
                numberOfParticipantsName, systemEveryRoundCode, systemInitCode);
        
        String externalDeclarations = StandardTranslatorData.getExternalDeclarations();
        String messageVariable = StandardTranslatorData.getMessageVariable();
        String computeMessageParameters = StandardTranslatorData.getParameters(
                "compute_message");
        setDynamicFileParameters("_process.cc", externalDeclarations,
                processVariables,
                messageVariable, numberOfParticipantsName,
                computeMessageParameters, computeMessageCode,
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
        if (numberOfParticipants < 0) {
            System.err.println("Undefined number of participants.");
            System.err.println("Correct this error by attaching a "
                    + " /* @TemplateParameter(name=\"numberOfParticipants\") */");
            System.err.println("annotated comment to a #define directive.");
            System.exit(1);
        }
        NEDFileContents = MessageFormat.format(NEDFileContents,
                numberOfParticipants);
        FileUtils.writeStringToFile(new File(Options.outputDirectory + "/"
                    + "network.ned"), NEDFileContents);
    }

    @Override
    public void checkForErrors() {
        if (!usedBlocks.contains("generic_part")) {
            System.err.println("The PROMELA model doesn't seem to contain a"
                    + " generic part.");
            System.err.println("Models using the round-based consensus"
                    + " template are required to have a generic part"
                    + " delimited by the following annotated comments:");
            System.err.println("/* @BeginTemplateBlock(name=\"generic_part\") */");
            System.err.println("/* @EndTemplateBlock */");
            System.exit(1);
        }
    }
}
