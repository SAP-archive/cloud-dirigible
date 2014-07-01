package com.sap.dirigible.repository.ext.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ProcessUtils {
	
	public static ProcessBuilder createProcess(String[] args) throws IOException {
		
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		return processBuilder;
	}
	
	public static void addEnvironmentVariables(ProcessBuilder processBuilder, Map<String, String> forAdding) {
		if (processBuilder != null
				&& forAdding != null) {
			Map<String, String> env = processBuilder.environment();
			env.putAll(forAdding);
		}
	}
	
	public static void removeEnvironmentVariables(ProcessBuilder processBuilder, List<String> forRemoving) {
		if (processBuilder != null
				&& forRemoving != null) {
			Map<String, String> env = processBuilder.environment();
			for (Iterator<String> iterator = forRemoving.iterator(); iterator.hasNext();) {
				String remove = iterator.next();
				env.remove(remove);
			}
		}
	}
		
	public static Process startProcess(String[] args,
			ProcessBuilder processBuilder) throws IOException {
		
		Process process = processBuilder.start();
		return process;
	}
	
		/**
		 * LIFTED FROM APACHE ANT CODE BASE!
		 * 
	     * Crack a command line.
	     *
	     * @param toProcess
	     *            the command line to process
	     * @return the command line broken into strings. An empty or null toProcess
	     *         parameter results in a zero sized array
	     */
	    public static String[] translateCommandline(final String toProcess) {
	        if (toProcess == null || toProcess.length() == 0) {
	            // no command? no string
	            return new String[0];
	        }
	
	        // parse with a simple finite state machine
	
	        final int normal = 0;
	        final int inQuote = 1;
	        final int inDoubleQuote = 2;
	        int state = normal;
	        final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
	        final ArrayList<String> list = new ArrayList<String>();
	        StringBuilder current = new StringBuilder();
	        boolean lastTokenHasBeenQuoted = false;
	
	        while (tok.hasMoreTokens()) {
	            final String nextTok = tok.nextToken();
	            switch (state) {
	            case inQuote:
	                if ("\'".equals(nextTok)) {
	                    lastTokenHasBeenQuoted = true;
	                    state = normal;
	                } else {
	                    current.append(nextTok);
	                }
	                break;
	            case inDoubleQuote:
	                if ("\"".equals(nextTok)) {
	                    lastTokenHasBeenQuoted = true;
	                    state = normal;
	                } else {
	                    current.append(nextTok);
	                }
	                break;
	            default:
	                if ("\'".equals(nextTok)) {
	                    state = inQuote;
	                } else if ("\"".equals(nextTok)) {
	                    state = inDoubleQuote;
	                } else if (" ".equals(nextTok)) {
	                    if (lastTokenHasBeenQuoted || current.length() != 0) {
	                        list.add(current.toString());
	                        current = new StringBuilder();
	                    }
	                } else {
	                    current.append(nextTok);
	                }
	                lastTokenHasBeenQuoted = false;
	                break;
	            }
	        }
	
	        if (lastTokenHasBeenQuoted || current.length() != 0) {
	            list.add(current.toString());
	        }
	
	        if (state == inQuote || state == inDoubleQuote) {
	            throw new IllegalArgumentException("Unbalanced quotes in "
	                    + toProcess);
	        }
	
	        final String[] args = new String[list.size()];
	        return list.toArray(args);
	}

}
