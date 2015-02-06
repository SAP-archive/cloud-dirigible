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

package com.sap.dirigible.ide.workspace.wizard.project.create;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.sap.dirigible.ide.common.image.ImageUtils;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.repository.logging.Logger;

public class NewProjectWizardTemplateTypePage extends WizardPage {

	private static final long serialVersionUID = -2312000617075348922L;
	private static final String ERROR_ON_LOADING_TEMPLATES_FOR_GENERATION = Messages.NewProjectWizardTemplateTypePage_ERROR_ON_LOADING_TEMPLATES_FOR_GENERATION;
	private static final String SELECT_TEMPLATE_TYPE_FORM_THE_LIST = Messages.NewProjectWizardTemplateTypePage_SELECT_TEMPLATE_TYPE_FORM_THE_LIST;
	private static final String PAGE_NAME = Messages.NewProjectWizardTemplateTypePage_PAGE_NAME;
	private static final String PAGE_TITLE = Messages.NewProjectWizardTemplateTypePage_PAGE_TITLE;
	private static final String PAGE_DESCRIPTION = Messages.NewProjectWizardTemplateTypePage_PAGE_DESCRIPTION;

	private static final Logger logger = Logger
			.getLogger(NewProjectWizardTemplateTypePage.class);

	private final NewProjectWizardModel model;

	private TableViewer typeViewer;
	
	private Label labelPreview;

	public NewProjectWizardTemplateTypePage(NewProjectWizardModel model) {
		super(PAGE_NAME);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
		this.model = model;
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout());
		createTypeField(composite);
		
		createPreviewLabel(composite);

		checkPageStatus();
	}
	
	private static final Image previewImage = ImageUtils.createImage(getIconURL("preview.png"));
	
	public static URL getIconURL(String iconName) {
		URL url = ImageUtils.getIconURL("com.sap.dirigible.ide.workspace.wizard.project", "/icons/", iconName);
		return url;
	}

	private void createPreviewLabel(final Composite composite) {
		labelPreview = new Label(composite,SWT.NONE);
		labelPreview.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		labelPreview.setBounds(0,0,450,300);
		labelPreview.setBackground(new org.eclipse.swt.graphics.Color(null, 0,0,0));
		labelPreview.setImage(previewImage);
	}

	private void createTypeField(Composite parent) {
		final Label label = new Label(parent, SWT.NONE);
		label.setText(Messages.NewProjectWizardTemplateTypePage_AVAILABLE_TEMPLATES);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));

		typeViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER
				| SWT.V_SCROLL);
		typeViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		typeViewer.setContentProvider(new ArrayContentProvider());
		typeViewer.setLabelProvider(new ProjectTemplateTypePageLabelProvider());
		ProjectTemplateType[] templateTypes = createTemplateTypes();
		typeViewer.setInput(templateTypes);
		typeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if (selection.getFirstElement() == null
						|| !(selection.getFirstElement() instanceof ProjectTemplateType)) {
					setErrorMessage(SELECT_TEMPLATE_TYPE_FORM_THE_LIST);
					setPageComplete(false);
					labelPreview.setImage(previewImage);
					labelPreview.pack(true);
					return;
				} else {
					setErrorMessage(null);
					ProjectTemplateType templateType = ((ProjectTemplateType) selection
							.getFirstElement());
					getModel().setTemplate(templateType);
					labelPreview.setImage(templateType.getImagePreview());
					labelPreview.pack(true);
				}
				checkPageStatus();
			}
		});

	}

	private ProjectTemplateType[] createTemplateTypes() {
		try {
			ProjectTemplateType[] templateTypes = prepareTemplateTypes();
			return templateTypes;
		} catch (IOException e) {
			logger.error(ERROR_ON_LOADING_TEMPLATES_FOR_GENERATION, e);
		}
		return null;
	}

	protected ProjectTemplateType[] prepareTemplateTypes() throws IOException {
		List<ProjectTemplateType> projectTemplateTypesList = new ArrayList<ProjectTemplateType>();
		IRepository repository = RepositoryFacade.getInstance().getRepository();
		ICollection projectTemplatesRoot = repository
				.getCollection(IRepositoryPaths.DB_DIRIGIBLE_TEMPLATES_PROJECTS);
		if (!projectTemplatesRoot.exists()) {
			model.setUseTemplate(false);
			return new ProjectTemplateType[] {};
		}
		for (ICollection projectCollection : projectTemplatesRoot
				.getCollections()) {
			try {
				projectTemplateTypesList.add(ProjectTemplateType
						.createTemplateType(repository,
								projectCollection.getPath()));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return projectTemplateTypesList.toArray(new ProjectTemplateType[] {});
	}

	private void checkPageStatus() {
		if (getModel().isUseTemplate()) {
			if (getModel().getTemplateLocation() == null
					|| "".equals(getModel().getTemplateLocation())) { //$NON-NLS-1$
				setPageComplete(false);
				return;
			}
		}
		setPageComplete(true);
	}

	public NewProjectWizardModel getModel() {
		return model;
	}

}
