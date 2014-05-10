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

package com.sap.dirigible.ide.workspace.wizard.project.sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.common.image.ImageUtils;
import com.sap.dirigible.ide.jgit.connector.JGitConnector;
import com.sap.dirigible.ide.jgit.utils.GitFileUtils;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.workspace.ui.shared.IValidationStatus;
import com.sap.dirigible.ide.workspace.wizard.project.create.ProjectTemplateType;

public class SampleProjectWizardGitTemplatePage extends WizardPage {

	private static final long serialVersionUID = 1L;

	private static final String PAGE_NAME = Messages.SampleProjectWizardGitTemplatePage_PAGE_NAME;
	private static final String PAGE_TITLE = Messages.SampleProjectWizardGitTemplatePage_PAGE_TITLE;
	private static final String PAGE_DESCRIPTION = Messages.SampleProjectWizardGitTemplatePage_PAGE_DESCRIPTION;
	private static final String ERROR_ON_LOADING_GIT_TEMPLATES_FOR_GENERATION = Messages.SampleProjectWizardGitTemplatePage_ERROR_ON_LOADING_GIT_TEMPLATES_FOR_GENERATION;
	private static final String HELP_DIRECTORY = "HelpDirectory"; //$NON-NLS-1$
	public static final String TEMP_DIRECTORY_PREFIX = "com.sap.dirigible.jgit."; //$NON-NLS-1$
	protected static final String SELECT_TEMPLATE_TYPE_FORM_THE_LIST = Messages.SampleProjectWizardGitTemplatePage_SELECT_TEMPLATE_TYPE_FORM_THE_LIST;
	private static final String GIT_TEMPLATE_DIRECTORY = "com.sap.dirigible.ide.workspace.wizard.project.sample"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(SampleProjectWizardGitTemplatePage.class);

	//    private static final String PROXY_PROPERTIES_FILE_LOCATION = "proxy.properties"; //$NON-NLS-1$
	//	private static final String DEFAULT_PROXY_VALUE = "false"; //$NON-NLS-1$
	//	private static final String PROXY = "proxy"; //$NON-NLS-1$
	//	public static final String HTTP_PROXY_HOST = "http.proxyHost"; //$NON-NLS-1$
	//	public static final String HTTP_PROXY_PORT = "http.proxyPort"; //$NON-NLS-1$
	//	public static final String HTTPS_PROXY_HOST = "https.proxyHost"; //$NON-NLS-1$
	//	public static final String HTTPS_PROXY_PORT = "https.proxyPort"; //$NON-NLS-1$
	//	public static final String HTTP_NON_PROXY_HOSTS = "http.nonProxyHosts"; //$NON-NLS-1$

	private final SampleProjectWizardModel model;

	private TreeViewer typeViewer;
	private Label labelPreview;

	public SampleProjectWizardGitTemplatePage(SampleProjectWizardModel model) {
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

	private static final Image previewImage = ImageUtils.createImage(getIconURL("preview.png")); //$NON-NLS-1$

	public static URL getIconURL(String iconName) {
		URL url = ImageUtils.getIconURL("com.sap.dirigible.ide.workspace.wizard.project", //$NON-NLS-1$
				"/icons/", iconName); //$NON-NLS-1$
		return url;
	}

	private void createPreviewLabel(final Composite composite) {
		labelPreview = new Label(composite, SWT.NONE);
		labelPreview.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		labelPreview.setBounds(0, 0, 450, 300);
		labelPreview.setBackground(new org.eclipse.swt.graphics.Color(null, 0, 0, 0));
		labelPreview.setImage(previewImage);
	}

	private void createTypeField(Composite parent) {
		final Label label = new Label(parent, SWT.NONE);
		label.setText(Messages.SampleProjectWizardGitTemplatePage_AVAILABLE_GIT_TEMPLATES);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));

		typeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		typeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		typeViewer.setContentProvider(new SamplesContentProvider());
		typeViewer.setLabelProvider(new SamplesLabelProvider());
		typeViewer.setSorter(new ViewerSorter());
		typeViewer.setInput(createGitTemplateTypes());

		typeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.getFirstElement() == null
						|| !(selection.getFirstElement() instanceof SamplesProject)) {
					setPageComplete(false);
					setErrorMessage(SELECT_TEMPLATE_TYPE_FORM_THE_LIST);
					labelPreview.setImage(previewImage);
					labelPreview.pack(true);
				} else {
					setErrorMessage(null);
					SamplesProject gitTemplate = ((SamplesProject) selection.getFirstElement());
					getModel().setTemplate(gitTemplate.getTemplate());
					checkPageStatus();
					labelPreview.setImage(gitTemplate.getTemplate().getImagePreview());
					labelPreview.pack(true);
				}
			}
		});
	}

	private SamplesCategory createGitTemplateTypes() {
		try {
			Map<String, List<SamplesProject>> categories = new HashMap<String, List<SamplesProject>>();
			for (ProjectTemplateType sampleProject : prepareGitTemplateTypes()) {
				String category = sampleProject.getCategory();
				if (categories.containsKey(category)) {
					categories.get(category).add(new SamplesProject(sampleProject));
				} else {
					List<SamplesProject> sampleProjectsList = new ArrayList<SamplesProject>();
					sampleProjectsList.add(new SamplesProject(sampleProject));
					categories.put(category, sampleProjectsList);
				}
			}

			SamplesCategory root = new SamplesCategory(""); //$NON-NLS-1$
			for (Entry<String, List<SamplesProject>> entry : categories.entrySet()) {
				root.addCategory(new SamplesCategory(entry.getKey(), entry.getValue()));
			}
			return root;
		} catch (IOException e) {
			logger.error(ERROR_ON_LOADING_GIT_TEMPLATES_FOR_GENERATION, e);
		}
		return null;
	}

	@SuppressWarnings("null")
	protected ProjectTemplateType[] prepareGitTemplateTypes() throws IOException {
		File gitDirectory = null;
		boolean isCloned = false;

		File file = GitFileUtils.createTempDirectory(HELP_DIRECTORY);
		File tempDirectory = file.getParentFile();
		GitFileUtils.deleteDirectory(file);

		for (File temp : tempDirectory.listFiles()) {
			if (temp.isDirectory() && temp.getName().startsWith(GIT_TEMPLATE_DIRECTORY)) {
				isCloned = true;
				gitDirectory = temp;
				break;
			}
		}

		if (isCloned) {
			doPull(gitDirectory.getCanonicalPath());
		} else {
			gitDirectory = GitFileUtils.createTempDirectory(GIT_TEMPLATE_DIRECTORY);
			doClone(gitDirectory);
		}

		if (gitDirectory.listFiles().length <= 0) {
			model.setUseTemplate(false);
			return new ProjectTemplateType[] {};
		}

		List<ProjectTemplateType> projectTemplateTypesList = new ArrayList<ProjectTemplateType>();

		for (File projectTemplate : gitDirectory.listFiles()) {
			if (!projectTemplate.getName().equalsIgnoreCase(".git") //$NON-NLS-1$
					&& projectTemplate.isDirectory()) {

				projectTemplateTypesList.add(ProjectTemplateType
						.createGitTemplateType(projectTemplate));
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
			} else {
				revalidateModel();
				return;
			}
		}
		setPageComplete(false);
	}

	public SampleProjectWizardModel getModel() {
		return this.model;
	}

	private void revalidateModel() {
		String projectName = getModel().getTemplate().getName();
		getModel().setProjectName(projectName);
		IValidationStatus status = getModel().validate();
		if (status.hasErrors()) {
			this.setErrorMessage(status.getMessage());
			this.setWarningMessage(null);
			this.setCanFinish(false);
		} else if (status.hasWarnings()) {
			this.setErrorMessage(null);
			this.setWarningMessage(status.getMessage());
			this.setCanFinish(true);
		} else {
			this.setErrorMessage(null);
			this.setWarningMessage(null);
			this.setCanFinish(true);
		}
	}

	public void setWarningMessage(String message) {
		setMessage(message, WARNING);
	}

	public void setCanFinish(boolean value) {
		setPageComplete(value);
	}

	private static void doPull(String gitDirectoryPath) throws IOException {
		Repository repository = null;
		try {
			repository = JGitConnector.getRepository(gitDirectoryPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JGitConnector jgit = new JGitConnector(repository);
		try {
			jgit.pull();
			jgit.add("."); //$NON-NLS-1$
			jgit.hardReset();
		} catch (WrongRepositoryStateException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (DetachedHeadException e) {
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (CanceledException e) {
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	private static void doClone(File gitDirectory) {
		try {
			JGitConnector.cloneRepository(CommonParameters.GIT_REPOSITORY_URL, gitDirectory);
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

}
