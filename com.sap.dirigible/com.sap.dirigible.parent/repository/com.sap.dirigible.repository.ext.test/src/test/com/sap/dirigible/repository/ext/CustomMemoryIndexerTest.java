package test.com.sap.dirigible.repository.ext;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.lucene.document.Document;
import org.junit.Test;

import com.sap.dirigible.repository.ext.lucene.CustomMemoryIndexer;

public class CustomMemoryIndexerTest {

	@Test
	public void testIndexer() {
		try {
			// create indexer
			CustomMemoryIndexer customMemoryIndexer = CustomMemoryIndexer.getIndex("TestIndex");
			
			assertNotNull(customMemoryIndexer);
			
			// create document
			Document document = customMemoryIndexer.createDocument("001", "Test Content");
			// index it
			customMemoryIndexer.indexDocument(document);
			// search it
			List<Document> hitDocs = customMemoryIndexer.search("Test");
			
			assertEquals(1, hitDocs.size());
			assertEquals("001", hitDocs.get(0).get("id"));
			
			// clear index
			customMemoryIndexer.clearIndex();
			// search again
			hitDocs = customMemoryIndexer.search("Test");
			// nothing found
			assertEquals(0, hitDocs.size());
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}


	@Test
	public void testDeleteDocument() {
		try {
			// create indexer
			CustomMemoryIndexer customMemoryIndexer = CustomMemoryIndexer.getIndex("TestIndex");
			
			assertNotNull(customMemoryIndexer);
			
			// create document
			Document document = customMemoryIndexer.createDocument("001", "Test Content");
			// index it
			customMemoryIndexer.indexDocument(document);
			// search it
			List<Document> hitDocs = customMemoryIndexer.search("Test");
			
			assertEquals(1, hitDocs.size());
			assertEquals("001", hitDocs.get(0).get("id"));
			
			document = customMemoryIndexer.createDocument("001", "Test Content");
			// clear index
			customMemoryIndexer.deleteDocument(document);
			// search again
			hitDocs = customMemoryIndexer.search("Test");
			// nothing found
			assertEquals(0, hitDocs.size());
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateDocument() {
		try {
			// create indexer
			CustomMemoryIndexer customMemoryIndexer = CustomMemoryIndexer.getIndex("TestIndex");
			
			assertNotNull(customMemoryIndexer);
			
			// create document
			Document document = customMemoryIndexer.createDocument("001", "Test Content");
			// index it
			customMemoryIndexer.indexDocument(document);
			// search it
			List<Document> hitDocs = customMemoryIndexer.search("Test");
			
			assertEquals(1, hitDocs.size());
			assertEquals("001", hitDocs.get(0).get("id"));
			assertEquals("Test Content", hitDocs.get(0).get("content"));
			
			document = customMemoryIndexer.createDocument("001", "Test Content 1");
			// clear index
			customMemoryIndexer.updateDocument(document);
			// search again
			hitDocs = customMemoryIndexer.search("Test");
			// nothing found
			assertEquals(1, hitDocs.size());
			assertEquals("001", hitDocs.get(0).get("id"));
			assertEquals("Test Content 1", hitDocs.get(0).get("content"));
			
			customMemoryIndexer.clearIndex();
			
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

}
