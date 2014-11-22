package com.sap.dirigible.runtime.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.dirigible.runtime.filter.XSSUtils;

public class XSSUtilsTest {

	@Test
	public void testXSS() {
		try {
			String script = "<script>alert(\"XSS\");</script>";
			String escaped = XSSUtils.stripXSS(script);
			assertEquals("&lt;script&gt;alert(&quot;XSS&quot;);&lt;/script&gt;",escaped);
		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
	}

}
