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

package com.sap.dirigible.ide.ui.rap.entry;

import java.io.IOException;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.client.service.ExitConfirmation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.common.io.ProxyUtils;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.repository.api.ICommonConstants;

public class DirigibleWorkbench implements EntryPoint {

	private static final String ARE_YOU_SURE_YOU_WANT_TO_QUIT = Messages.DirigibleWorkbench_ARE_YOU_SURE_YOU_WANT_TO_QUIT;
	
	private static final Logger logger = Logger.getLogger(DirigibleWorkbench.class); 

	public int createUI() {
		final Display display = PlatformUI.createDisplay();
		// Disabled because we do not want to show confirmation dialog.
		ExitConfirmation service = RWT.getClient().getService(
				ExitConfirmation.class);
		service.setMessage(ARE_YOU_SURE_YOU_WANT_TO_QUIT
				+ ICommonConstants.DIRIGIBLE_PRODUCT_NAME); // TODO: I18N
		
		try {
			ProxyUtils.setProxySettings();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return PlatformUI.createAndRunWorkbench(display,
				new DirigibleWorkbenchAdvisor());
	}

}
