/**
 * Copyright 2016 Zhang.Jingwei (basecn@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kv.cached.db.server;


/**
 *
 */
public class CachedDbException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3966610235420062921L;


	/**
	 * 
	 */
	public CachedDbException() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param message
	 */
	public CachedDbException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param cause
	 */
	public CachedDbException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param message
	 * @param cause
	 */
	public CachedDbException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public CachedDbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
