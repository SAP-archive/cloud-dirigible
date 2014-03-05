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

package com.sap.dirigible.runtime.metrics;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class TimeUtils {
	
	private static DateTime dateTimeCeiling(DateTime dt, Period p) {
	    if (p.getYears() != 0) {
	        return dt.yearOfEra().roundCeilingCopy().minusYears(dt.getYearOfEra() % p.getYears());
	    } else if (p.getMonths() != 0) {
	        return dt.monthOfYear().roundCeilingCopy().minusMonths((dt.getMonthOfYear() - 1) % p.getMonths());
	    } else if (p.getWeeks() != 0) {
	        return dt.weekOfWeekyear().roundCeilingCopy().minusWeeks((dt.getWeekOfWeekyear() - 1) % p.getWeeks());
	    } else if (p.getDays() != 0) {
	        return dt.dayOfMonth().roundCeilingCopy().minusDays((dt.getDayOfMonth() - 1) % p.getDays());
	    } else if (p.getHours() != 0) {
	        return dt.hourOfDay().roundCeilingCopy().minusHours(dt.getHourOfDay() % p.getHours());
	    } else if (p.getMinutes() != 0) {
	        return dt.minuteOfHour().roundCeilingCopy().minusMinutes(dt.getMinuteOfHour() % p.getMinutes());
	    } else if (p.getSeconds() != 0) {
	        return dt.secondOfMinute().roundCeilingCopy().minusSeconds(dt.getSecondOfMinute() % p.getSeconds());
	    }
	    return dt.millisOfSecond().roundCeilingCopy().minusMillis(dt.getMillisOfSecond() % p.getMillis());
	}
	
	public static Date roundCeilingHour(Date date) {
		DateTime bound = dateTimeCeiling(new DateTime(date.getTime()), Period.hours(1));
		return new Date(bound.getMillis());
	}

}
