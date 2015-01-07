package com.sap.dirigible.runtime.flow;

public class Flow extends FlowBase {
	
	private FlowStep[] steps;

	public FlowStep[] getSteps() {
		return steps;
	}

	public void setSteps(FlowStep[] steps) {
		this.steps = steps;
	}
	
//	public static void main(String[] args) {
//		// Flow
//		Flow flow1 = new Flow();
//		flow1.setName("MyFlow1");
//		flow1.getProperties().setProperty("myKey1", "myValue1");
//		flow1.getProperties().setProperty("myKey2", "myValue2");
//		// FlowStep
//		FlowStep flowStep1 = new FlowStep();
//		flowStep1.setName("1");
//		flowStep1.setType("javascript");
//		flowStep1.setModule("/project1/service1.js");
//		flowStep1.getProperties().setProperty("step1Key1", "step1Value1");
//		flowStep1.getProperties().setProperty("step1Key2", "step1Value2");
//		// second FlowStep
//		FlowStep flowStep2 = new FlowStep();
//		flowStep2.setName("2");
//		flowStep2.setType("javascript");
//		flowStep2.setModule("/project1/service2.js");
//		// conditional FlowStep
//		FlowStep flowStep3 = new FlowStep();
//		flowStep3.setName("3");
//		flowStep3.setType("condition");
//		// first FlowCase
//		FlowCase flowCase1 = new FlowCase();
//		flowCase1.setKey("myKey1");
//		flowCase1.setValue("myValue1");
//		// internal case Flow
//		Flow flow2 = new Flow();
//		// internal case flow FlowStep
//		FlowStep flowStep4 = new FlowStep();
//		flowStep4.setName("4");
//		flowStep4.setType("javascript");
//		flowStep4.setModule("/project1/service4.js");
//		flow2.setSteps(new FlowStep[] {flowStep4});
//		flowCase1.setFlow(flow2);
//		// second FlowCase
//		FlowCase flowCase2 = new FlowCase();
//		flowCase2.setKey("myKey1");
//		flowCase2.setValue("modified");
//		// internal case Flow
//		Flow flow3 = new Flow();
//		// internal case flow FlowStep
//		FlowStep flowStep5 = new FlowStep();
//		flowStep5.setName("5");
//		flowStep5.setType("javascript");
//		flowStep5.setModule("/project1/service5.js");
//		flow3.setSteps(new FlowStep[] {flowStep5});
//		flowCase2.setFlow(flow3);
//		flowStep3.setCases(new FlowCase[] {flowCase1, flowCase2});
//		
//		flow1.setSteps(new FlowStep[] {flowStep1, flowStep2, flowStep3});
//		Gson gson = new Gson();
//		String json = gson.toJson(flow1);
//		System.out.println(json);
//	}

}
