package com.sap.dirigible.runtime.js.debug;

import java.beans.PropertyChangeSupport;

import com.sap.dirigible.repository.ext.debug.IDebugProtocol;

public class DebugProtocolHandler extends PropertyChangeSupport implements IDebugProtocol {

	private static final long serialVersionUID = 5008236386408550199L;

	public DebugProtocolHandler() {
		super(new Object());
	}

}
