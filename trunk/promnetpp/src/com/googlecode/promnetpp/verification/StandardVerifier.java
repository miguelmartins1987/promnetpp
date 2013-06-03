/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.verification;

import com.googlecode.promnetpp.options.Options;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Martins
 */
public class StandardVerifier extends Verifier {

    private String pathToInputFile;
    private boolean containsErrors;

    public StandardVerifier(String pathToInputFile) {
        this.pathToInputFile = pathToInputFile;
        containsErrors = false;
    }

    @Override
    public boolean containsErrors() {
        return containsErrors;
    }

    @Override
    public void doVerification() {
        String pathToSpin = Options.spinHome + "/spin";
        //spin -a model.pml
        ProcessBuilder processBuilder = new ProcessBuilder(pathToSpin, "-a",
                pathToInputFile);
        try {
            System.out.println("Performing verification...");
            System.out.println(processBuilder.command());
            Process spinProcess = processBuilder.start();
            spinProcess.waitFor();
            String output = getOutputStreamAsString(spinProcess);
            /*
             * We'll assume that, if spin -a produces output, then errors
             * occurred (for instance, syntax errors)
             */
            if (output.length() > 0) {
                System.err.println(output);
                containsErrors = true;
            } else {
                System.out.println("No errors found so far. Compile and run"
                        + " PAN...");
                compileAndRunPAN();
            }
        } catch (IOException ex) {
            Logger.getLogger(StandardVerifier.class.getName()).log(Level.SEVERE,
                    null, ex);
            containsErrors = true;
        } catch (InterruptedException ex) {
            Logger.getLogger(StandardVerifier.class.getName()).log(Level.SEVERE,
                    null, ex);
            containsErrors = true;
        }
    }
    
    
    @Override
    public void finish() {
        List<String> filesToDelete = new ArrayList<String>();
        filesToDelete.add("pan.b");
        filesToDelete.add("pan.c");
        filesToDelete.add("pan.h");
        filesToDelete.add("pan.m");
        filesToDelete.add("pan.p");
        filesToDelete.add("pan.t");
        //Under Windows, PAN is likely named pan.exe
        filesToDelete.add("pan.exe");
        //Under Linux, it should be just pan
        filesToDelete.add("pan");
        for (String file : filesToDelete) {
            deleteFileIfExists(file);
        }
        //Notify the user that we're done
        System.out.println("Verification complete. Proceeding to the"
                + " translation process...");
        System.out.println();
    }

    private String getOutputStreamAsString(Process process) throws IOException {
        byte [] outputBytes = new byte[process.getInputStream().available()];
        process.getInputStream().read(outputBytes);
        return new String(outputBytes);
    }

    private String getErrorStreamAsString(Process process) throws IOException {
        byte [] errorBytes = new byte[process.getErrorStream().available()];
        process.getErrorStream().read(errorBytes);
        return new String(errorBytes);
    }

    private void compileAndRunPAN() throws IOException, InterruptedException {
        if (Options.panCompiler.equalsIgnoreCase("gcc")) {
            ProcessBuilder processBuilder = new ProcessBuilder("gcc", "pan.c",
                    "-o", "pan");
            System.out.println(processBuilder.command());
            //Compile
            Process gcc = processBuilder.start();
            int exitValue = gcc.waitFor();
            if (exitValue > 0) {
                System.err.println(getOutputStreamAsString(gcc));
                containsErrors = true;
                return;
            }
            System.out.println("PAN has been successfully compiled. Running PAN"
                    + " now...");
            //Run
            processBuilder = new ProcessBuilder("pan");
            System.out.println(processBuilder.command());
            Process PAN = processBuilder.start();
            exitValue = PAN.waitFor();
            System.out.println(getOutputStreamAsString(PAN));
            if (exitValue > 0) {
                System.err.println("PAN reported one or more errors.");
                containsErrors = true;
            }
        }
    }

    private void deleteFileIfExists(String fileName) {
        File file = new File(fileName);
        file.delete();
    }
}
