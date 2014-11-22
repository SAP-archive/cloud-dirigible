package com.sap.dirigible.ide.workspace.ui.view.newmenu;

public class NewMenuException extends RuntimeException {
	private static final long serialVersionUID = 564186555025736228L;

	public NewMenuException() {
		super();
	}

	public NewMenuException(String message) {
		super(message);
	}

	public NewMenuException(Throwable ex) {
		super(ex);
	}

	public NewMenuException(String message, Throwable ex) {
		super(message, ex);
	}
}
