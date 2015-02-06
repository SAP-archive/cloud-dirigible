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

package com.sap.dirigible.runtime.metrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sap.dirigible.repository.logging.Logger;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

public class AccessLogLocationsDAO {

	private static final String DELETE_FROM_DGB_ACCESS_LOG_LOCATIONS_WHERE_ACCLOGLOCATION = "DELETE FROM DGB_ACCESS_LOG_LOCATIONS WHERE ACCLOGLOC_LOCATION = ?";
	private static final String DELETE_FROM_DGB_ACCESS_LOG_LOCATIONS_ALL = "DELETE FROM DGB_ACCESS_LOG_LOCATIONS";

	private static final String SELECT_ALL_DGB_ACCESS_LOG_LOCATIONS = "SELECT * FROM DGB_ACCESS_LOG_LOCATIONS";

	private static final String INSERT_INTO_DGB_ACCESS_LOG_LOCATIONS = "INSERT INTO DGB_ACCESS_LOG_LOCATIONS ("
			+ "ACCLOGLOC_LOCATION) " + "VALUES (?)";

	private static final String CREATE_TABLE_DGB_ACCESS_LOG_LOCATIONS = "CREATE TABLE DGB_ACCESS_LOG_LOCATIONS ("
			+ " ACCLOGLOC_LOCATION VARCHAR(256))";

	private static final String SELECT_COUNT_FROM_DGB_ACCESS_LOG_LOCATIONS = "SELECT COUNT(*) FROM DGB_ACCESS_LOG_LOCATIONS";

	private static final Logger logger = Logger.getLogger(AccessLogLocationsDAO.class);

	public static void refreshLocations() throws SQLException {
		try {
			checkDB();

			DataSource dataSource = RepositoryFacade.getInstance().getDataSource();
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				PreparedStatement pstmt = connection
						.prepareStatement(SELECT_ALL_DGB_ACCESS_LOG_LOCATIONS);

				ResultSet rs = pstmt.executeQuery();

				AccessLogLocationsSynchronizer.getAccessLogLocations().clear();
				while (rs.next()) {
					String location = rs.getString(1);
					AccessLogLocationsSynchronizer.getAccessLogLocations().add(location);
				}

			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	public static void insertLocation(String location) throws SQLException {
		try {
			checkDB();

			DataSource dataSource = RepositoryFacade.getInstance().getDataSource();
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				PreparedStatement pstmt = connection
						.prepareStatement(INSERT_INTO_DGB_ACCESS_LOG_LOCATIONS);

				pstmt.setString(1, location);

				pstmt.executeUpdate();

				AccessLogLocationsSynchronizer.getAccessLogLocations().add(location);

			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	public static void deleteLocation(String location) throws SQLException {
		try {
			checkDB();

			DataSource dataSource = RepositoryFacade.getInstance().getDataSource();
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				PreparedStatement pstmt = connection
						.prepareStatement(DELETE_FROM_DGB_ACCESS_LOG_LOCATIONS_WHERE_ACCLOGLOCATION);

				pstmt.setString(1, location);

				pstmt.executeUpdate();

				AccessLogLocationsSynchronizer.getAccessLogLocations().remove(location);

			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	public static void deleteAllLocations() throws SQLException {
		try {
			checkDB();

			DataSource dataSource = RepositoryFacade.getInstance().getDataSource();
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				PreparedStatement pstmt = connection
						.prepareStatement(DELETE_FROM_DGB_ACCESS_LOG_LOCATIONS_ALL);

				pstmt.executeUpdate();

				AccessLogLocationsSynchronizer.getAccessLogLocations().clear();

			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	private static void checkDB() throws NamingException, SQLException {
		DataSource dataSource = RepositoryFacade.getInstance().getDataSource();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement stmt = connection.createStatement();

			try {
				stmt.executeQuery(SELECT_COUNT_FROM_DGB_ACCESS_LOG_LOCATIONS);
			} catch (Exception e) {
				logger.error("DGB_ACCESS_LOG does not exist?" + e.getMessage());
				// Create Access Log Table
				stmt.executeUpdate(CREATE_TABLE_DGB_ACCESS_LOG_LOCATIONS);
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

}
