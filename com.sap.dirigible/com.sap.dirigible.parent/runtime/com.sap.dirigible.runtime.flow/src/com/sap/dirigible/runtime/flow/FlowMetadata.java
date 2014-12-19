package com.sap.dirigible.runtime.flow;

import java.util.Properties;

import com.google.gson.Gson;

public class FlowMetadata {
	
	private String name;
	
	private String description;
	
	private Properties properties = new Properties();
	
	private FlowStep[] steps;

	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public FlowStep[] getSteps() {
		return steps;
	}

	public void setSteps(FlowStep[] steps) {
		this.steps = steps;
	}
	
	public static void main(String[] args) {
		FlowMetadata flowMetadata = new FlowMetadata();
		flowMetadata.setName("MyFlow1");
		flowMetadata.getProperties().setProperty("myKey1", "myValue1");
		flowMetadata.getProperties().setProperty("myKey2", "myValue2");
		FlowStep flowStep1 = new FlowStep();
		flowStep1.setId("1");
		flowStep1.setType("javascript");
		flowStep1.setModule("/project1/service1.js");
		FlowStep flowStep2 = new FlowStep();
		flowStep2.setId("2");
		flowStep2.setType("javascript");
		flowStep2.setModule("/project1/service2.js");
		flowMetadata.setSteps(new FlowStep[] {flowStep1, flowStep2});
		Gson gson = new Gson();
		String json = gson.toJson(flowMetadata);
		System.out.println(json);
	}

}
