package test.com.sap.dirigible.ide.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.dirigible.ide.common.CommonParameters;
import com.sap.dirigible.ide.common.CommonUtils;

public class CommonUtilsTest {

	private static final String SEPARATOR = CommonParameters.SEPARATOR;
	private static final String FOLDER = CommonParameters.SCRIPTING_CONTENT_FOLDER;
	private static final String PROJECT = "project";
	private static final String SUB_FOLDER = "subFolder";
	private static final String FILE_NAME = "file.js";

	@Test
	public void testFormatToIDEPath() throws Exception {
		final String runtimePath = SEPARATOR + PROJECT + SEPARATOR + FILE_NAME;
		final String expectedPath = SEPARATOR + PROJECT + SEPARATOR + FOLDER + SEPARATOR
				+ FILE_NAME;

		String actualPath = CommonUtils.formatToIDEPath(FOLDER, runtimePath);

		assertEquals(expectedPath, actualPath);
	}

	@Test
	public void testFormatToRuntimePath() throws Exception {
		final String idePath = SEPARATOR + PROJECT + SEPARATOR + FOLDER + SEPARATOR + FILE_NAME;
		final String expectedPath = SEPARATOR + PROJECT + SEPARATOR + FILE_NAME;

		String actualPath = CommonUtils.formatToRuntimePath(FOLDER, idePath);

		assertEquals(expectedPath, actualPath);
	}

	@Test
	public void testFormatToIDEPathWithSubFolder() throws Exception {
		final String runtimePath = SEPARATOR + PROJECT + SEPARATOR + SUB_FOLDER + SEPARATOR
				+ FILE_NAME;
		final String expectedPath = SEPARATOR + PROJECT + SEPARATOR + FOLDER + SEPARATOR
				+ SUB_FOLDER + SEPARATOR + FILE_NAME;

		String actualPath = CommonUtils.formatToIDEPath(FOLDER, runtimePath);

		assertEquals(expectedPath, actualPath);
	}

	@Test
	public void testFormatToRuntimePathWithSubFolder() throws Exception {
		final String idePath = SEPARATOR + PROJECT + SEPARATOR + FOLDER + SEPARATOR + SUB_FOLDER
				+ SEPARATOR + FILE_NAME;
		final String expectedPath = SEPARATOR + PROJECT + SEPARATOR + SUB_FOLDER + SEPARATOR
				+ FILE_NAME;

		String actualPath = CommonUtils.formatToRuntimePath(FOLDER, idePath);

		assertEquals(expectedPath, actualPath);
	}
}
