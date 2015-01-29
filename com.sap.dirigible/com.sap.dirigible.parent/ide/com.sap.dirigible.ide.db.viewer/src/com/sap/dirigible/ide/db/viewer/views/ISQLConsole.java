package com.sap.dirigible.ide.db.viewer.views;

public interface ISQLConsole {
	
	public void setQuery(String query);
	
	public void executeStatement(boolean isQuery);

}
