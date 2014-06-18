package com.sap.dirigible.ide.template.ui.ed.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.common.CommonUtils;
import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.ide.template.ui.ed.wizard.Messages;
import com.sap.dirigible.ide.workspace.RemoteResourcesPlugin;
import com.sap.dirigible.ide.workspace.ui.commands.OpenHandler;
import com.sap.dirigible.repository.ext.extensions.ExtensionDefinition;
import com.sap.dirigible.repository.ext.extensions.ExtensionManager;
import com.sap.dirigible.repository.ext.extensions.ExtensionPointDefinition;

public class ExtensionsView extends ViewPart {

	private static final Logger logger = Logger.getLogger(ExtensionsView.class);

	public static final String EXTENSIONS_ERROR = Messages.ExtensionsView_EXTENSIONS_ERROR;

	private TreeViewer viewer;

	private ExtensionManager extensionManager = ExtensionManager.getInstance(RepositoryFacade
			.getInstance().getRepository(), DataSourceFacade.getInstance().getDataSource());

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(new ExtensionsContentProvider(extensionManager, viewer
				.getControl().getShell()));
		viewer.setLabelProvider(new ExtensionsLabelProvider());
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(getExtensionPoints());

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object firstElement = selection.getFirstElement();

				if (firstElement != null) {
					if (firstElement instanceof ExtensionPointDefinition) {
						ExtensionPointDefinition extensionPoint = (ExtensionPointDefinition) firstElement;
						openEditor(CommonUtils.formatToIDEPath(
								CommonParameters.EXTENSION_CONTENT_FOLDER,
								extensionPoint.getLocation())
								+ CommonParameters.EXTENSION_POINT_EXTENSION);
					} else if (firstElement instanceof ExtensionDefinition) {
						ExtensionDefinition extension = (ExtensionDefinition) firstElement;
						openEditor(CommonUtils.formatToIDEPath(
								CommonParameters.EXTENSION_CONTENT_FOLDER, extension.getLocation())
								+ CommonParameters.EXTENSION_EXTENSION);
					}
				}
			}
		});
	}

	private void openEditor(String path) {
		IPath location = new Path(path);
		IWorkspace workspace = RemoteResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IFile file = root.getFile(location);
		if (file.exists()) {
			OpenHandler.open(file, 0);
		}
	}

	private List<ExtensionPointDefinition> getExtensionPoints() {
		List<ExtensionPointDefinition> extensionPoints = new ArrayList<ExtensionPointDefinition>();
		try {
			for (String nextExtensionPoint : extensionManager.getExtensionPoints()) {
				extensionPoints.add(extensionManager.getExtensionPoint(nextExtensionPoint));
			}
		} catch (Exception e) {
			logger.error(EXTENSIONS_ERROR, e);
			MessageDialog.openError(viewer.getControl().getShell(), EXTENSIONS_ERROR,
					e.getMessage());
		}
		return extensionPoints;
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
