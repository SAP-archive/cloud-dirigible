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

package com.sap.dirigible.ide.template.ui.db.wizard;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class DataStructureTemplateStructurePageLabelProvider extends
		ColumnLabelProvider implements ITableLabelProvider {

	private static final long serialVersionUID = 4279129633962596895L;

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof ColumnDefinition) {
			switch (columnIndex) {
			case 0: // Name
				return ((ColumnDefinition) element).getName();
			case 1: // Type
				return ((ColumnDefinition) element).getType();
			case 2: // Length
				return new Integer(((ColumnDefinition) element).getLength())
						.toString();
			case 3: // NN?
				return new Boolean(((ColumnDefinition) element).isNotNull())
						.toString();
			case 4: // PK?
				return new Boolean(((ColumnDefinition) element).isPrimaryKey())
						.toString();
			case 5: // Default
				return ((ColumnDefinition) element).getDefaultValue();
			default:
				return null;
			}
		}
		return null;
	}

}
