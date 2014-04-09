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

package com.sap.dirigible.ide.repository.ui.viewer;

import java.net.URL;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sap.dirigible.ide.common.image.ImageUtils;

public class AbstractArtifactLabelProvider extends LabelProvider {

	private static final long serialVersionUID = -510389132926348760L;

	protected static final String REPOSITORY_ROOT = Messages.REPOSITORY_ROOT;

	public static final URL TYPE_PROJECT_ICON_URL = getIconURL("icon-project.png"); //$NON-NLS-1$
	public static final URL TYPE_XML_ICON_URL = getIconURL("icon-xml.png"); //$NON-NLS-1$
	public static final URL TYPE_XSL_ICON_URL = getIconURL("icon-xsl.png"); //$NON-NLS-1$
	public static final URL TYPE_WSDL_ICON_URL = getIconURL("icon-wsdl.png"); //$NON-NLS-1$
	public static final URL TYPE_HTML_ICON_URL = getIconURL("icon-html.png"); //$NON-NLS-1$
	public static final URL TYPE_TXT_ICON_URL = getIconURL("icon-txt.png"); //$NON-NLS-1$
	public static final URL TYPE_JS_ICON_URL = getIconURL("icon-js.png"); //$NON-NLS-1$
	public static final URL TYPE_JSON_ICON_URL = getIconURL("icon-json.png"); //$NON-NLS-1$
	public static final URL TYPE_XSD_ICON_URL = getIconURL("icon-xsd.png"); //$NON-NLS-1$
	public static final URL TYPE_UNKNOWN_ICON_URL = getIconURL("icon-unknown.png"); //$NON-NLS-1$
	public static final URL TYPE_COLLECTION_ICON_URL = getIconURL("icon-collection.png"); //$NON-NLS-1$
	public static final URL TYPE_JSLIB_ICON_URL = getIconURL("icon-jslib.png"); //$NON-NLS-1$
	public static final URL TYPE_ODATA_ICON_URL = getIconURL("icon-odata.png"); //$NON-NLS-1$
	public static final URL TYPE_ENTITY_ICON_URL = getIconURL("icon-entity.png"); //$NON-NLS-1$
	public static final URL TYPE_ROUTES_ICON_URL = getIconURL("icon-routes.png"); //$NON-NLS-1$
	public static final URL TYPE_WS_ICON_URL = getIconURL("icon-ws.png"); //$NON-NLS-1$
	public static final URL TYPE_MENU_ICON_URL = getIconURL("icon-menu.png"); //$NON-NLS-1$
	public static final URL TYPE_TABLE_ICON_URL = getIconURL("icon-table.png"); //$NON-NLS-1$
	public static final URL TYPE_VIEW_ICON_URL = getIconURL("icon-view.png"); //$NON-NLS-1$
	public static final URL TYPE_PICTURE_ICON_URL = getIconURL("icon-picture.png"); //$NON-NLS-1$
	public static final URL TYPE_REPOSITORY_ROOT_ICON_URL = getIconURL("icon-repository.png"); //$NON-NLS-1$
	public static final URL TYPE_ACCESS_ICON_URL = getIconURL("icon-access.png"); //$NON-NLS-1$
	public static final URL TYPE_DSV_ICON_URL = getIconURL("icon-dsv.png"); //$NON-NLS-1$
	public static final URL TYPE_RUBY_ICON_URL = getIconURL("icon-ruby.png"); //$NON-NLS-1$
	public static final URL TYPE_GROOVY_ICON_URL = getIconURL("icon-groovy.png"); //$NON-NLS-1$
	public static final URL TYPE_WIKI_ICON_URL = getIconURL("icon-wiki.png"); //$NON-NLS-1$
	public static final URL TYPE_BATCH_ICON_URL = getIconURL("icon-batch.png"); //$NON-NLS-1$
	public static final URL TYPE_EXTENSION_POINT_ICON_URL = getIconURL("icon-extension-point.png"); //$NON-NLS-1$
	public static final URL TYPE_EXTENSION_ICON_URL = getIconURL("icon-extension.png"); //$NON-NLS-1$

	public static URL getIconURL(String iconName) {
		URL url = ImageUtils.getIconURL("com.sap.dirigible.ide.repository.ui", "/resources/icons/", iconName);
		return url;
	}

	protected Image createImage(URL imageURL) {
		return ImageUtils.createImage(imageURL);
	}

