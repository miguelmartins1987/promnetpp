/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.translation;

import com.googlecode.promnetpp.options.Options;
import com.googlecode.promnetpp.other.Utilities;
import com.googlecode.promnetpp.parsing.ASTNode;
import com.googlecode.promnetpp.parsing.AbstractSyntaxTree;
import com.googlecode.promnetpp.translation.nodes.Channel;
import com.googlecode.promnetpp.translation.nodes.Function;
import com.googlecode.promnetpp.translation.nodes.Process;
import com.googlecode.promnetpp.translation.templates.RoundBasedProtocolGeneric;
import com.googlecode.promnetpp.translation.templates.Template;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * The
 * <code>StandardTranslator</code> class represents the standard translation
 * procedure to C++ code that's runnable in OMNeT++
 *
 * @author Miguel Martins
 */
public class StandardTranslator implements Translator {

    //Indentation settings
    private static final int SPACES_PER_TAB = 4;
    private int currentIndentationLevel = 0;
    //Whether the translator can skip units (except for annotated comments)
    //May change over time
    private static boolean canSkipUnits = false;
    //The template to be used, if any
    private Template template;
    //Process collection (excluding init)
    private Map<String, Process> processes;
    //Init process
    private Process initProcess;
    //String writers for various purposes (one of them being type definitions)
    private StringWriter typeDefinitions;
    private StringWriter globalDeclarations, globalDefinitions;
    //Other
    private Map<String, Function> functions;
    private String currentFunction;
    private Map<String, String> macros, nonMacros;
    private ASTNode lastWrittenInstruction;
    
    private int currentStep = 0;
    private boolean usingStepMap;
    private boolean initialStepWritten;

    @Override
    public void init() {
        if (!(Options.outputDirectory.equals("."))) {
            Utilities.makeDirectory(Options.outputDirectory);
        }
        //Initialize writers
        typeDefinitions = new StringWriter();
        globalDeclarations = new StringWriter();
        globalDefinitions = new StringWriter();
        //Initialize init process
        initProcess = new Process("init");
        //Initialize the various hash maps
        functions = new HashMap<String, Function>();
        macros = new HashMap<String, String>();
        nonMacros = new HashMap<String, String>();
        processes = new HashMap<String, Process>();

        Logger.getLogger(StandardTranslator.class.getName()).log(Level.INFO,
                "Translation process ready. Output directory: {0}",
                Options.outputDirectory);
    }

