package com.googlecode.promnetpp.eclipseplugin;

import java.io.IOException;
import java.io.InputStream;

public class ProcessThread extends Thread {

	private ProcessBuilder processBuilder;
	private Process process;
	private int returnCode;

	public int getReturnCode() {
		return returnCode;
	}
	
	public void setProcessBuilder(ProcessBuilder processBuilder) {
		this.processBuilder = processBuilder;		
	}

	@Override
	public void run() {
		try {
			process = processBuilder.start();
			returnCode = process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getStandardOutput() {
		return getInputStreamAsString(process.getInputStream());
	}

	public String getStandardError() {
		return getInputStreamAsString(process.getErrorStream());
	}

	private String getInputStreamAsString(InputStream stream) {
		try {
			int numberOfBytes = Math.min(1024, stream.available());
			byte [] bytes = new byte[numberOfBytes];
			stream.read(bytes);
			return new String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Unable to get the contents of a stream.");
	}

	public boolean isRunningProcess() {
		return (process != null);
	}
}
