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

package com.sap.dirigible.ide.template.ui.common;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.wizard.WizardPage;

public class TemplateTypeDescriptor {
	
	private String text;

	private String location;

	private String image;
	
	private String category;

	private Set<String> parameters = new HashSet<String>();
	
	private Set<WizardPage> pages = new HashSet<WizardPage>();

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Set<String> getParameters() {
		return parameters;
	}

	public void setParameters(Set<String> parameters) {
		this.parameters = parameters;
	}

	public Set<WizardPage> getPages() {
		return pages;
	}

	public void setPages(Set<WizardPage> pages) {
		this.pages = pages;
	}
	

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

}
