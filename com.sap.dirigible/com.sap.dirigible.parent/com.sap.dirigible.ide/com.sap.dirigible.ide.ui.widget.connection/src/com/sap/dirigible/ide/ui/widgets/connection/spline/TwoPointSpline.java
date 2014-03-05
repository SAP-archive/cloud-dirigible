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

public class TwoPointSpline implements Spline {

	private final int x1;

	private final int x2;

	private final int y1;

	private final int y2;

	public TwoPointSpline(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public int getX(float amount) {
		amount = fixAmount(amount);
		return x1 + (int) ((x2 - x1) * amount);
	}

	@Override
	public int getY(float amount) {
		amount = fixAmount(amount);
		if (amount < 0.5f) {
			return y1 + getOffset(amount);
		} else {
			return y2 - getOffset(1.0f - amount);
		}
	}

	private float fixAmount(float amount) {
		float result = amount;
		if (result < 0.0f) {
			result = 0.0f;
		}
		if (result > 1.0f) {
			result = 1.0f;
		}
		return result;
	}

	private int getOffset(float amount) {
		if (y2 == y1) {
			return 0;
		}
		final float coef = (y2 - y1) / (2.0f * 0.5f * 0.5f);
		return (int) (coef * amount * amount);
	}

}
