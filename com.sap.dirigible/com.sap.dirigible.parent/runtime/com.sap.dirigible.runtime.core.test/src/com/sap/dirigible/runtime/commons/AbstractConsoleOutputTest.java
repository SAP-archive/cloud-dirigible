package com.sap.dirigible.runtime.commons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractConsoleOutputTest {

	private PrintStream backupOut;
	private PrintStream backupErr;

	private ByteArrayOutputStream baos;
	private PrintStream printStream;

	@Before
	public void setUp() throws Exception {
		backupOut = System.out;
		backupErr = System.err;
		
		baos = new ByteArrayOutputStream();
		printStream = new PrintStream(baos);
		
		System.setOut(printStream);
		System.setErr(printStream);
	}

	@After
	public void tearDown() throws Exception {
		if (baos != null) {
			baos.close();
		}
		if(printStream != null){
			printStream.close();
		}
		System.setOut(backupOut);
		System.setErr(backupErr);
	}

	protected String getOutput() throws IOException {
		String output = null;
		if(baos != null){
			output = baos.toString();
			baos.reset();
		}
		return output;
	}
}