	public AbstractArtifactLabelProvider() {
		super();
	}

	protected Image getResourceImage(String resourceName) {

		if (resourceName.endsWith("xml")) { //$NON-NLS-1$
			return createImage(TYPE_XML_ICON_URL);
		}
		if (resourceName.endsWith("xsl")) { //$NON-NLS-1$
			return createImage(TYPE_XSL_ICON_URL);
		}
		if (resourceName.endsWith("wsdl")) { //$NON-NLS-1$
			return createImage(TYPE_WSDL_ICON_URL);
		}
		if (resourceName.endsWith("txt") //$NON-NLS-1$
				|| resourceName.endsWith("properties")) { //$NON-NLS-1$
			return createImage(TYPE_TXT_ICON_URL);
		}
		if (resourceName.endsWith("js")) { //$NON-NLS-1$
			return createImage(TYPE_JS_ICON_URL);
		}
		if (resourceName.endsWith("json")) { //$NON-NLS-1$
			return createImage(TYPE_JSON_ICON_URL);
		}
		if (resourceName.endsWith("xsd")) { //$NON-NLS-1$
			return createImage(TYPE_XSD_ICON_URL);
		}
		if (resourceName.endsWith("html")) { //$NON-NLS-1$
			return createImage(TYPE_HTML_ICON_URL);
		}
		if (resourceName.endsWith("jslib")) { //$NON-NLS-1$
			return createImage(TYPE_JSLIB_ICON_URL);
		}
		if (resourceName.endsWith("odata")) { //$NON-NLS-1$
			return createImage(TYPE_ODATA_ICON_URL);
		}
		if (resourceName.endsWith("entity")) { //$NON-NLS-1$
			return createImage(TYPE_ENTITY_ICON_URL);
		}
		if (resourceName.endsWith("routes")) { //$NON-NLS-1$
			return createImage(TYPE_ROUTES_ICON_URL);
		}
		if (resourceName.endsWith("ws")) { //$NON-NLS-1$
			return createImage(TYPE_WS_ICON_URL);
		}
		if (resourceName.endsWith("menu")) { //$NON-NLS-1$
			return createImage(TYPE_MENU_ICON_URL);
		}
		if (resourceName.endsWith("table")) { //$NON-NLS-1$
			return createImage(TYPE_TABLE_ICON_URL);
		}
		if (resourceName.endsWith("view")) { //$NON-NLS-1$
			return createImage(TYPE_VIEW_ICON_URL);
		}
		if (resourceName.endsWith("css")) { //$NON-NLS-1$
			return createImage(TYPE_TXT_ICON_URL);
		}
		if (resourceName.endsWith("png") //$NON-NLS-1$
				|| resourceName.endsWith("jpg") //$NON-NLS-1$
				|| resourceName.endsWith("jpeg") //$NON-NLS-1$
				|| resourceName.endsWith("gif") //$NON-NLS-1$
				|| resourceName.endsWith("bmp")) { //$NON-NLS-1$
			return createImage(TYPE_PICTURE_ICON_URL);
		}
		if (resourceName.endsWith("access")) { //$NON-NLS-1$
			return createImage(TYPE_ACCESS_ICON_URL);
		}
		if (resourceName.endsWith("dsv")) { //$NON-NLS-1$
			return createImage(TYPE_DSV_ICON_URL);
		}
		if (resourceName.endsWith("rb")) { //$NON-NLS-1$
			return createImage(TYPE_RUBY_ICON_URL);
		}
		if (resourceName.endsWith("groovy")) { //$NON-NLS-1$
			return createImage(TYPE_GROOVY_ICON_URL);
		}
		if (resourceName.endsWith("wiki")) { //$NON-NLS-1$
			return createImage(TYPE_WIKI_ICON_URL);
		}
		if (resourceName.endsWith("wikis")) { //$NON-NLS-1$
			return createImage(TYPE_BATCH_ICON_URL);
		}
		if (resourceName.endsWith("extensionpoint")) { //$NON-NLS-1$
			return createImage(TYPE_EXTENSION_POINT_ICON_URL);
		}
		if (resourceName.endsWith("extension")) { //$NON-NLS-1$
			return createImage(TYPE_EXTENSION_ICON_URL);
		}

		return createImage(TYPE_UNKNOWN_ICON_URL);
	}


	@Override
	public void dispose() {
		super.dispose();
	}

}