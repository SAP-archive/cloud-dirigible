package com.sap.dirigible.runtime.job;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JobParser {
	
	static final String NODE_NAME = "name";
	static final String NODE_DESCRIPTION = "description";
	static final String NODE_EXPRESSION = "expression";
	static final String NODE_TYPE = "type";
	static final String NODE_MODULE = "module";

	public static JsonObject parseJob(String jobDefinition) throws IOException {
		// {
		// "name":"MyJob",
		// "description":"MyJob Description",
		// "expression":"0/20 * * * * ?",
		// "type":"javascript",
		// "module":"/${projectName}/service1.js"
		// }

		JsonParser parser = new JsonParser();
		JsonObject jobDefinitionObject = (JsonObject) parser.parse(jobDefinition);

		// TODO validate the parsed content has the right structure

		return jobDefinitionObject;
	}
}
