package com.sap.dirigible.ide.common;

import java.net.URI;

import org.eclipse.jface.dialogs.IInputValidator;

public class UriValidator implements IInputValidator {

	private static final long serialVersionUID = 553319995495098208L;

	/**
	 * Validates the String. Returns null for no error, or an error message
	 * 
	 * @param newText
	 *            the String to validate
	 * @return String
	 */
	public String isValid(String newText) {
		try {
			URI.create(newText);
			return null;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
