package com.sap.dirigible.runtime.scripting.utils;

import com.sap.dirigible.repository.ext.lucene.CustomMemoryIndexer;

public class IndexerUtils {

	public CustomMemoryIndexer getIndex(String indexName) {
		return CustomMemoryIndexer.getIndex(indexName);
	}

}
