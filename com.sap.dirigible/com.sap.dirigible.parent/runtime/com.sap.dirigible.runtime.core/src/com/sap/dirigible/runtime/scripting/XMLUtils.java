package com.sap.dirigible.runtime.scripting;

import org.json.JSONObject;
import org.json.XML;

public class XMLUtils {
	
	public String fromJson(String jsonString) {
		JSONObject json = new JSONObject(jsonString);
		String xml = XML.toString(json);
		return xml;
	}
	
	public String toJson(String xmlString) {
		JSONObject json  = XML.toJSONObject(xmlString);
		String result = json.toString();
		return result;
	}

}
