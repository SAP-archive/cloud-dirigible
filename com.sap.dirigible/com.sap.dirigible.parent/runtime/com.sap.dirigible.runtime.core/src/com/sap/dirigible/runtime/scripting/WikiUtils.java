package com.sap.dirigible.runtime.scripting;

import java.io.StringWriter;

import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;

public class WikiUtils {
	
	public String toHtml(String confluence) {
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		builder.setEmitAsDocument(false);
		MarkupParser markupParser = new MarkupParser();
		markupParser.setBuilder(builder);		
		markupParser.setMarkupLanguage(new ConfluenceLanguage());
		markupParser.parse(new String(confluence));
		String htmlContent = writer.toString();
		return htmlContent;
	}

}
