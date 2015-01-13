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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.sap.dirigible.ide.workspace.ui.viewer.ReservedFolderFilter;
import com.sap.dirigible.ide.workspace.ui.viewer.WorkspaceContainerFilter;
import com.sap.dirigible.ide.workspace.ui.viewer.WorkspaceViewer;

public abstract class TemplateTargetLocationPage extends WizardPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7791118502101439238L;

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final String INPUT_THE_FILE_NAME = Messages.TemplateTargetLocationPage_INPUT_THE_FILE_NAME;

	private static final String SELECT_THE_LOCATION_OF_THE_GENERATED_PAGE = Messages.TemplateTargetLocationPage_SELECT_THE_LOCATION_OF_THE_GENERATED_PAGE;

	protected abstract GenerationModel getModel();

	private WorkspaceViewer projectViewer;

	private Text fileNameText;

	protected TemplateTargetLocationPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout(1, false));

		createProjectViewerField(composite);
		createFileNameField(composite);
		checkPageStatus();
	}

	private void setPreselectedElement() {
		projectViewer.getViewer().setSelection(getPreselectedElement(), true);
	}

	private void createProjectViewerField(Composite parent) {
		projectViewer = new WorkspaceViewer(parent, SWT.BORDER);
		projectViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		projectViewer.getViewer().addFilter(getFilter());
		projectViewer.getViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						String targetLocation = null;
						IStructuredSelection selection = (IStructuredSelection) projectViewer
								.getSelectionProvider().getSelection();
						if (selection.getFirstElement() == null
								|| !(selection.getFirstElement() instanceof IContainer)) {
							setErrorMessage(SELECT_THE_LOCATION_OF_THE_GENERATED_PAGE);
						} else {
							setErrorMessage(null);
							IContainer container = ((IContainer) selection
									.getFirstElement());
							targetLocation = container.getFullPath().toString();
							getModel().setTargetLocation(targetLocation);
						}
						checkPageStatus();
					}
				});
		projectViewer.getViewer().expandToLevel(2);
	}

	protected ViewerFilter getFilter() {
		if (getArtifactContainerName() == null) {
			return new WorkspaceContainerFilter();
		} else {
			return new ReservedFolderFilter(getArtifactContainerName());
		}

	}

	/**
	 * Override if you wish to filter where the file can be created. Method
	 * should return one of the main types of artifacts: javascript service,
	 * integration service etc. Null if no the artifact can be created
	 * everywhere.
	 * 
	 * @return Method should return one of the main types of artifacts:
	 *         javascript service, integration service etc.
	 */
	protected String getArtifactContainerName() {
		return null;
	}

	protected Object getApropriateFolderForAction(String folderName) {
		Object[] expandedElements = projectViewer.getViewer()
				.getExpandedElements();
		if (expandedElements == null || expandedElements.length == 0) {
			return null;
		}
		ITreeContentProvider contentProvider = (ITreeContentProvider) projectViewer
				.getViewer().getContentProvider();
		IResource sourceResource = getModel().getSourceResource();
		if (sourceResource != null) {
			String[] segments = sourceResource.getLocation().segments();
			for (String segment : segments) {
				if (segment.equals(folderName)) {
					return sourceResource;
				}
			}
		}

		// backup variant if selection is on different artifact type or on
		// project level
		try {
			if (sourceResource != null) {
				IProject project = sourceResource.getProject();
				Object[] children = contentProvider.getChildren(project);
				for (Object object : children) {
					IResource r = (IResource) object;
					if ((r instanceof IFolder) && r.getName().equals(folderName)) {
						return r;
					}
				}
			} else {
				return null;
			}
		} catch (NullPointerException e) {
			// project is not selected, return null
		}
		return null;
	}

	private void createFileNameField(Composite parent) {
		final Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setText(Messages.TemplateTargetLocationPage_FILE_NAME);

		fileNameText = new Text(parent, SWT.BORDER);
		fileNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		fileNameText.addModifyListener(new ModifyListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 72329751007839679L;

			@Override
			public void modifyText(ModifyEvent event) {
				if (fileNameText.getText() == null
						|| EMPTY_STRING.equals(fileNameText.getText())) {
					setErrorMessage(INPUT_THE_FILE_NAME);
				} else {
					setErrorMessage(null);
					getModel().setFileName(fileNameText.getText());
				}
				checkPageStatus();
			}
		});

	}

	protected abstract void checkPageStatus();

	protected abstract String getDefaultFileName();

	@Override
	public void setVisible(boolean visible) {
		setPreselectedElement();
		if (fileNameText.getText() == null
				|| EMPTY_STRING.equals(fileNameText.getText())) {
			fileNameText.setText(getDefaultFileName());
		} else {
			if (isForcedFileName()
//					&& getModel().getFileName() == null
					) {
				fileNameText.setText(getDefaultFileName());
			}
		}

		super.setVisible(visible);
		preselectFileNameText();

	}

	private void preselectFileNameText() {
		fileNameText.setFocus();
		String defaultName = getDefaultFileName();
		if (defaultName != null && defaultName.length() > 0) {
			int lastIndexOf = defaultName.indexOf("."); //$NON-NLS-1$
			if (lastIndexOf == -1) {
				lastIndexOf = defaultName.length();
			}
			fileNameText.setSelection(0, lastIndexOf);
		}

	}

	/**
	 * Method returns preselected element that is the correct type of artifact
	 * parent location.
	 * 
	 * @return default selection
	 */
	protected StructuredSelection getPreselectedElement() {
		if (getArtifactContainerName() != null) {
			Object byName = getApropriateFolderForAction(getArtifactContainerName());
			if (byName != null) {
				return new StructuredSelection(byName);
			}
		}
		return new StructuredSelection();

	}

	protected boolean isForcedFileName() {
		return false;
	}

}
