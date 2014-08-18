package com.sap.dirigible.ide.workspace.ui.shared;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public abstract class FocusableWizardPage extends WizardPage{
	private static final long serialVersionUID = -3674212416615171129L;

	private Control control;
	
	protected FocusableWizardPage(String pageName){
		super(pageName);
	}
	
	protected void setFocusable(Control control){
		this.control = control;
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		control.setFocus();
		if(control instanceof Text){
			((Text) control).selectAll();
		}
	}
}
