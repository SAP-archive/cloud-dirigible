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

package com.sap.dirigible.repository.db;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.WeakHashMap;

import com.sap.dirigible.repository.logging.Logger;

public class SimpleCacheManager {
	
	private static final Logger logger = Logger.getLogger(SimpleCacheManager.class);
	
	private static final long MAX_EXPIRATION_TIME = 10000; 

    private Map<String, ExpirationWrapper> cache = Collections.synchronizedMap(new WeakHashMap<String, ExpirationWrapper>());
    
    private boolean disabled = true;

    public SimpleCacheManager(boolean disableCache) {
    	this.disabled = disableCache;
    }

    public void put(String cacheKey, Object value) {
    	if (!isDisabled() && value != null && (cache.get(cacheKey) == null)) {
    		logger.debug("put: " + cacheKey + " value: " + value);
	    	ExpirationWrapper wrapper = new ExpirationWrapper(value, GregorianCalendar.getInstance().getTime().getTime());
	        cache.put(cacheKey, wrapper);
    	}
    }
    
    public boolean isDisabled() {
		return disabled;
	}
    
    public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

    public Object get(String cacheKey) {
    	ExpirationWrapper wrapper = cache.get(cacheKey);
    	if (wrapper != null) {
	    	if ((GregorianCalendar.getInstance().getTime().getTime() - wrapper.getCreated()) < MAX_EXPIRATION_TIME) {
	    		logger.debug("cache used for: " + cacheKey);
	    		return wrapper.value;
	    	} else {
	    		logger.debug("cache expired for: " + cacheKey);
	    		clear(cacheKey);
	    	}
    	}
        return null;
    }

    public void clear(String cacheKey) {
        cache.remove(cacheKey);
    }

    public void clear() {
        cache.clear();
    }

//    public static SimpleCacheManager getInstance(boolean disableCache) {
//        if (instance == null) {
//            synchronized (monitor) {
//                if (instance == null) {
//                    instance = new SimpleCacheManager(disableCache);
//                }
//            }
//        }
//        return instance;
//    }
    
    class ExpirationWrapper {
    	
    	private Object value; 
    	private long created;
    	
    	ExpirationWrapper(Object value, long created) {
    		this.value = value;
    		this.created = created;
    	}
    	
    	public Object getValue() {
			return value;
		}
    	
    	public long getCreated() {
			return created;
		}
    	
    	
    }

}