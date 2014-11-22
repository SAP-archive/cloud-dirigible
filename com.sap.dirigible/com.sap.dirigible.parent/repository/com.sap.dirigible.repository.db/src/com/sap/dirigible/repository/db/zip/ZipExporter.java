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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;

/**
 * Utility class which exports all the content under a given path
 * 
 */
public class ZipExporter {

	private static final String RELATIVE_ROOT_S_DOESN_T_EXIST = Messages.getString("ZipExporter.RELATIVE_ROOT_S_DOESN_T_EXIST"); //$NON-NLS-1$

	/**
	 * Export all the content under the given path(s) with the target repository
	 * instance Include the last segment of the relative roots during the
	 * archiving
	 * 
	 * @param repository
	 * @param relativeRoot
	 * @return
	 * @throws IOException
	 */
	public static byte[] exportZip(IRepository repository,
			List<String> relativeRoots) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = null;
		try {
			zipOutputStream = new ZipOutputStream(baos);

			for (Iterator<String> iterator = relativeRoots.iterator(); iterator
					.hasNext();) {
				String relativeRoot = iterator.next();
				ICollection collection = repository.getCollection(relativeRoot);
				if (collection.exists()) {
					traverseCollection(zipOutputStream, collection,
							relativeRoot.length()
									- collection.getName().length());
				} else {
					IResource iResource = repository.getResource(relativeRoot);
					if (iResource.exists()) {
						ZipEntry zipEntry = new ZipEntry(iResource.getPath()
								.substring(
										relativeRoot.length()
												- iResource.getName().length()));
						zipOutputStream.putNextEntry(zipEntry);
						zipOutputStream
								.write((iResource.getContent() == null ? "" //$NON-NLS-1$
										.getBytes() : iResource.getContent()));
						zipOutputStream.closeEntry();
					} else {
						throw new IOException(String.format(
								RELATIVE_ROOT_S_DOESN_T_EXIST, relativeRoot));
					}
				}
			}
		} finally {
			if (zipOutputStream != null) {
				zipOutputStream.finish();
				zipOutputStream.flush();
				zipOutputStream.close();
			}
		}

		byte[] result = baos.toByteArray();
		return result;
	}

	/**
	 * Export all the content under the given path with the target repository
	 * instance Include or NOT the last segment of the relative root during the
	 * archiving
	 * 
	 * @param repository
	 * @param relativeRoot
	 *            single root
	 * @param inclusive
	 *            whether to include the last segment of the root or to pack its
	 *            content directly in the archive
	 * @return
	 * @throws IOException
	 */
	public static byte[] exportZip(IRepository repository, String relativeRoot,
			boolean inclusive) throws IOException {

		List<String> relativeRoots = new ArrayList<String>();

		ICollection collection = repository.getCollection(relativeRoot);
		if (collection.exists()) {

			if (inclusive) {
				relativeRoots.add(relativeRoot);
			} else {
				List<IEntity> entities = collection.getChildren();
				for (IEntity iEntity : entities) {
					relativeRoots.add(iEntity.getPath());
				}
			}
			return exportZip(repository, relativeRoots);
		} else {
			IResource resource = repository.getResource(relativeRoot);
			if (resource.exists()) {
				relativeRoots.add(resource.getPath());
				return exportZip(repository, relativeRoots);
			} else {
				throw new IOException(String.format(
						RELATIVE_ROOT_S_DOESN_T_EXIST, relativeRoot));
			}

		}
	}

	/**
	 * Iterate recursively a given collection and put its content to the zip
	 * 
	 * @param zipOutputStream
	 * @param collection
	 * @param substring
	 * @throws IOException
	 */
	private static void traverseCollection(ZipOutputStream zipOutputStream,
			ICollection collection, int substring) throws IOException {

		ZipEntry zipEntry = null;
		if (collection.getPath().length() >= substring) {
			zipEntry = new ZipEntry(collection.getPath().substring(substring)
					+ IRepository.SEPARATOR);
			zipOutputStream.putNextEntry(zipEntry);
			zipOutputStream.closeEntry();
		}

		List<ICollection> collections = collection.getCollections();
		for (Iterator<ICollection> iterator = collections.iterator(); iterator
				.hasNext();) {
			ICollection iCollection = iterator.next();
			traverseCollection(zipOutputStream, iCollection, substring);
		}

		List<IResource> resources = collection.getResources();
		for (Iterator<IResource> iterator = resources.iterator(); iterator
				.hasNext();) {
			IResource iResource = iterator.next();
			zipEntry = new ZipEntry(iResource.getPath().substring(substring));
			zipOutputStream.putNextEntry(zipEntry);
			zipOutputStream.write((iResource.getContent() == null ? "" //$NON-NLS-1$
					.getBytes() : iResource.getContent()));
			zipOutputStream.closeEntry();
		}
	}

}
