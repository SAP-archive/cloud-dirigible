package com.sap.dirigible.runtime.scripts;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.security.InvalidParameterException;

import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.runtime.scripting.StorageUtils;
import com.sap.dirigible.runtime.scripting.StorageUtils.StorageFile;
import com.sap.dirigible.runtime.utils.DataSourceUtils;

public class StorageUtilsTest {

	private static final String PATH = "/a/b/c";
	private static final byte[] DATA = "Some data".getBytes();
	private static final String CONTENT_TYPE = "application/pdf";
	private static final String EMPTY_CONTENT_TYPE = "";
	private static final byte[] OTHER_DATA = "Other data".getBytes();
	private static final byte[] TOO_BIG_DATA = new byte[StorageUtils.MAX_STORAGE_FILE_SIZE_IN_BYTES + 1];

	private StorageUtils storage;

	@Before
	public void setUp() {
		storage = new StorageUtils(DataSourceUtils.createLocal());
	}

	@Test
	public void testPut() {
		try {
			storage.put(PATH, DATA);
			StorageFile retrieved = storage.get(PATH);
			assertArrayEquals(DATA, retrieved.getData());
			assertEquals(EMPTY_CONTENT_TYPE, retrieved.getContentType());
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testPutContentType() {
		try {
			storage.put(PATH, DATA, CONTENT_TYPE);
			StorageFile retrieved = storage.get(PATH);
			assertArrayEquals(DATA, retrieved.getData());
			assertEquals(CONTENT_TYPE, retrieved.getContentType());
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPutTooBigData() {
		try {
			storage.put(PATH, TOO_BIG_DATA);
			fail("Test should fail, because " + StorageUtils.TOO_BIG_DATA_MESSAGE);
		} catch (InvalidParameterException e) {
			assertEquals(StorageUtils.TOO_BIG_DATA_MESSAGE, e.getMessage());
		} catch (Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testClear() {
		try {
			storage.put(PATH, DATA);
			storage.clear();
			assertNull(storage.get(PATH));
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testDelete() {
		try {
			storage.put(PATH, DATA);
			storage.delete(PATH);
			assertNull(storage.get(PATH));
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testSet() {
		try {
			storage.put(PATH, DATA);
			storage.put(PATH, OTHER_DATA, CONTENT_TYPE);
			StorageFile retrieved = storage.get(PATH);
			assertArrayEquals(OTHER_DATA, retrieved.getData());
			assertEquals(CONTENT_TYPE, retrieved.getContentType());
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

}
