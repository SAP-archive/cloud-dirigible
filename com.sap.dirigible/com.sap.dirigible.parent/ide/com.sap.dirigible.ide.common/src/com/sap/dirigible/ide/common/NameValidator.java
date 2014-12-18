package com.sap.dirigible.ide.common;

import org.eclipse.jface.dialogs.IInputValidator;

public class NameValidator implements IInputValidator {

	private static final long serialVersionUID = 553319995495098208L;

	/**
	 * Validates the String. Returns null for no error, or an error message
	 * 
	 * @param newText
	 *            the String to validate
	 * @return String
	 */
	public String isValid(String newText) {
		return (newText.length() < 1 || !newText.matches("\\w")) ? "Illegal Name provided" : null;
	}

}
