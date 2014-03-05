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

package com.sap.dirigible.ide.repository.ui.command;

import java.util.ArrayList;

import org.eclipse.rap.rwt.RWT;

public class Clipboard extends ArrayList<Object> {

	private static final long serialVersionUID = 6272208252465388075L;

	private static final String DIRIGIBLE_CLIPBOARD = "dirigible.clipboard"; //$NON-NLS-1$

	private String command;

	public static Clipboard getInstance() {
		Clipboard clipboard = (Clipboard) RWT.getRequest().getSession()
				.getAttribute(DIRIGIBLE_CLIPBOARD);
		if (clipboard == null) {
			clipboard = new Clipboard();
			RWT.getRequest().getSession()
					.setAttribute(DIRIGIBLE_CLIPBOARD, clipboard);
		}
		return clipboard;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
