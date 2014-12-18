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

package com.sap.dirigible.ide.editor.js;

import java.util.Arrays;

public enum EditorMode {

	JS("javascript", "js", "jslib", "xsjs", "xsjslib"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	SQL("sql", "sql"), //$NON-NLS-1$ //$NON-NLS-2$
	JSON("json", "json", "odata", "ws", "table", "view", "entity", "menu", "access", "extensionpoint", "extension", "command"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
	XML("xml", "xml", "xsd", "wsdl", "xsl", "xslt", "routes"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
	HTML("html", "html", "css"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	TXT("text", "txt"), //$NON-NLS-1$ //$NON-NLS-2$
	RUBY("ruby", "rb"), //$NON-NLS-1$ //$NON-NLS-2$
	JAVA("java", "java"), //$NON-NLS-1$ //$NON-NLS-2$
	GROOVY("groovy", "groovy", "gvy", "gy", "gsh");
	
	private static final EditorMode DEFAULT_MODE = TXT;
	private String name;
	private String[] extensions;

	private EditorMode(String name, String... extensions) {
		this.name = name;
		this.extensions = extensions;
		Arrays.sort(this.extensions);
	}

	public String getName() {
		return name;
	}

	public static EditorMode getByExtension(String extension) {
		for (EditorMode mode : EditorMode.values()) {
			if (Arrays.binarySearch(mode.extensions, extension) >= 0) {
				return mode;
			}
		}

		return DEFAULT_MODE;
	}

}
