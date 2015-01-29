package com.sap.dirigible.ide.editor.text.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TextEditorWidget extends AbstractTextEditorWidget {
	
	private static final long serialVersionUID = -4840499933475637489L;
	
	private Text text;

	public TextEditorWidget(Composite parent, int style) {
		super(parent, style);
		super.setLayout(new FillLayout());

		this.text = new Text(this, SWT.NONE);
	}

	public TextEditorWidget(Composite parent) {
		this(parent, SWT.NONE);
	}

	@Override
	public void setText(String text, EditorMode mode, boolean readOnly,
			boolean breakpointsEnabled, int row) {
		this.text.setText(text);
	}

	@Override
	public String getText() {
		return this.text.getText();
	}

}