    @Override
    public void finish() {
        String currentFileContents;
        try {
            //Write type definitions
            currentFileContents = FileUtils.readFileToString(new File(
                    System.getProperty("promnetpp.home")
                    + "/templates/private/types.h"));
            currentFileContents = MessageFormat.format(currentFileContents,
                    typeDefinitions.toString());
            FileUtils.writeStringToFile(new File(
                    Options.outputDirectory + "/types.h"), currentFileContents);
            //Write global definitions
            currentFileContents = FileUtils.readFileToString(new File(
                    System.getProperty("promnetpp.home")
                    + "/templates/private/global_definitions.h"));
            currentFileContents = MessageFormat.format(currentFileContents,
                    globalDefinitions.toString());
            FileUtils.writeStringToFile(new File(Options.outputDirectory
                    + "/global_definitions.h"), currentFileContents);
            //Write global declarations
            currentFileContents = FileUtils.readFileToString(new File(
                    System.getProperty("promnetpp.home")
                    + "/templates/private/globals.cc"));
            currentFileContents = MessageFormat.format(currentFileContents,
                    globalDeclarations.toString());
            FileUtils.writeStringToFile(new File(Options.outputDirectory
                    + "/globals.cc"), currentFileContents);
            //Copy files
            copyFiles();
        } catch (IOException ex) {
            Logger.getLogger(StandardTranslator.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    private void copyFiles() throws IOException {
        //omnetpp.ini
        FileUtils.copyFile(new File(System.getProperty("promnetpp.home")
                + "/templates/private/omnetpp.ini"),
                new File(Options.outputDirectory + "/omnetpp.ini"));
        //Process interface files
        FileUtils.copyFile(new File(System.getProperty("promnetpp.home")
                + "/templates/private/process_interface.h"),
                new File(Options.outputDirectory + "/process_interface.h"));
        FileUtils.copyFile(new File(System.getProperty("promnetpp.home")
                + "/templates/private/process_interface.cc"),
                new File(Options.outputDirectory + "/process_interface.cc"));
        //Template files
        if (template != null) {
            template.copyStaticFiles();
            template.writeDynamicFiles();
        }
        //Utility files
        FileUtils.copyFile(new File(System.getProperty("promnetpp.home")
                + "/templates/private/utilities.cc"),
                new File(Options.outputDirectory + "/utilities.h"));
        FileUtils.copyFile(new File(System.getProperty("promnetpp.home")
                + "/templates/private/utilities.cc"),
                new File(Options.outputDirectory + "/utilities.cc"));
    }

    private void indent() {
        currentIndentationLevel += SPACES_PER_TAB;
    }

    private void dedent() {
        currentIndentationLevel -= SPACES_PER_TAB;
    }

    @Override
    public void translate(AbstractSyntaxTree abstractSyntaxTree) {
        doFirstPassesOnAST(abstractSyntaxTree);
        //Make preparations
        makePreparationsBeforeFinalPass();
        //Final pass
        ASTNode rootNode = abstractSyntaxTree.getRootNode();
        ASTNode specification = (ASTNode) rootNode.jjtGetChild(0);
        translateSpecification(specification);
    }

    private void translateSpecification(ASTNode specification) {
        for (int i = 0; i < specification.jjtGetNumChildren(); ++i) {
            ASTNode element = (ASTNode) specification.jjtGetChild(i);
            String elementType = element.getNodeName();
            assert elementType.equals("Unit") : "Expected to find a Unit!"
                    + "Found " + elementType + " instead!";
            translateUnit(element);
        }
    }

    private void translateUnit(ASTNode translationUnit) {
        ASTNode currentChild;
        String currentChildType;
        for (int i = 0; i < translationUnit.jjtGetNumChildren(); ++i) {
            currentChild = (ASTNode) translationUnit.jjtGetChild(i);
            currentChildType = currentChild.getNodeName();
            //Comment
            if (currentChildType.equals("Comment")) {
                handleComment(currentChild);
            } else {
                if (!canSkipUnits) {
                    //Type definition (global)
                    if (currentChildType.equals("TypeDefinition")) {
                        translateTypeDefinition(currentChild);
                    } //Global declarations
                    else if (currentChildType.equals("GlobalDeclaration")) {
                        translateGlobalDeclaration(currentChild);
                    } //Function definitions
                    else if (currentChildType.equals("FunctionDefinition")) {
                        String functionName = currentChild.getValueAsString(
                                "functionName");
                        Function function = functions.get(functionName);
                        translateFunction(function);
                    } //Process definitions
                    else if (currentChildType.equals("ProcessDefinition")) {
                        translateProcessDefinition(currentChild);
                    }
                }
            }
        }
    }

    private void translateTypeDefinition(ASTNode typeDefinition) {
        String userTypeName = typeDefinition.getName();
        Logger.getLogger(StandardTranslator.class.getName()).log(Level.INFO,
                "Found type definition (typeName={0})", userTypeName);
        //OMNeT++ may not like certain names for type names, such as "message"
        if (userTypeName.equals("message")) {
            userTypeName = "message_t";
        }

        typeDefinitions.write("typedef struct {\n");
        indent();
        for (int i = 0; i < typeDefinition.jjtGetNumChildren(); ++i) {
            ASTNode currentChild = (ASTNode) typeDefinition.jjtGetChild(i);
            if (currentChild.getNodeName().equals("SimpleDeclaration")) {
                String typeName = currentChild.getTypeName();
                String name = currentChild.getName();
                boolean isArray = currentChild.getValueAsBoolean("isArray");
                if (!isArray) {
                    String message = MessageFormat.format("{0} {1};\n",
                            typeName, name);
                    Utilities.writeWithIndentation(typeDefinitions, message,
                            currentIndentationLevel);
                } else {
                    String message = MessageFormat.format("{0} {1}[{2}];\n",
                            typeName, name, currentChild.getValueAsString(
                            "arrayCapacity"));
                    Utilities.writeWithIndentation(typeDefinitions, message,
                            currentIndentationLevel);
                }
            }
        }
        dedent();
        typeDefinitions.write(MessageFormat.format("'}' {0};", userTypeName));
        typeDefinitions.write("\n\n");
    }

    private void handleComment(ASTNode comment) {
        String commentAsString = comment.getValueAsString();
        assert commentAsString.startsWith("/*")
                && commentAsString.endsWith("*/");
        //Remove comment delimiters "/*" and "*/"
        commentAsString = commentAsString.replace("/*", "").replace("*/", "");
        String trimmedComment = commentAsString.trim();
        boolean isAnnotatedComment = trimmedComment.startsWith("@");
        if (isAnnotatedComment) {
            Logger.getLogger(StandardTranslator.class.getName()).log(Level.INFO,
                    "Found an annotated comment: {0}", trimmedComment);
            handleAnnotatedComment(trimmedComment);
        }
    }

    private void handleDefineDirective(ASTNode defineDirective) {
        String defineDirectiveAsString = defineDirective.getValueAsString();
        globalDefinitions.write(defineDirectiveAsString);

        Logger.getLogger(StandardTranslator.class.getName()).log(Level.INFO,
                "Handling define directive {0}", defineDirectiveAsString);
        //Remove the #define at the beginning and parse the directive itself
        defineDirectiveAsString = defineDirectiveAsString.substring(
                "#define".length()).trim();
        boolean isMacro = defineDirectiveAsString.contains("(");
        if (!isMacro) {
            String directiveName = defineDirectiveAsString.substring(0,
                    defineDirectiveAsString.indexOf(" "));
            String directiveValue = defineDirectiveAsString.substring(
                    defineDirectiveAsString.indexOf(" ")).trim();
            nonMacros.put(directiveName, directiveValue);
            //Template-related non-macros
            if (template instanceof RoundBasedProtocolGeneric) {
                if (directiveName.equals("NUMBER_OF_PROCESSES")) {
                    int numberOfParticipants = Integer.parseInt(directiveValue);
                    Logger.getLogger(StandardTranslator.class.getName()).log(
                            Level.INFO, "RoundBasedProtocolGeneric will have "
                            + "{0} participants.", numberOfParticipants);
                    ((RoundBasedProtocolGeneric) template).
                            setNumberOfParticipants(numberOfParticipants);
                }
            }
        } else {
            String directiveName = defineDirectiveAsString.substring(0,
                    defineDirectiveAsString.indexOf("("));
            String directiveValue = defineDirectiveAsString.substring(
                    defineDirectiveAsString.indexOf("("));
            macros.put(directiveName, directiveValue);
        }
    }

    private void translateGlobalDeclaration(ASTNode globalDeclaration) {
        ASTNode simpleDeclaration = globalDeclaration.getFirstChild();
        String typeName = simpleDeclaration.getTypeName();
        String identifier = simpleDeclaration.getName();
        boolean isArray = simpleDeclaration.getValueAsBoolean("isArray");
        String arrayCapacity = null;
        if (isArray) {
            arrayCapacity = simpleDeclaration.getValueAsString("arrayCapacity");
        }
        if (typeName.equals("chan")) {
            ASTNode channelInitialization = globalDeclaration.getSecondChild();
            Channel channel = new Channel(identifier);
            channel.buildFromInitialization(channelInitialization);
            translateChannel(channel);
        } else {

            if (globalDeclaration.hasSingleChild()) {
                globalDeclarations.write(typeName + " " + identifier);
                if (isArray) {
                    globalDeclarations.write("[");
                    globalDeclarations.write(arrayCapacity);
                    globalDeclarations.write("]");
                } else if (typeName.equals("byte") || typeName.equals("short")
                        || typeName.equals("int")) {
                    globalDeclarations.write(" = 0");
                }
                globalDeclarations.write(";\n");
            }
        }
    }

    private void translateProcessDefinition(ASTNode processDefinition) {
        String processName = processDefinition.getValueAsString("processName");
        ASTNode instructionList = (ASTNode) processDefinition.jjtGetChild(0);
        for (int i = 0; i < instructionList.jjtGetNumChildren(); ++i) {
            ASTNode instruction = (ASTNode) instructionList.jjtGetChild(i);
            instruction = instruction.getFirstChild();
            //NonBlockInstruction
            if (instruction.getNodeName().equals("NonBlockInstruction")) {
                ASTNode nonBlockInstruction = instruction.getFirstChild();
                //SimpleDeclaration
                if (nonBlockInstruction.getNodeName().equals(
                        "SimpleDeclaration")) {
                    String variableName = nonBlockInstruction.getName();
                    String variableType = nonBlockInstruction.getTypeName();
                    Logger.getLogger(StandardTranslator.class.getName()).log(
                            Level.INFO, "Process {0} has a variable of type {1}"
                            + " named {2}.", new Object[]{processName,
                                variableName, variableType});
                }
            } //BlockInstruction
            else if (instruction.getNodeName().equals("BlockInstruction")) {
            }
        }
    }

    private void handleAnnotatedComment(String comment) {
        assert comment.startsWith("@");
        comment = comment.substring("@".length());
        String directiveName = comment.replaceFirst("\\(.*\\)", "");
        if (directiveName.equalsIgnoreCase("USES_TEMPLATE")) {
            String parametersAsString = comment.substring(comment.indexOf("("),
                    comment.lastIndexOf(")")).substring("(".length());
            String[] parameters = parametersAsString.split(",");
            for (String parameter : parameters) {
                String[] parameterUnits = parameter.split("=");
                String parameterName = parameterUnits[0];
                String parameterValue = parameterUnits[1];
                if (parameterName.equalsIgnoreCase("template_name")) {
                    String templateName = parameterValue.replaceAll("\"", "");
                    Logger.getLogger(StandardTranslator.class.getName()).log(
                            Level.INFO, "Using template {0}", templateName);
                    template = Template.getTemplate(templateName);
                }
            }
        } else if (directiveName.equalsIgnoreCase("BEGIN_TEMPLATE_BLOCK")) {
            String parametersAsString = comment.substring(comment.indexOf("("),
                    comment.lastIndexOf(")")).substring("(".length());
            String[] parameters = parametersAsString.split(",");
            for (String parameter : parameters) {
                String[] parameterUnits = parameter.split("=");
                String parameterName = parameterUnits[0];
                String parameterValue = parameterUnits[1];
                if (parameterName.equalsIgnoreCase("block_name")) {
                    String blockName = parameterValue.replaceAll("\"", "");
                    if (blockName.equalsIgnoreCase("generic_part")) {
                        template.setCurrentBlock("generic_part");
                        canSkipUnits = true;
                    }
                }
            }
        } else if (directiveName.equalsIgnoreCase("END_TEMPLATE_BLOCK")) {
            template.setCurrentBlock("main");
            canSkipUnits = false;
        }
    }

    private void doFirstPassesOnAST(AbstractSyntaxTree abstractSyntaxTree) {
        ASTNode rootNode = abstractSyntaxTree.getRootNode();
        ASTNode specification = (ASTNode) rootNode.jjtGetChild(0);
        ASTNode unit, unitChild;
        String unitType;
        //First pass
        for (int i = 0; i < specification.jjtGetNumChildren(); ++i) {
            unit = (ASTNode) specification.jjtGetChild(i);
            for (int j = 0; j < unit.jjtGetNumChildren(); ++j) {
                unitChild = (ASTNode) unit.jjtGetChild(j);
                unitType = unitChild.getNodeName();
                //Treat define directives
                if (unitType.equals("DefineDirective")) {
                    handleDefineDirective(unitChild);
                }
                //Add any function definitions to the function map
                if (unitType.equals("FunctionDefinition")) {
                    String functionName = unitChild.getValueAsString(
                            "functionName");
                    Function function = new Function(functionName);
                    ASTNode parameters = null, instructions;
                    //Functions with no parameters
                    if (unitChild.jjtGetNumChildren() == 1) {
                        instructions = (ASTNode) unitChild.jjtGetChild(0);
                    } //Functions with parameters
                    else {
                        parameters = (ASTNode) unitChild.jjtGetChild(0);
                        instructions = (ASTNode) unitChild.jjtGetChild(1);
                    }
                    function.setParameters(parameters);
                    function.setInstructions(instructions);
                    function.analyze();
                    functions.put(functionName, function);
                    Logger.getLogger(StandardTranslator.class.getName()).log(
                            Level.INFO, "Added a function named {0}",
                            functionName);
                }
                //Search inside process definitions (including init process)
                if (unitType.equals("ProcessDefinition")) {
                    String processName = unitChild.getValueAsString(
                            "processName");
                    Process process = new Process(processName);
                    processes.put(processName, process);
                    setFunctionCallers(processName, "process", unitChild);
                } else if (unitType.equals("InitProcessDefinition")) {
                    setFunctionCallers("init", "process", unitChild);
                }
            }
        }
        //Second pass
        for (int i = 0; i < specification.jjtGetNumChildren(); ++i) {
            unit = (ASTNode) specification.jjtGetChild(i);
            for (int j = 0; j < unit.jjtGetNumChildren(); ++j) {
                unitChild = (ASTNode) unit.jjtGetChild(j);
                unitType = unitChild.getNodeName();
                //Functions may call other functions too
                if (unitType.equals("FunctionDefinition")) {
                    String functionName = unitChild.getValueAsString(
                            "functionName");
                    setFunctionCallers(functionName, "function", unitChild);
                }
            }
        }
    }

    private void setFunctionCallers(String name, String type, ASTNode node) {
        List<String> calledFunctions = Utilities.searchForFunctionCalls(node);
        for (String functionName : calledFunctions) {
            //Ignore functions that are not user-defined
            if (functionName.equals("printf") || functionName.equals("select")
                    || functionName.equals("assert")
                    || functionName.equals("empty")
                    || functionName.equals("nempty")
                    || functionName.equals("full")
                    || functionName.equals("nfull")) {
                continue;
            }
            //Ignore macros
            if (macros.containsKey(functionName)) {
                continue;
            }
            Function function = functions.get(functionName);
            function.addCaller(name, type);
            function.normalize(functions);
        }
    }

    private void translateFunction(Function function) {
        currentFunction = function.getName();
        ASTNode instructions = function.getInstructions();
        assert instructions != null : "Function " + function.getName() + " has"
                + " no instructions!";
        if (template != null) {
            /*
             * If we're using a template, there's no need to translate functions
             * for the init process.
             */
            if (function.hasSingleCaller() && function.getFirstCaller()
                    .equalsIgnoreCase("init")) {
                return;
            }
            System.out.println("Translating " + function.getName());
            StringWriter writer = template.getSpecificFunctionWriter(
                    function.getName());
            indent();
            if (function.requiresStepMap()) {
                Utilities.writeWithIndentation(writer, MessageFormat.format(
                        "int step = step_map[\"{0}\"];\n", function.getName()),
                        currentIndentationLevel);
                usingStepMap = true;
            }
            for (int i = 0; i < instructions.jjtGetNumChildren(); ++i) {
                ASTNode instruction = (ASTNode) instructions.jjtGetChild(i);
                instruction = instruction.getFirstChild();
                translateInstruction(instruction, writer);
            }
            dedent();
        }
        initialStepWritten = false;
        usingStepMap = false;
    }

    private void translateChannel(Channel channel) {
        //Handle the header file first (.h)
        String headerFileLocation = Options.outputDirectory + "/"
                + channel.toCppHeaderFileName();
        FileWriter headerFileWriter;
        try {
            headerFileWriter = new FileWriter(headerFileLocation);
            String headerFileContents = FileUtils.readFileToString(new File(
                    "templates/private/channel.h"));
            headerFileContents = MessageFormat.format(headerFileContents, new Object[]{channel.toCppHeaderName(),
                        channel.toCppClassName()});
            headerFileWriter.write(headerFileContents);
            headerFileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(StandardTranslator.class.getName()).log(
                    Level.SEVERE, null, ex);
            System.err.println("Unable to write header file for a channel: "
                    + headerFileLocation);
            System.err.println(ex);
            System.exit(1);
        }
        //Now handle the source file (.cc)
        String sourceFileLocation = Options.outputDirectory + "/"
                + channel.toCppSourceFileName();
        FileWriter sourceFileWriter;
        try {
            sourceFileWriter = new FileWriter(sourceFileLocation);
            String sourceFileContents = FileUtils.readFileToString(new File(
                    "templates/private/channel.cc"));
            sourceFileContents = MessageFormat.format(sourceFileContents,
                    new Object[]{channel.toCppHeaderFileName(),
                        channel.toCppClassName()});
            sourceFileWriter.write(sourceFileContents);
            sourceFileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(StandardTranslator.class.getName()).log(
                    Level.SEVERE, null, ex);
            System.err.println("Unable to write source file for a channel: "
                    + sourceFileLocation);
            System.err.println(ex);
            System.exit(1);
        }
    }

    private void makePreparationsBeforeFinalPass() {
        for (Function function : functions.values()) {
            if (function.hasSingleCaller()) {
                String caller = function.getFirstCaller();
                String callerType = function.getFirstCallerType();
                if (callerType.equalsIgnoreCase("process")) {
                    if (caller.equalsIgnoreCase("init")) {
                        initProcess.addFunction(function.getName());
                    } else {
                        Process process = processes.get(caller);
                        process.addFunction(function.getName());
                    }
                }
            }
        }
    }

    private void translateInstruction(ASTNode instruction, StringWriter writer) {
        String code;
        String instructionType = instruction.getNodeName();
        System.out.println(instructionType);
        //Write initial step, if needed
        if (usingStepMap && currentStep == 0 && !initialStepWritten) {
            Utilities.writeWithIndentation(writer,
                    "if (step == 0) {\n", currentIndentationLevel);
            ++currentStep;
            indent();
        }
        //Perform the actual translation
        if (instructionType.equals("Assignment")) {
            ASTNode variable = instruction.getFirstChild();
            ASTNode expression = instruction.getSecondChild();
            String translatedAssignment = "{0} = {1};\n";
            String translatedVariable = variable.toCppVariableName();
            String translatedExpression = expression.toCppExpression();
            translatedAssignment = MessageFormat.format(translatedAssignment,
                    new Object[]{translatedVariable, translatedExpression});
            Utilities.writeWithIndentation(writer, translatedAssignment,
                    currentIndentationLevel);
        } else if (instructionType.equals("DStepBlock")) {
            ASTNode blockInstructions = instruction.getFirstChild();
            for (int i = 0; i < blockInstructions.jjtGetNumChildren(); ++i) {
                ASTNode blockInstruction = (ASTNode) blockInstructions.jjtGetChild(i);
                translateInstruction(blockInstruction.getFirstChild(), writer);
            }
        } else if (instructionType.equals("DoLoop")) {
            code = MessageFormat.format("if (step == {0}) '{'\n",
                    currentStep);
            Utilities.writeWithIndentation(writer, code,
                    currentIndentationLevel);
            indent();
            for (int i = 0; i < instruction.jjtGetNumChildren(); ++i) {
                ASTNode guard = (ASTNode) instruction.jjtGetChild(i);
                for (int j = 0; j < guard.jjtGetNumChildren(); ++j) {
                    ASTNode guardInstruction = (ASTNode) guard.jjtGetChild(j);
                    translateInstruction(guardInstruction.getFirstChild(),
                            writer);
                }
            }
            dedent();
            Utilities.writeWithIndentation(writer, "}\n",
                    currentIndentationLevel);
        } else if (instructionType.equals("If")) {
            for (int i = 0; i < instruction.jjtGetNumChildren(); ++i) {
                ASTNode guard = (ASTNode) instruction.jjtGetChild(i);
                //Might not be a condition at all; we must determine if the
                //statement is executable or not first
                ASTNode guardCondition = guard.getFirstChild().getFirstChild();
                if (!guardCondition.isAlwaysExecutable()) {
                    String condition = null;
                    String conditionType = guardCondition.getNodeName();
                    if (conditionType.equals("Expression")) {
                        condition = guardCondition.toCppExpression();
                    }
                    if (condition != null) {
                        code = MessageFormat.format("if ({0}) '{'\n", condition);
                        Utilities.writeWithIndentation(writer, code,
                                currentIndentationLevel);
                        indent();
                        for (int j = 1; j < guard.jjtGetNumChildren(); ++j) {
                            ASTNode guardInstruction = (ASTNode)
                                    guard.jjtGetChild(j);
                            translateInstruction(
                                    guardInstruction.getFirstChild(), writer);
                            if (!lastWrittenInstruction.isAlwaysExecutable()) {
                                break;
                            }
                        }
                        dedent();
                        Utilities.writeWithIndentation(writer, "} else {\n",
                                currentIndentationLevel);
                        indent();
                        Utilities.writeWithIndentation(writer, "step = 3;\n",
                                currentIndentationLevel);
                        dedent();
                        Utilities.writeWithIndentation(writer, "}\n",
                                currentIndentationLevel);
                    }
                }
            }
        } else if (instructionType.equals("Expression")) {
            Utilities.writeWithIndentation(writer, instruction.toCppExpression()
                    + ";\n", currentIndentationLevel);
            if (!instruction.isAlwaysExecutable()) {
                code = MessageFormat.format("save_location(\"{0}\", {1});\n",
                        new Object[]{currentFunction, currentStep + 1});
                Utilities.writeWithIndentation(writer, code,
                        currentIndentationLevel);
                ++currentStep;
            }
        }
        //Close initial step, if needed
        if (usingStepMap && currentStep == 1 && !initialStepWritten) {
            Utilities.writeWithIndentation(writer,
                    "++step;\n", currentIndentationLevel);
            dedent();
            Utilities.writeWithIndentation(writer,
                    "}\n", currentIndentationLevel);
            initialStepWritten = true;
        }
        //Save the last written instruction
        lastWrittenInstruction = instruction;
    }
}
