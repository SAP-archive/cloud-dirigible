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

package com.sap.rap.ui.shared.editor;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

public class SourceFileEditorInput extends FileEditorInput {
	
	private boolean readOnly = false;
	
	private boolean breakpointsEnabled = false;
	
	private int row = 0;

	public SourceFileEditorInput(IFile file) {
		super(file);
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isBreakpointsEnabled() {
		return breakpointsEnabled;
	}

	public void setBreakpointsEnabled(boolean breakpointsEnabled) {
		this.breakpointsEnabled = breakpointsEnabled;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public IFile getFile() {
		// TODO Auto-generated method stub
		return super.getFile();
	}
	
	@Override
	public URI getURI() {
		// TODO Auto-generated method stub
		return super.getURI();
	}
	
	
}
