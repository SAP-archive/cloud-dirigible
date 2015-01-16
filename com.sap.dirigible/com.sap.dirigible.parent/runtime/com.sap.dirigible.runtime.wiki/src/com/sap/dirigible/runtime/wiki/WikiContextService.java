package com.sap.dirigible.runtime.wiki;

import com.sap.dirigible.runtime.scripting.IContextService;

public class WikiContextService implements IContextService {

	@Override
	public String getName() {
		return "wiki";
	}

	@Override
	public Object getInstance() {
		WikiUtils wikiUtils = new WikiUtils();
		return wikiUtils;
	}

}
