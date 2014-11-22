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

package com.sap.dirigible.ide.debug.model;

import java.beans.PropertyChangeListener;

import org.eclipse.ui.IEditorPart;

public interface IDebugController extends PropertyChangeListener {
	
	public void refresh(DebugModel debugModel);

	public void stepInto(DebugModel debugModel);

	public void stepOver(DebugModel debugModel);

	public void continueExecution(DebugModel debugModel);

	public void skipAllBreakpoints(DebugModel debugModel);

	public IEditorPart openEditor(String path, int row);

	public void setBreakpoint(DebugModel debugModel, String path, int row);

	public void clearBreakpoint(DebugModel debugModel, String path, int row);

	public void clearAllBreakpoints(DebugModel debugModel);

	public void clearAllBreakpoints(DebugModel debugModel, String path);

}
