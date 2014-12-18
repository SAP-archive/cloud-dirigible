package com.sap.dirigible.runtime.scripts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.dirigible.runtime.scripting.XMLUtils;

public class XMLUtilsTest {

	@Test
	public void testFromJson() {
		XMLUtils xmlUtils = new XMLUtils();
		String xml = xmlUtils.fromJson(
				"{"
				+ "  'name':'JSON',"
				+ "  'integer':1,"
				+ "  'double':2.0,"
				+ "  'boolean':true,"
				+ "  'nested':"
				+ "  {"
				+ "    'id':42"
				+ "  },"
				+ "  'array':[1,2,3]"
				+ "}");
		assertEquals("<nested><id>42</id></nested><integer>1</integer><name>JSON</name><boolean>true</boolean><double>2.0</double><array>1</array><array>2</array><array>3</array>", xml);
	}

	@Test
	public void testToJson() {
		XMLUtils xmlUtils = new XMLUtils();
		String json = xmlUtils.toJson(
				"  <nested>"
				+ "  <id>42</id>"
				+ "</nested>"
				+ "<integer>1</integer>"
				+ "<name>JSON</name>"
				+ "<boolean>true</boolean>"
				+ "<double>2.0</double>"
				+ "<array>1</array>"
				+ "<array>2</array>"
				+ "<array>3</array>");
		assertEquals("{\"nested\":{\"id\":42},\"integer\":1,\"name\":\"JSON\",\"boolean\":true,\"double\":2,\"array\":[1,2,3]}", json);
	}

}
