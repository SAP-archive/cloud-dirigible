package com.sap.dirigible.ide.template.ui.ed.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.ide.repository.RepositoryFacade;
import com.sap.dirigible.ide.template.ui.ed.wizard.Messages;
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
