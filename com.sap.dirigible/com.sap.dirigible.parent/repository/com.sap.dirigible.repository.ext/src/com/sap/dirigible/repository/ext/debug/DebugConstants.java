/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.repository.ext.debug;

public class DebugConstants {
	private static final String UNDERSCORE = "_";
	public static final String VIEW = "VIEW";
	public static final String DEBUG = "DEBUG";

	public static final String VIEW_REGISTER = VIEW + UNDERSCORE + "REGISTER";
	public static final String VIEW_FINISH = VIEW + UNDERSCORE + "FINISH";
	public static final String VIEW_SESSIONS = VIEW + UNDERSCORE + "SESSIONS";
	public static final String VIEW_ON_LINE_CHANGE = VIEW + UNDERSCORE + "ON_LINE_CHANGE";
	public static final String VIEW_VARIABLE_VALUES = VIEW + UNDERSCORE + "VARIABLE_VALUES";
	public static final String VIEW_BREAKPOINT_METADATA = VIEW + UNDERSCORE + "BREAKPOINT_METADATA";

	public static final String DEBUG_REFRESH = DEBUG + UNDERSCORE + "REFRESH";
	public static final String DEBUG_STEP_INTO = DEBUG + UNDERSCORE + "STEP_INTO";
	public static final String DEBUG_STEP_OVER = DEBUG + UNDERSCORE + "STEP_OVER";
	public static final String DEBUG_CONTINUE = DEBUG + UNDERSCORE + "CONTINUE";;
	public static final String DEBUG_SKIP_ALL_BREAKPOINTS = DEBUG + UNDERSCORE
			+ "SKIP_ALL_BREAKPOINTS";
	public static final String DEBUG_SET_BREAKPOINT = DEBUG + UNDERSCORE + "SET_BREAKPOINT";
	public static final String DEBUG_CLEAR_BREAKPOINT = DEBUG + UNDERSCORE + "CLEAR_BREAKPOINT";
	public static final String DEBUG_CLEAR_ALL_BREAKPOINTS = DEBUG + UNDERSCORE
			+ "CLEAR_ALL_BREAKPOINTS";
	public static final String DEBUG_CLEAR_ALL_BREAKPOINTS_FOR_FILE = DEBUG + UNDERSCORE
			+ "CLEAR_ALL_BREAKPOINTS_FOR_FILE";


}
