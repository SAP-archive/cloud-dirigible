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

package com.sap.dirigible.repository.db.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.sap.dirigible.repository.api.ContentTypeHelper;
import com.sap.dirigible.repository.api.IRepository;

/**
 * Utility class which imports all the content from a given zip
 * 
 */
public class ZipImporter {

	/**
	 * Import all the content from a given zip to the target repository instance
	 * within the given path
	 * 
	 * @param repository
	 * @param zipInputStream
	 * @param relativeRoot
	 * @throws IOException
	 */
	public static void importZip(IRepository repository,
			ZipInputStream zipInputStream, String relativeRoot)
			throws IOException {
		try {

			byte[] buffer = new byte[2048];

			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) {
				String outpath = relativeRoot + IRepository.SEPARATOR + entry.getName();
				ByteArrayOutputStream output = null;
				try {
					output = new ByteArrayOutputStream();
					int len = 0;
					while ((len = zipInputStream.read(buffer)) > 0) {
						output.write(buffer, 0, len);
					}

					if (output.toByteArray().length > 0) {
						// TODO filter for binary extensions
						String mimeType = null;
						String extension = ContentTypeHelper.getExtension(entry
								.getName());
						if ((mimeType = ContentTypeHelper
								.getContentType(extension)) != null) {
							repository.createResource(outpath,
									output.toByteArray(),
									ContentTypeHelper.isBinary(mimeType),
									mimeType);
						} else {
							repository.createResource(outpath,
									output.toByteArray());
						}
					} else {
						if (outpath.endsWith(Messages.getString("ZipImporter.1"))) { //$NON-NLS-1$
							repository.createCollection(outpath);
						}
					}

				} finally {
					if (output != null)
						output.close();
				}
			}
		} finally {
			zipInputStream.close();
		}
	}

}
