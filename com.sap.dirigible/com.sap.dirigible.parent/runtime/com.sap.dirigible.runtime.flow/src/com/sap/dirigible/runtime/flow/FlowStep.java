package com.sap.dirigible.runtime.flow;

public class FlowStep {
	
	public static final String FLOW_STEP_TYPE_JAVASCRIPT = "javascript";
	public static final String FLOW_STEP_TYPE_JAVA = "java";
	public static final String FLOW_STEP_TYPE_COMMAND = "command";
//	public static final String FLOW_STEP_TYPE_TIMER = "timer";
//	public static final String FLOW_STEP_TYPE_QUARTZ = "quartz";
//	public static final String FLOW_STEP_TYPE_ENDPOINT = "endpoint";
	
	private String id;
	
	private String type;
	
	private String module;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	
	
}
