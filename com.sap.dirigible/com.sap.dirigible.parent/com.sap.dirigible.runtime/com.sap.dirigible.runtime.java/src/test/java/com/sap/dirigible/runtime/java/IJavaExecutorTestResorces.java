package com.sap.dirigible.runtime.java;

import java.io.File;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IRepositoryPaths;

public interface IJavaExecutorTestResorces {
	
	public static final String LIB_DIRECTORY = "src/test/resources/lib";
	public static final String USER = "guest";

	public static final String REPOSITORY_PUBLIC_DEPLOY_PATH = IRepositoryPaths.DB_DIRIGIBLE_REGISTRY_PUBLIC + ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	public static final String REPOSITORY_SANDBOX_DEPLOY_PATH = IRepositoryPaths.DB_DIRIGIBLE_SANDBOX + USER + ICommonConstants.SEPARATOR + ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;
	
	//------------------ hello_world_project  resources ------------------//
	public static final File HELLO_WORLD_SOURCЕ = new File("src/test/resources/src/hello_world_project/HelloWorld.java");
	public static final File HELLO_WORLD_UPDATED_SOURCE = new File("src/test/resources/src/hello_world_project/HelloWorld_Updated.java");
	public static final String HELLO_WORLD_MODULE = "/hello_world_project/HelloWorld.java";
	public static final String HELLO_WORLD_RESOURCE_PATH = REPOSITORY_PUBLIC_DEPLOY_PATH + HELLO_WORLD_MODULE;
	
	//------------------ calculator_project  resources ------------------//
	public static final File CALCULATOR_SOURCЕ = new File("src/test/resources/src/calculator_project/Calculator.java");
	public static final File CALCULATOR_UPDATED_SOURCE = new File("src/test/resources/src/calculator_project/Calculator_Updated.java");
	public static final String CALCULATOR_MODULE = "/calculator_project/Calculator.java";
	public static final String CALCULATOR_RESOURCE_PATH = REPOSITORY_PUBLIC_DEPLOY_PATH + CALCULATOR_MODULE;

	public static final File UTILS_SOURCЕ = new File("src/test/resources/src/calculator_project/Utils.java");
	public static final String UTILS_MODULE = "/calculator_project/Utils.java";
	public static final String UTILS_RESOURCE_PATH = REPOSITORY_PUBLIC_DEPLOY_PATH + UTILS_MODULE;
	
	//------------------ two_endpoints_project  resources ------------------//
	public static final File ENDPOINT1_SOURCЕ = new File("src/test/resources/src/two_endpoints_project/Endpoint1.java");
	public static final File ENDPOINT2_SOURCЕ = new File("src/test/resources/src/two_endpoints_project/Endpoint2.java");
	public static final String ENDPOINT1_MODULE = "/two_endpoints_project/Endpoint1.java";
	public static final String ENDPOINT2_MODULE = "/two_endpoints_project/Endpoint2.java";
	public static final String ENDPOINT1_RESOURCE_PATH = REPOSITORY_PUBLIC_DEPLOY_PATH + ENDPOINT1_MODULE;
	public static final String ENDPOINT2_RESOURCE_PATH = REPOSITORY_PUBLIC_DEPLOY_PATH + ENDPOINT2_MODULE;
}
