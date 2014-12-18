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

package com.sap.dirigible.ide.db.viewer.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.sap.dirigible.ide.db.viewer.views.IDbConnectionFactory;
import com.sap.dirigible.ide.db.viewer.views.TableDefinition;

public class DbEditorInput implements IEditorInput {

	private static final String DATABASE_METADATA_FOR = Messages.DbEditorInput_DATABASE_METADATA_FOR;
	private TableDefinition tDefinition;
	private IDbConnectionFactory iDbConnectionFactory;

	public DbEditorInput(TableDefinition tDefinition,
			IDbConnectionFactory iDbConnectionFactory) {
		this.tDefinition = tDefinition;
		this.iDbConnectionFactory = iDbConnectionFactory;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return tDefinition.getTableName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return DATABASE_METADATA_FOR + tDefinition.getTableName();
	}

	public String getTableName() {
		return tDefinition.getTableName();
	}

	public TableDefinition getTableDefinition() {
		return tDefinition;
	}

	public IDbConnectionFactory getDbConnectionFactory() {
		return iDbConnectionFactory;
	}

	@Override
	public boolean equals(Object obj) {
		if (DbEditorInput.class.isInstance(obj)) {
			DbEditorInput nInput = (DbEditorInput) obj;
			if (nInput.getTableDefinition().equals(getTableDefinition())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getTableDefinition().hashCode();
	}

}