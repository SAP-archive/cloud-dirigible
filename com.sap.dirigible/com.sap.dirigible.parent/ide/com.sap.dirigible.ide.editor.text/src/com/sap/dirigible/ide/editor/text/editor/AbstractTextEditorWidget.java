package com.sap.dirigible.ide.editor.text.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractTextEditorWidget extends Composite {

	private static final long serialVersionUID = 3624167150503876670L;

	AbstractTextEditorWidget(Composite parent) {
		super(parent, SWT.NONE);
	}

	public AbstractTextEditorWidget(Composite parent, int style) {
		super(parent, style);
	}

	public abstract void setText(final String text, final EditorMode mode, boolean readOnly,
			boolean breakpointsEnabled, int row);
	
	public abstract String getText();
	
}
