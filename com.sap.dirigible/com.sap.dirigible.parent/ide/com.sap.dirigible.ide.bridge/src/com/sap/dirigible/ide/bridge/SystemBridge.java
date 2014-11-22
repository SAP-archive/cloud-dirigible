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

package com.sap.dirigible.ide.bridge;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemBridge extends HttpServlet {
	
	private static final long serialVersionUID = -8043662807856187626L;
	private static final Logger logger = LoggerFactory.getLogger(SystemBridge.class); 
	
	public static Properties ENV_PROPERTIES = new Properties();
	
	@Override
	public void init() throws ServletException {
		
		ENV_PROPERTIES.putAll(System.getProperties());
		
		for (Object property : ENV_PROPERTIES.keySet()) {
			logger.info("SYSTEM_" + property + ": " + ENV_PROPERTIES.getProperty(property.toString()));
		}
		
		super.init();
	}	

}
