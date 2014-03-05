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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.workspace.ui.commands.OpenHandler;

public abstract class TemplateWizard extends Wizard {

	private static final String GENERATION_FAILED = Messages.TemplateWizard_GENERATION_FAILED;

	private static final Logger logger = Logger.getLogger(TemplateWizard.class);

	protected abstract GenerationModel getModel();

	@Override
	public boolean canFinish() {
		final IWizardPage currentPage = getContainer().getCurrentPage();
		if (currentPage == null || !currentPage.isPageComplete()) {
			return false;
		}
		if (getModel().validate().hasErrors()) {
			return false;
		}

		return followingPagesAreComplete(currentPage);
	}

	private boolean followingPagesAreComplete(IWizardPage page) {
		IWizardPage currentPage = page.getNextPage();
		while (currentPage != null) {
			if (!currentPage.isPageComplete()) {
				return false;
			}
			currentPage = currentPage.getNextPage();
		}
		return true;
	}

	@Override
	public boolean performFinish() {
		try {
			TemplateGenerator generator = getTemplateGenerator();
			generator.generate();
			openFiles(generator.getGeneratedFiles());
			return true;
		} catch (Exception ex) {
			logger.error(GENERATION_FAILED, ex);
			MessageDialog.openError(null, GENERATION_FAILED, ex.getMessage());
			return false;
		}
	}

	private void openFiles(List<IFile> generatedFiles) {
		if (generatedFiles.size() > 0) {
			if (generatedFiles.size() > 1) {
				if (openEditorForFileWithExtension() != null) {
					for (IFile iFile : generatedFiles) {
						if (iFile.getName().endsWith(
								openEditorForFileWithExtension())) {
							OpenHandler.open(iFile, 0);
						}
					}
				}
			} else {
				OpenHandler.open(generatedFiles.get(0), 0);
			}
		}

	}

	protected String openEditorForFileWithExtension() {
		return null;
	}

	public abstract TemplateGenerator getTemplateGenerator();

}
