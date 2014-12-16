package com.sap.dirigible.runtime.scripting;

import java.io.StringWriter;

import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;

public class WikiUtils {
	
	public static final String WIKI_FORMAT_CONFLUENCE = "CONFLUENCE";
	
	public String toHtml(String confluence) {
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		builder.setEmitAsDocument(false);
		MarkupParser markupParser = new MarkupParser();
		markupParser.setBuilder(builder);		
		markupParser.setMarkupLanguage(new ConfluenceLanguage());
		markupParser.parse(confluence);
		String htmlContent = writer.toString();
		return htmlContent;
	}
	
	public String toHtml(String content, String format) {
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		builder.setEmitAsDocument(false);
		MarkupParser markupParser = new MarkupParser();
		markupParser.setBuilder(builder);
		if (WIKI_FORMAT_CONFLUENCE.equalsIgnoreCase(format)) {
			markupParser.setMarkupLanguage(new ConfluenceLanguage());
			// ...
		} else {
			// default
			markupParser.setMarkupLanguage(new ConfluenceLanguage());
		}
		markupParser.parse(content);
		String htmlContent = writer.toString();
		return htmlContent;
	}

}
