package com.sap.dirigible.repository.ext.command;

import org.apache.commons.io.input.BoundedInputStream;

public class Piper implements java.lang.Runnable {

    private static final String BROKEN_PIPE = "Broken pipe";

	private static final long MAX_COMMAND_OUTPUT_LENGTH = 65536;

	private java.io.InputStream input;

    private java.io.OutputStream output;

    public Piper(java.io.InputStream input, java.io.OutputStream output) {
        this.input = new BoundedInputStream(input, MAX_COMMAND_OUTPUT_LENGTH);
        this.output = output;
    }

    public void run() {
        try {
            byte[] b = new byte[512];
            int read = 1;
            int sum = 0;
            while (read > -1) {
                read = input.read(b, 0, b.length);
                sum += read;
                if (read > -1) {
                    // Write bytes to output
                    output.write(b, 0, read);
                }
            }
            if (sum >= MAX_COMMAND_OUTPUT_LENGTH - 1) {
            	output.write("\n...".getBytes());
            }
        } catch (Exception e) {
            // Something happened while reading or writing streams; pipe is broken
            throw new RuntimeException(BROKEN_PIPE, e);
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
            try {
                output.close();
            } catch (Exception e) {
            }
        }
    }

}