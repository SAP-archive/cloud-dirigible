package com.sap.dirigible.runtime.scripting;

import java.io.StringWriter;

import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.markdown.core.MarkdownLanguage;
//import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;
import org.eclipse.mylyn.wikitext.tracwiki.core.TracWikiLanguage;
import org.eclipse.mylyn.wikitext.twiki.core.TWikiLanguage;

public class WikiUtils {
	
	// confluence, markdown, mediawiki, textile, trackwiki, twiki
	
	public static final String WIKI_FORMAT_CONFLUENCE = "CONFLUENCE";
	public static final String WIKI_FORMAT_MARKDOWN = "MARKDOWN";
//	public static final String WIKI_FORMAT_MEDIAWIKI = "MEDIAWIKI";
	public static final String WIKI_FORMAT_TEXTILE = "TEXTILE";
	public static final String WIKI_FORMAT_TRACWIKI = "TRACWIKI";
	public static final String WIKI_FORMAT_TWIKI = "TWIKI";
	
	
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
		setLanguage(format, markupParser);
		markupParser.parse(content);
		String htmlContent = writer.toString();
		return htmlContent;
	}

	private void setLanguage(String format, MarkupParser markupParser) {
		if (WIKI_FORMAT_CONFLUENCE.equalsIgnoreCase(format)) {
			markupParser.setMarkupLanguage(new ConfluenceLanguage());
		} else if (WIKI_FORMAT_MARKDOWN.equalsIgnoreCase(format)) {
			markupParser.setMarkupLanguage(new MarkdownLanguage());
//		} else if (WIKI_FORMAT_MEDIAWIKI.equalsIgnoreCase(format)) {
//			markupParser.setMarkupLanguage(new MediaWikiLanguage());
		} else if (WIKI_FORMAT_TEXTILE.equalsIgnoreCase(format)) {
			markupParser.setMarkupLanguage(new TextileLanguage());
		} else if (WIKI_FORMAT_TRACWIKI.equalsIgnoreCase(format)) {
			markupParser.setMarkupLanguage(new TracWikiLanguage());
		} else if (WIKI_FORMAT_TWIKI.equalsIgnoreCase(format)) {
			markupParser.setMarkupLanguage(new TWikiLanguage());
		} else {
			// default
			markupParser.setMarkupLanguage(new ConfluenceLanguage());
		}
	}

}
