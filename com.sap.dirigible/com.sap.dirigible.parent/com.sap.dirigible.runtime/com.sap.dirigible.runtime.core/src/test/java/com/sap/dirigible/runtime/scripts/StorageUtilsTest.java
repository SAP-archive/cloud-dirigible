package com.sap.dirigible.runtime.scripts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.runtime.scripting.StorageUtils;
import com.sap.dirigible.runtime.utils.DataSourceUtils;

public class StorageUtilsTest {
	
	DataSource dataSource = null; 
	
	@Before
	public void setUp() {
		dataSource = DataSourceUtils.createLocal();
	}

	@Test
	public void testPut() {
		StorageUtils storage = new StorageUtils(dataSource);
		try {
			storage.put("/a/b/c", "Some data".getBytes());
			byte[] retrieved = storage.get("/a/b/c");
			assertArrayEquals("Some data".getBytes(), retrieved);
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testClear() {
		StorageUtils storage = new StorageUtils(dataSource);
		try {
			storage.put("/a/b/c", "Some data".getBytes());
			storage.clear();
			byte[] retrieved = storage.get("/a/b/c");
			assertArrayEquals(new byte[]{}, retrieved);
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testDelete() {
		StorageUtils storage = new StorageUtils(dataSource);
		try {
			storage.put("/a/b/c", "Some data".getBytes());
			storage.delete("/a/b/c");
			byte[] retrieved = storage.get("/a/b/c");
			assertArrayEquals(new byte[]{}, retrieved);
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
}
