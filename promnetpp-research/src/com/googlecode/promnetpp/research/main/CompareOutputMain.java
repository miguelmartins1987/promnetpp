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
import com.googlecode.promnetpp.research.other.Utilities;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Miguel Martins
 */
public class CompareOutputMain {

    private static String fileName;
    private static String sourceCode;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        for (String _fileName : GeneralData.fileNames) {
            fileName = _fileName;
            sourceCode = FileUtils.readFileToString(new File(fileName));
            System.out.println("Running seeds for file " + fileName);
            for (int seed : GeneralData.seeds) {
                doSeedRun(seed);
            }
        }
    }

    private static void doSeedRun(int seed) throws IOException, InterruptedException {
        System.out.println("Running with seed " + seed);
        String pattern = "int random = <INSERT_SEED_HERE>;";
        int start = sourceCode.indexOf(pattern);
        int end = start + pattern.length();
        String line = sourceCode.substring(start, end);
        line = line.replace("<INSERT_SEED_HERE>", Integer.toString(seed));
        String sourceCodeWithSeed = sourceCode.replace(pattern, line);
        File tempFile = new File("temp.pml");
        FileUtils.writeStringToFile(tempFile, sourceCodeWithSeed);
        //Create a "project" folder
        String fileNameWithoutExtension = fileName.split("[.]")[0];
        File folder = new File("test1-" + fileNameWithoutExtension);
        if (folder.exists()) {
            FileUtils.deleteDirectory(folder);
        }
        folder.mkdir();
        //Copy temp.pml to our new folder
        FileUtils.copyFileToDirectory(tempFile, folder);
        //Simulate the model using Spin
        List<String> spinCommand = new ArrayList<String>();
        spinCommand.add(GeneralData.spinHome + "/spin");
        spinCommand.add("-u1000000");
        spinCommand.add("temp.pml");
        ProcessBuilder processBuilder = new ProcessBuilder(spinCommand);
        processBuilder.directory(folder);
        processBuilder.redirectOutput(new File(folder, "spin-" + seed + ".txt"));
        Process process = processBuilder.start();
        process.waitFor();
        //Translate via PROMNeT++
        List<String> PROMNeTppCommand = new ArrayList<String>();
        PROMNeTppCommand.add("java");
        PROMNeTppCommand.add("-enableassertions");
        PROMNeTppCommand.add("-jar");
        PROMNeTppCommand.add("\"" + GeneralData.getJARFilePath() + "\"");
        PROMNeTppCommand.add("temp.pml");
        processBuilder = new ProcessBuilder(PROMNeTppCommand);
        processBuilder.directory(folder);
        processBuilder.environment().put("PROMNETPP_HOME",
                GeneralData.PROMNeTppHome);
        process = processBuilder.start();
        process.waitFor();
        //Run opp_makemake
        FileUtils.copyFileToDirectory(new File("opp_makemake.bat"), folder);
        List<String> makemakeCommand = new ArrayList<String>();
        if (Utilities.operatingSystemType.equals("windows")) {
            makemakeCommand.add("cmd");
            makemakeCommand.add("/c");
            makemakeCommand.add("opp_makemake.bat");
        } else {
            throw new RuntimeException("Support for Linux/OS X not implemented"
                    + " here yet.");
        }
        processBuilder = new ProcessBuilder(makemakeCommand);
        processBuilder.directory(folder);
        process = processBuilder.start();
        process.waitFor();
        //Run make
        FileUtils.copyFileToDirectory(new File("opp_make.bat"), folder);
        List<String> makeCommand = new ArrayList<String>();
        if (Utilities.operatingSystemType.equals("windows")) {
            makeCommand.add("cmd");
            makeCommand.add("/c");
            makeCommand.add("opp_make.bat");
        } else {
            throw new RuntimeException("Support for Linux/OS X not implemented"
                    + " here yet.");
        }
        processBuilder = new ProcessBuilder(makeCommand);
        processBuilder.directory(folder);
        process = processBuilder.start();
        process.waitFor();
        System.out.println(Utilities.getStreamAsString(
                process.getInputStream()));
        System.exit(1);
    }
}
