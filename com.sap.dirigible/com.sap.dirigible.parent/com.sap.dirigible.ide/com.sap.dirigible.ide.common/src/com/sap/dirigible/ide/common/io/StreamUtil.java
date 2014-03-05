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

package com.sap.dirigible.ide.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This utility class provides methods for easy working with streams.
 * 
 */
public final class StreamUtil {

	private static final String INPUT_STREAM_IS_NULL = Messages.StreamUtil_INPUT_STREAM_IS_NULL;

	/**
	 * Transfers all data from the specified {@link InputStream} into the
	 * specified {@link OutputStream}.
	 * 
	 * @param in
	 *            stream from which data will be read
	 * @param out
	 *            stream to which data will be written
	 * @throws IOException
	 *             if an I/O exception occurs.
	 */
	public static void transferData(InputStream in, OutputStream out)
			throws IOException {
		final byte[] buffer = new byte[1024];
		int count = 0;
		while ((count = in.read(buffer)) > 0) {
			out.write(buffer, 0, count);
		}
	}

	/**
	 * Reads all of the data provided by the specified {@link InputStream}.
	 * 
	 * @param in
	 *            input stream to be read
	 * @throws IOException
	 *             if an I/O exception occurs.
	 */
	public static byte[] readFully(InputStream in) throws IOException {
		if (in == null) {
			throw new IOException(INPUT_STREAM_IS_NULL);
		}
		final ByteArrayOutputStream out = new ByteArrayOutputStream(
				in.available());
		try {
			transferData(in, out);
		} finally {
			in.close();
		}
		return out.toByteArray();
	}

	/*
	 * Prevent instantiation
	 */
	private StreamUtil() {
		super();
	}

}
