package com.sap.dirigible.runtime.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.repository.ext.lucene.MemoryIndexer;

public class SearchIndexerTest {

	private static IRepository repository;

	@Before
	public void setUp() {
		DataSource dataSource = createLocal();
		try {
			repository = new DBRepository(dataSource, "guest", false); //$NON-NLS-1$
			
			repository.createResource(
					"/db/dirigible/file1.txt", "Apache LuceneTM is a high-performance, full-featured text search engine library written entirely in Java." //$NON-NLS-1$ //$NON-NLS-2$
							.getBytes(), false, "text/plain");
			
			repository.createResource(
					"/db/dirigible/file2.txt", "It is a technology suitable for nearly any application that requires full-text search, especially cross-platform." //$NON-NLS-1$ //$NON-NLS-2$
							.getBytes(), false, "text/plain");
			
			repository.createResource(
					"/db/dirigible/sub/file3.txt", "Apache Lucene is an open source project available for free download. Please use the links on the right to access Lucene." //$NON-NLS-1$ //$NON-NLS-2$
							.getBytes(), false, "text/plain");
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearch() {

		try {
			
			
			MemoryIndexer.indexRepository(repository);
			
			List<String> matches = MemoryIndexer.search("Lucene");
			assertNotNull(matches);
			assertEquals(1, matches.size());
			
			matches = MemoryIndexer.search("Apache");
			assertNotNull(matches);
			assertEquals(2, matches.size());
			
			matches = MemoryIndexer.search("apache");
			assertNotNull(matches);
			assertEquals(2, matches.size());
			
			matches = MemoryIndexer.search("Eclipse");
			assertNotNull(matches);
			assertEquals(0, matches.size());

		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	
	public static DataSource createLocal() {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("derby"); //$NON-NLS-1$
		dataSource.setCreateDatabase("create"); //$NON-NLS-1$
		return dataSource;
	}

}
