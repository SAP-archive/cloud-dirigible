/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

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
