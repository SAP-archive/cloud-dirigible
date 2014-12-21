package com.sap.dirigible.runtime.flow;

public class FlowCase extends FlowBase {
	
	private String key;
	
	private String value;

	private Flow flow;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Flow getFlow() {
		return flow;
	}
	
	public void setFlow(Flow flow) {
		this.flow = flow;
	}
	
}
