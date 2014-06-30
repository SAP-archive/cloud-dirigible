package com.sap.dirigible.repository.ext.command;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.tools.ant.types.Commandline;

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
	
	public static String[] splitCommand(String commandLine) {
//		//\"[^\"]*\"|'[^']*'|[A-Za-z']
//		//"\\s+"
//		String[] result = commandLine.split("\"[^\"]*\"|'[^']*'|[A-Za-z']");
//		return result;
		
		
		return Commandline.translateCommandline(commandLine);
		
//		List<String> result = new ArrayList<String>();
//		
//		Scanner sc = new Scanner(commandLine);
//	    Pattern pattern = Pattern.compile(
//	        "\"[^\"]*\"" +
//	        "|'[^']*'" +
//	        "|[A-Za-z']+"
//	    );
//	    String token;
//	    while ((token = sc.findInLine(pattern)) != null) {
//	    	if (token != null
//	    			&& token.length() > 2) {
//	    		if ((token.charAt(0) == '\"')
//	    				&& (token.charAt(token.length()-1) == '\"')) {
//	    			result.add(token.substring(1, token.length()-1));
//	    		} else {
//	    			result.add(token);
//	    		}
//	    	} else { 
//	    		result.add(token);
//	    	}
//	    }
//	    return result.toArray(new String[]{});
	}

}
