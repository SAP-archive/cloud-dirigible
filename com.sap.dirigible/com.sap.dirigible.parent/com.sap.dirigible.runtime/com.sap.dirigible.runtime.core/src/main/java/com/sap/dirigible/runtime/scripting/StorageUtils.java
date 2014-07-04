package com.sap.dirigible.runtime.scripting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sap.dirigible.repository.db.dao.DBMapper;
import com.sap.dirigible.runtime.logger.Logger;

public class StorageUtils {
	
	private static final String DGB_STORAGE_TABLE_DOES_NOT_EXIST_S = "DGB_STORAGE table does not exist: %s";

	private static final String STORAGE_DATA = "STORAGE_DATA";

	private static final Logger logger = Logger.getLogger(StorageUtils.class);
	
	private static final String INSERT_INTO_DGB_STORAGE = "INSERT INTO DGB_STORAGE ("
			+ "STORAGE_PATH, " + "STORAGE_DATA, "
			+ "STORAGE_TIMESTAMP)" + "VALUES (?,?,?)";
	private static final String UPDATE_DGB_STORAGE = "UPDATE DGB_STORAGE SET "
			+ "STORAGE_PATH = ?, STORAGE_DATA = ?, STORAGE_TIMESTAMP = ?";

	private static final String DELETE_DGB_STORAGE = "DELETE FROM DGB_STORAGE";
	private static final String DELETE_DGB_STORAGE_PATH = "DELETE FROM DGB_STORAGE WHERE STORAGE_PATH=?";

	private static final String CREATE_TABLE_DGB_STORAGE = "CREATE TABLE DGB_STORAGE ("
			+ "STORAGE_PATH VARCHAR(2048) PRIMARY KEY, " + "STORAGE_DATA BLOB, "
			+ "STORAGE_TIMESTAMP TIMESTAMP" + " )";
	private static final String SELECT_COUNT_FROM_DGB_STORAGE = "SELECT COUNT(*) FROM DGB_STORAGE";
	private static final String SELECT_DGB_STORAGE = "SELECT * FROM DGB_STORAGE WHERE STORAGE_PATH=?";
	
	private static final String SELECT_DGB_STORAGE_EXISTS = "SELECT STORAGE_PATH FROM DGB_STORAGE WHERE STORAGE_PATH=?";
	
	private DataSource dataSource;
	
	public StorageUtils(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void checkDB() throws NamingException, SQLException {
		DataSource dataSource = this.dataSource;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement stmt = connection.createStatement();
			try {
				stmt.executeQuery(SELECT_COUNT_FROM_DGB_STORAGE);
			} catch (Exception e) {
				logger.warn(String.format(DGB_STORAGE_TABLE_DOES_NOT_EXIST_S, e.getMessage()));
				// Create Table
				stmt.executeUpdate(CREATE_TABLE_DGB_STORAGE);
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	public void put(String path, byte[] data) throws SQLException {
		try {
			checkDB();
			
			if (exists(path)) {
				update(path, data);
			} else {
				insert(path, data);
			}
			
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	private void insert(String path, byte[] data) throws SQLException {
		DataSource dataSource = this.dataSource;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(INSERT_INTO_DGB_STORAGE);

			int i = 0;
			pstmt.setString(++i, path);
			pstmt.setBinaryStream(++i, new ByteArrayInputStream(data), data.length);
			pstmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));

			pstmt.executeUpdate();

		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	private void update(String path, byte[] data) throws SQLException {
		DataSource dataSource = this.dataSource;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(UPDATE_DGB_STORAGE);

			int i = 0;
			pstmt.setString(++i, path);
			pstmt.setBinaryStream(++i, new ByteArrayInputStream(data), data.length);
			pstmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));

			pstmt.executeUpdate();

		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	// Retrieve photo data from the cache
	public byte[] get(String path) throws SQLException, IOException {
		try {
			checkDB();
			
			DataSource dataSource = this.dataSource;
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(SELECT_DGB_STORAGE);
				pstmt.setString(1, path);
				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					byte[] data = DBMapper.dbToDataBinary(rs, STORAGE_DATA);
					return data;
				}
				
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
		return new byte[]{};
	}
	
	public boolean exists(String path) throws SQLException {
		try {
			checkDB();
			
			DataSource dataSource = this.dataSource;
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(SELECT_DGB_STORAGE_EXISTS);
				pstmt.setString(1, path);
				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					return true;
				}
				
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
		return false;
	}
	
	public void clear() throws SQLException {
		try {
			checkDB();

			DataSource dataSource = this.dataSource;
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(DELETE_DGB_STORAGE);
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}
	
	public void delete(String path) throws SQLException {
		try {
			checkDB();

			DataSource dataSource = this.dataSource;
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(DELETE_DGB_STORAGE_PATH);
				pstmt.setString(1, path);
				pstmt.executeUpdate();
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

}