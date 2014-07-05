package com.sap.dirigible.runtime.scripting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
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

	private static final String MAX_STORAGE_FILE_SIZE_MESSAGE = "The maximum allowed storage file size is %d MB";

	public static final int MAX_STORAGE_FILE_SIZE_IN_MEGA_BYTES = 2;

	public static final int MAX_STORAGE_FILE_SIZE_IN_BYTES = MAX_STORAGE_FILE_SIZE_IN_MEGA_BYTES * 1024 * 1024;

	public static final String TOO_BIG_DATA_MESSAGE = String.format(MAX_STORAGE_FILE_SIZE_MESSAGE,
			MAX_STORAGE_FILE_SIZE_IN_MEGA_BYTES);

	private static final String DGB_STORAGE_TABLE_DOES_NOT_EXIST_S = "DGB_STORAGE table does not exist: %s";

	private static final String STORAGE_CONTENT_TYPE = "STORAGE_CONTENT_TYPE";

	private static final String STORAGE_DATA = "STORAGE_DATA";

	private static final Logger logger = Logger.getLogger(StorageUtils.class);

	private static final String INSERT_INTO_DGB_STORAGE = "INSERT INTO DGB_STORAGE ("
			+ "STORAGE_PATH, " + "STORAGE_DATA, " + "STORAGE_CONTENT_TYPE, " + "STORAGE_TIMESTAMP)"
			+ "VALUES (?,?,?,?)";
	private static final String UPDATE_DGB_STORAGE = "UPDATE DGB_STORAGE SET "
			+ "STORAGE_PATH = ?, STORAGE_DATA = ?, STORAGE_CONTENT_TYPE=?, STORAGE_TIMESTAMP = ?";

	private static final String DELETE_DGB_STORAGE = "DELETE FROM DGB_STORAGE";
	private static final String DELETE_DGB_STORAGE_PATH = "DELETE FROM DGB_STORAGE WHERE STORAGE_PATH=?";

	private static final String CREATE_TABLE_DGB_STORAGE = "CREATE TABLE DGB_STORAGE ("
			+ "STORAGE_PATH VARCHAR(2048) PRIMARY KEY, " + "STORAGE_DATA BLOB, "
			+ "STORAGE_CONTENT_TYPE VARCHAR(50), " + "STORAGE_TIMESTAMP TIMESTAMP" + " )";
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
		put(path, data, "");
	}

	public void put(String path, byte[] data, String contentType) throws SQLException {
		if (data.length > MAX_STORAGE_FILE_SIZE_IN_BYTES) {
			logger.warn(TOO_BIG_DATA_MESSAGE);
			throw new InvalidParameterException(TOO_BIG_DATA_MESSAGE);
		}
		try {
			checkDB();

			if (exists(path)) {
				update(path, data, contentType);
			} else {
				insert(path, data, contentType);
			}

		} catch (NamingException e) {
			throw new SQLException(e);
		}
	}

	private void insert(String path, byte[] data, String contentType) throws SQLException {
		DataSource dataSource = this.dataSource;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(INSERT_INTO_DGB_STORAGE);

			int i = 0;
			pstmt.setString(++i, path);
			pstmt.setBinaryStream(++i, new ByteArrayInputStream(data), data.length);
			pstmt.setString(++i, contentType);
			pstmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));

			pstmt.executeUpdate();

		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private void update(String path, byte[] data, String contentType) throws SQLException {
		DataSource dataSource = this.dataSource;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(UPDATE_DGB_STORAGE);

			int i = 0;
			pstmt.setString(++i, path);
			pstmt.setBinaryStream(++i, new ByteArrayInputStream(data), data.length);
			pstmt.setString(++i, contentType);
			pstmt.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));

			pstmt.executeUpdate();

		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	// Retrieve photo data from the cache
	public StorageFile get(String path) throws SQLException, IOException {
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
					String contentType = rs.getString(STORAGE_CONTENT_TYPE);
					return new StorageFile(data, contentType);
				}

			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (NamingException e) {
			throw new SQLException(e);
		}
		return null;
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

	public class StorageFile {

		private byte[] data;
		private String contentType;

		public StorageFile(byte[] data, String contentType) {
			this.data = data;
			this.contentType = contentType;
		}

		public byte[] getData() {
			return data;
		}

		public String getContentType() {
			return contentType;
		}
	}

}