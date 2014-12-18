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
	public static final File SOURCЕ_HELLO_WORLD = new File("src/test/resources/src/hello_world_project/HelloWorld.java");
	public static final File SOURCE_HELLO_WORLD_UPDATED = new File("src/test/resources/src/hello_world_project/HelloWorld_Updated.java");
	public static final String MODULE_HELLO_WORLD = "/hello_world_project/HelloWorld.java";
	public static final String RESOURCE_PATH_HELLO_WORLD = REPOSITORY_PUBLIC_DEPLOY_PATH + MODULE_HELLO_WORLD;
	
	//------------------ calculator_project  resources ------------------//
	public static final File SOURCЕ_CALCULATOR = new File("src/test/resources/src/calculator_project/Calculator.java");
	public static final File SOURCE_CALCULATOR_UPDATED = new File("src/test/resources/src/calculator_project/Calculator_Updated.java");
	public static final String MODULE_CALCULATOR = "/calculator_project/Calculator.java";
	public static final String PATH_CALCULATOR_RESOURCE = REPOSITORY_PUBLIC_DEPLOY_PATH + MODULE_CALCULATOR;

	public static final File SOURCЕ_UTILS = new File("src/test/resources/src/calculator_project/Utils.java");
	public static final String MODULE_UTILS = "/calculator_project/Utils.java";
	public static final String RESOURCE_PATH_UTILS = REPOSITORY_PUBLIC_DEPLOY_PATH + MODULE_UTILS;
	
	//------------------ two_services_project  resources ------------------//
	public static final File SOURCЕ_SERVICE1 = new File("src/test/resources/src/project_with_two_services/Service1.java");
	public static final File SOURCЕ_SERVICE2 = new File("src/test/resources/src/project_with_two_services/Service2.java");
	public static final File SOURCЕ_SERVICE1_UPDATED = new File("src/test/resources/src/project_with_two_services/Service1_Updated.java");
	public static final File SOURCЕ_SERVICE2_UPDATED = new File("src/test/resources/src/project_with_two_services/Service2_Updated.java");
	public static final String MODULE_SERVICE1 = "/project_with_two_services/Service1.java";
	public static final String MODULE_SERVICE2 = "/project_with_two_services/Service2.java";
	public static final String RESOURCE_PATH_SERVICE1 = REPOSITORY_PUBLIC_DEPLOY_PATH + MODULE_SERVICE1;
	public static final String RESOURCE_PATH_SERVICE2 = REPOSITORY_PUBLIC_DEPLOY_PATH + MODULE_SERVICE2;
	
	//------------------ project_update resources ------------------//
	public static final File SOURCЕ_SERVICE = new File("src/test/resources/src/project_update/Service.java");
	public static final File SOURCЕ_SERVICE_UPDATED1 = new File("src/test/resources/src/project_update/Service_Updated1.java");
	public static final File SOURCЕ_SERVICE_UPDATED2 = new File("src/test/resources/src/project_update/Service_Updated2.java");
	public static final File SOURCЕ_SERVICE_UPDATED3 = new File("src/test/resources/src/project_update/Service_Updated3.java");
	public static final String MODULE_SERVICE = "/project_update/Service.java";
	public static final String RESOURCE_PATH_SERVICE = REPOSITORY_PUBLIC_DEPLOY_PATH + MODULE_SERVICE;
}
