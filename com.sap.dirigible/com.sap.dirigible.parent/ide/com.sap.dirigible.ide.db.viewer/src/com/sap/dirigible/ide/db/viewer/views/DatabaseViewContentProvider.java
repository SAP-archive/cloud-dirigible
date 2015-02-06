/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.ide.db.viewer.views;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.repository.ext.db.DBUtils;
import com.sap.dirigible.repository.ext.db.dialect.IDialectSpecifier;
import com.sap.dirigible.repository.logging.Logger;

public class DatabaseViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {
	
	private static final Logger logger = Logger.getLogger(DatabaseViewContentProvider.class);

	private static final String DIRIGIBLE_SYSTEM_TALBES_PREFIX = "DGB_";

	private static final String PRCNT = "%"; //$NON-NLS-1$

	private static final String EMPTY = ""; //$NON-NLS-1$

	private static final String CBC = "] "; //$NON-NLS-1$

	private static final String CBO = " ["; //$NON-NLS-1$

	private static final long serialVersionUID = 8868769345708033548L;

	private TreeParent invisibleRoot;

	public DatabaseViewer databaseViewer;

	public DatabaseViewContentProvider(DatabaseViewer databaseViewer) {
		this.databaseViewer = databaseViewer;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		//
	}

	public void dispose() {
		//
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(databaseViewer.getViewSite())) {
			if (invisibleRoot == null)
				initialize();
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof TreeObject) {
			return ((TreeObject) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof TreeParent) {
			return ((TreeParent) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof TreeParent)
			return ((TreeParent) parent).hasChildren();
		return false;
	}

	private void initialize() {

		try {
			Connection connection = this.databaseViewer.getDatabaseConnection();

			try {
				// get the database metadata
				DatabaseMetaData dmd = connection.getMetaData();

				TreeParent schemes = null;
				// TreeParent tables = null;
				List<TreeParent> schemesMid = new ArrayList<TreeParent>();
				schemes = new TreeParent(CommonParameters.getDatabaseProductName() + CBO
						+ CommonParameters.getDatabaseProductVersion() + CBC + CommonParameters.getDriverName(),
						this.databaseViewer);

				// list catalogs
				// List<String> listOfCatalogs = getListOfCatalogs(dmd);
				// for (Iterator iteratorCatalogs =
				// listOfCatalogs.iterator();
				// iteratorCatalogs.hasNext();) {
				//
				// String catalogName = (String) iteratorCatalogs.next();
				// TreeParent catalog = new TreeParent(catalogName);

				boolean isOperator = CommonParameters.isUserInRole(CommonParameters.ROLE_OPERATOR);
				String catalogName = null;
				// list schemes
				List<String> listOfSchemes = getListOfSchemes(connection, catalogName);
				for (Iterator<String> iteratorSchemes = listOfSchemes.iterator(); iteratorSchemes
						.hasNext();) {

					String schemeName = (String) iteratorSchemes.next();

					TreeParent scheme = new TreeParent(schemeName, this.databaseViewer);

					// get a list of all tables
					List<String> listOfTables = getListOfTables(dmd, catalogName, schemeName);
					// tables = new TreeParent(schemeName);
					for (Iterator<String> iteratorTables = listOfTables.iterator(); iteratorTables
							.hasNext();) {
						String tableName = iteratorTables.next();
						if (!isOperator && tableName.startsWith(DIRIGIBLE_SYSTEM_TALBES_PREFIX)) {
							continue;
						}
						TreeObject toTable = new TreeObject(tableName, new TableDefinition(
								catalogName, schemeName, tableName));
						scheme.addChild(toTable);
					}
					// scheme.addChild(tables);
					schemesMid.add(scheme);
				}

				invisibleRoot = new TreeParent(EMPTY, this.databaseViewer);
				if (this.databaseViewer.showSchemes() || schemesMid.size() > 1) {
					for (TreeParent treeParent : schemesMid) {
						schemes.addChild(treeParent);
					}
					invisibleRoot.addChild(schemes);
				}
				// else {
				// invisibleRoot.addChild(tables);
				// }
			} finally {
				if (connection != null) {
					connection.close();
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			TreeParent root = new TreeParent(e.getMessage(), this.databaseViewer);
			invisibleRoot = new TreeParent(EMPTY, this.databaseViewer);
			invisibleRoot.addChild(root);
		}

	}

	private List<String> getListOfSchemes(Connection connection, String catalogName)
			throws SQLException {

		DatabaseMetaData metaData = connection.getMetaData();

		List<String> listOfSchemes = new ArrayList<String>();
		ResultSet rs = null;

		String productName = CommonParameters.getDatabaseProductName();
		IDialectSpecifier dialectSpecifier = DBUtils.getDialectSpecifier(productName);

		IFilter schemaFilter = databaseViewer.getSchemaFilter(connection);
		try {
			if (dialectSpecifier.isSchemaFilterSupported()) {
				try {
					// low level filtering for schema
					rs = connection.createStatement().executeQuery(
							dialectSpecifier.getSchemaFilterScript());
				} catch (Exception e) {
					// backup in case of wrong product recognition
					rs = metaData.getSchemas(catalogName, null);
				}
			} else {
				rs = metaData.getSchemas(catalogName, null);
			}
			while (rs.next()) {
				String schemeName = rs.getString(1);
				// higher level filtering for schema if low level is not
				// supported
				if (schemaFilter != null && !schemaFilter.select(schemeName)) {
					continue;
				}
				listOfSchemes.add(schemeName);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		return listOfSchemes;
	}

	private List<String> getListOfTables(DatabaseMetaData dmd, String catalogName, String schemeName)
			throws SQLException {

		List<String> listOfTables = new ArrayList<String>();

		ResultSet rs = dmd.getTables(catalogName, schemeName, PRCNT, CommonParameters.TABLE_TYPES);

		while (rs.next()) {
			String tableName = rs.getString(3);
			listOfTables.add(tableName);
		}
		rs.close();

		return listOfTables;
	}

	public void requestRefreshContent() {
		invisibleRoot = null;
	}
}
