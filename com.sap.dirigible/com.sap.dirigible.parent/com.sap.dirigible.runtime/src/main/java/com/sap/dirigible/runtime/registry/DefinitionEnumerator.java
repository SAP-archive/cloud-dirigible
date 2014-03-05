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

package com.sap.dirigible.runtime.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;

public class DefinitionEnumerator {

    private final List<String> list = new ArrayList<String>();

    public DefinitionEnumerator(final String repositoryPath,
                                final ICollection collection, final String fileExtension) throws IOException {
        enumerateJsDefinitions(repositoryPath, collection, fileExtension);
    }

    public List<String> toArrayList() {
        return list;
    }

    private void enumerateJsDefinitions(final String repositoryPath, final ICollection collection,
                                        final String fileExtension)
            throws IOException {
        if (collection.exists()) {
            for (final IResource resource : collection.getResources()) {
                if (resource != null && resource.getName() != null) {
                    if (resource.getName().endsWith(fileExtension)) {
                        final String fullPath =
                            collection.getPath().substring(repositoryPath.length()) + IRepository.SEPARATOR + resource.getName();
                        this.list.add(fullPath);
                    }
                }
            }
            for (final ICollection subCollection : collection.getCollections()) {
                enumerateJsDefinitions(repositoryPath, subCollection,
                        fileExtension);
            }
        }

    }

}
