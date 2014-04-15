package com.sap.dirigible.repository.ext.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;

public class MemoryIndexer {
	
	private static final String FIELD_PATH = "path";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_CONTENT = "content";
	
	private static final Logger logger = LoggerFactory.getLogger(MemoryIndexer.class);
	
	private static Directory directory = new RAMDirectory();
	private static List<String> indexedResources = new ArrayList<String>();
	private static Date lastIndexed = new Date();

	private MemoryIndexer() {
		// no external instances
	}
	
	public static void indexRepository(IRepository repository) 
			throws IOException {

		try {
			synchronized (directory) {
				
				logger.debug("entering: indexRepository(IRepository repository)");
				
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
				IndexWriterConfig config = null;
				IndexWriter iwriter = null;
				try {
					config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
					iwriter = new IndexWriter(directory, config);
					
					ICollection collection = repository.getRoot();
					indexCollection(iwriter, collection);
					
					lastIndexed = new Date();
					
				} finally {
					if (iwriter != null) {
						iwriter.close();
					}
//					directory.close();
				}
				logger.debug("exiting: indexRepository(IRepository repository)");
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static void clearIndex()
			throws IOException {

		try {
			synchronized (directory) {
				
				logger.debug("entering: clearIndex()");
				
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
				IndexWriterConfig config = null;
				IndexWriter iwriter = null;
				try {
					config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
					iwriter = new IndexWriter(directory, config);
					iwriter.deleteAll();
				} finally {
					if (iwriter != null) {
						iwriter.close();
					}
//					directory.close();
				}
				logger.debug("exiting: clearIndex()");
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public static List<String> search(String term) 
			throws IOException {
		
		try {
			synchronized (directory) {
				
				logger.debug("entering: search(String term)");
				
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
				// Now search the index:
				IndexSearcher isearcher = null;
				IndexReader ireader = null;
				List<String> docs = new ArrayList<String>(); 
				try {
					ireader = IndexReader.open(directory);
					isearcher = new IndexSearcher(ireader);
					// Parse a simple query that searches for "text":
					QueryParser parser = new QueryParser(Version.LUCENE_35, FIELD_CONTENT, analyzer);
					Query query = parser.parse(term);
					ScoreDoc[] hits = isearcher.search(query, null, 100).scoreDocs;
					// Iterate through the results:
					for (int i = 0; i < hits.length; i++) {
					  Document hitDoc = isearcher.doc(hits[i].doc);
					  docs.add(hitDoc.get(FIELD_PATH));
					}
				} finally {
					if (isearcher != null) {
						isearcher.close();
					}
					if (ireader != null) {
						ireader.close();
					}
//					directory.close();
				}
				
				logger.debug("exiting: search(String term)");
				return docs;
			}
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

	private static void indexCollection(IndexWriter iwriter, ICollection collection) throws IOException {
		
		logger.debug("entering: indexCollection(IndexWriter iwriter, ICollection collection)");
		
		List<IResource> resources = collection.getResources();
		for (Iterator<IResource> iterator = resources.iterator(); iterator.hasNext();) {
			IResource resource = iterator.next();
			indexResource(iwriter, resource);
		}
		List<ICollection> collections = collection.getCollections();
		for (Iterator<ICollection> iterator = collections.iterator(); iterator.hasNext();) {
			ICollection child = iterator.next();
			indexCollection(iwriter, child);
		}
		logger.debug("exiting: indexCollection(IndexWriter iwriter, ICollection collection)");
	}

	private static void indexResource(IndexWriter iwriter, IResource resource)
			throws CorruptIndexException, IOException {
		
		logger.debug("entering: indexResource(IndexWriter iwriter, IResource resource)");
		
		if (!resource.isBinary()) {
			if (!indexedResources.contains(resource.getPath())) {
				logger.debug("Indexing resource: " + resource.getPath());
				Document doc = new Document();
				String text = new String(resource.getContent(), "UTF-8");
				doc.add(new Field(FIELD_CONTENT, text, Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field(FIELD_NAME, resource.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field(FIELD_PATH, resource.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				iwriter.addDocument(doc);
				iwriter.commit();
				logger.debug("Resource: " + resource.getPath() + " indexed successfully");
				indexedResources.add(resource.getPath());
			} else {
				if (lastIndexed.before(resource.getInformation().getModifiedAt())) {
					logger.debug("Updating index for resource: " + resource.getPath());
					Document doc = new Document();
					String text = new String(resource.getContent(), "UTF-8");
					doc.add(new Field(FIELD_CONTENT, text, Field.Store.YES, Field.Index.ANALYZED));
					doc.add(new Field(FIELD_NAME, resource.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
					doc.add(new Field(FIELD_PATH, resource.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
					Term term = new Term(FIELD_PATH, resource.getPath());
					iwriter.updateDocument(term, doc);
					iwriter.commit();
					logger.debug("For resource: " + resource.getPath() + " index updated successfully");
				} else {
					logger.debug("Skip indexing for unmodified resource: " + resource.getPath());
				}
			}
		} else {
			logger.debug("Skip indexing for binary resource: " + resource.getPath());
		}
		
		logger.debug("exiting: indexResource(IndexWriter iwriter, IResource resource)");
	}
	
}
