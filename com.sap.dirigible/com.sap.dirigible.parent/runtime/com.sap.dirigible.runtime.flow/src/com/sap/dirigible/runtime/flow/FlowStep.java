package com.sap.dirigible.runtime.flow;


public class FlowStep extends FlowBase {
	
	public static final String FLOW_STEP_TYPE_JAVASCRIPT = "javascript";
	public static final String FLOW_STEP_TYPE_JAVA = "java";
	public static final String FLOW_STEP_TYPE_COMMAND = "command";
	public static final String FLOW_STEP_TYPE_CONDITION = "condition";
//	public static final String FLOW_STEP_TYPE_TIMER = "timer";
//	public static final String FLOW_STEP_TYPE_QUARTZ = "quartz";
//	public static final String FLOW_STEP_TYPE_ENDPOINT = "endpoint";
	
	private String type;
	
	private String module;
	
	private FlowCase[] cases;

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

	public FlowCase[] getCases() {
		return cases;
	}
	
	public void setCases(FlowCase[] cases) {
		this.cases = cases;
	}
}
