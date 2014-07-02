package com.sap.dirigible.runtime.utils;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;

public class DataSourceUtils {
	
	public static DataSource createLocal() {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("derby"); //$NON-NLS-1$
		dataSource.setCreateDatabase("create"); //$NON-NLS-1$
		return dataSource;
	}

}
