package com.sap.dirigible.runtime.services;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.dirigible.runtime.memory.MemoryLogRecordDAO;

public class MemoryTest {
	
	@Test
	public void testMemory() {
		assertNotNull(MemoryLogRecordDAO.generateMemoryInfo());
	}
}
