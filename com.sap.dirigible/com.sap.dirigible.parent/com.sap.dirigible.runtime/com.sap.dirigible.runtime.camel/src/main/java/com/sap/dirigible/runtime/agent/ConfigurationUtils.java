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

package com.sap.dirigible.runtime.agent;

import java.util.UUID;

public class ConfigurationUtils {

	public static String DIRIGIBLE_CONSUMER_PREFIX = "dirigible_esb_"; //$NON-NLS-1$

	public static String generateConsumerName(String serverInstanceId) {
		return DIRIGIBLE_CONSUMER_PREFIX + serverInstanceId + "_" //$NON-NLS-1$
				+ UUID.randomUUID();
	}

	public static String formatName(String s) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);

			if (Character.isJavaIdentifierPart(c)) {
				sb.append(c);
			} else {
				sb.append('_');
			}
		}

		return new String(sb);
	}

}
