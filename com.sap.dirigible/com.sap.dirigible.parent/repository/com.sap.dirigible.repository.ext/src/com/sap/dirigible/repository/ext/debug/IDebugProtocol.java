package com.sap.dirigible.repository.ext.debug;

import java.beans.PropertyChangeListener;

public interface IDebugProtocol {

	public void addPropertyChangeListener(PropertyChangeListener listener);
	
	public void firePropertyChange(String commandId, Object clientId, Object commandBody);
	
	public void removePropertyChangeListener(PropertyChangeListener listener);
}
