/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.research.main;

import com.googlecode.promnetpp.research.data.GeneralData;
import com.googlecode.promnetpp.research.data.Protocol;
import com.googlecode.promnetpp.research.data.PANOptions;
import com.googlecode.promnetpp.research.other.Utilities;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Miguel Martins
 */
public class CoverageMain {
    private static String sourceCode;
    private static String fileName;
    private static File CSVFile = new File("results.csv");
    private static int currentSeed;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(GeneralData.seeds.length + " seeds available.");
            prepareCSVFile();
            for (String _fileName : GeneralData.fileNames) {
                fileName = _fileName;
                sourceCode = FileUtils.readFileToString(new File(fileName));
                System.out.println("Running seeds for file " + fileName);
                for (int seed : GeneralData.seeds) {
                    doSeedRun(seed);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CoverageMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CoverageMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cleanup();
        }
    }

    private static void doSeedRun(int seed) throws IOException, InterruptedException {
        currentSeed = seed;
        System.out.println("Running with seed " + seed);
        String pattern = "int random = <INSERT_SEED_HERE>;";
        int start = sourceCode.indexOf(pattern);
        int end = start + pattern.length();
        String line = sourceCode.substring(start, end);
        line = line.replace("<INSERT_SEED_HERE>", Integer.toString(seed));
        String sourceCodeWithSeed = sourceCode.replace(pattern, line);
        FileUtils.writeStringToFile(new File("temp.pml"), sourceCodeWithSeed);
        //Run Spin first
        List<String> spinCommand = new ArrayList<String>();
        spinCommand.add(GeneralData.spinHome + "/spin");
        spinCommand.add("-a");
        spinCommand.add("temp.pml");
        ProcessBuilder processBuilder = new ProcessBuilder(spinCommand);
        Process process = processBuilder.start();
        process.waitFor();
        //Compile PAN next
        List<String> compilePANCommand = new ArrayList<String>();
        compilePANCommand.add("gcc");
        compilePANCommand.add("-o");
        compilePANCommand.add("pan");
        compilePANCommand.add("pan.c");
        processBuilder = new ProcessBuilder(compilePANCommand);
        process = processBuilder.start();
        process.waitFor();
        //Finally, run PAN
        List<String> runPANCommand = new ArrayList<String>();
        runPANCommand.add("./pan");
        String runtimeOptions = PANOptions.getRuntimeOptionsFor(fileName);
        if (!runtimeOptions.isEmpty()) {
            runPANCommand.add(runtimeOptions);
        }
        processBuilder = new ProcessBuilder(runPANCommand);
        process = processBuilder.start();
        process.waitFor();
        String PANOutput = Utilities.getStreamAsString(process.getInputStream());
        if (PANOutputContainsErrors(PANOutput)) {
            throw new RuntimeException("PAN reported errors.");
        }
        processPANOutput(PANOutput);
    }

    private static void processPANOutput(String output) throws IOException {
        //Have a truncated version of the output
        int start = output.indexOf("unreached in");
        if (start == -1) {
            FileUtils.writeStringToFile(CSVFile, fileName + ","
                    + "\"none\","
                    + "\"none\","
                    + "\"none\","
                    + "\"none\"\n", true);
        } else {
            String partialOutput = output.substring(start);
            String endPattern = "states)";
            int end = partialOutput.lastIndexOf(endPattern) + endPattern.length();
            partialOutput = partialOutput.substring(0, end);
            //Search for unreached lines
            Pattern pattern = Pattern.compile("temp[.]pml[:]\\d+?,");
            Matcher matcher = pattern.matcher(partialOutput);
            String lineNumbers = "";
            while (matcher.find()) {
                String line = matcher.group();
                line = line.substring(line.indexOf(":") + 1);
                line = line.replace(",", "");
                int lineNumber = Integer.parseInt(line);
                lineNumbers += lineNumber + ",";
            }
            //Remove trailing comma
            lineNumbers = lineNumbers.substring(0, lineNumbers.length() - 1);
            String relevantLineNumbers = Protocol.excludeIrrelevantLineNumbers(
                    fileName, lineNumbers);
            FileUtils.writeStringToFile(CSVFile, fileName + ","
                    + Integer.toString(currentSeed) + ","
                    + "\"" + partialOutput.replace("\"", "\"\"") + "\","
                    + "\"" + lineNumbers + "\","
                    + "\"" + relevantLineNumbers + "\"\n"
                    , true);
        }

    }

    private static void prepareCSVFile() throws IOException {
        if (CSVFile.exists()) {
            CSVFile.delete();
        }
        CSVFile.createNewFile();
        FileUtils.writeStringToFile(CSVFile, "\"File name\","
                + "\"Seed\","
                + "\"Output\","
                + "\"Unreachable lines\","
                + "\"Unreachable lines (excluding irrelevant)\"\n");
    }

    private static void cleanup() {
        List<String> filesToDelete = new ArrayList<String>();
        filesToDelete.add("pan.b");
        filesToDelete.add("pan.c");
        filesToDelete.add("pan.exe"); //Windows
        filesToDelete.add("pan"); //Linux/OS X
        filesToDelete.add("pan.h");
        filesToDelete.add("pan.m");
        filesToDelete.add("pan.p");
        filesToDelete.add("pan.t");
        filesToDelete.add("temp.pml");
        filesToDelete.add("temp.pml.trail");
        for (String file : filesToDelete) {
            new File(file).deleteOnExit();
        }
    }

    private static boolean PANOutputContainsErrors(String output) {
        String errorReport = output.substring(output.indexOf("errors: "));
        errorReport = errorReport.substring(0, errorReport.indexOf("\n") - 1);
        String errorCountAsString = errorReport.substring(errorReport.indexOf(": "));
        errorCountAsString = errorCountAsString.substring(": ".length());
        int errorCount = Integer.parseInt(errorCountAsString);
        if (errorCount > 0) {
            System.err.println(output);
            return true;
        }
        return false;
    }
}
