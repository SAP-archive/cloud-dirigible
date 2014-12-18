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

package com.sap.dirigible.ide.ui.widgets.connection.spline;

import org.eclipse.swt.graphics.GC;

public class SplineRenderer {

	private final int precision;

	public SplineRenderer(int precision) {
		this.precision = precision;
	}

	public void renderSpline(GC gc, Spline spline) {
		for (int i = 1; i <= precision; ++i) {
			final float startAmount = i / (float) precision;
			final float endAmount = (i - 1) / (float) precision;

			final int startX = spline.getX(startAmount);
			final int startY = spline.getY(startAmount);

			final int endX = spline.getX(endAmount);
			final int endY = spline.getY(endAmount);

			gc.drawLine(startX, startY, endX, endY);
		}
	}

}
