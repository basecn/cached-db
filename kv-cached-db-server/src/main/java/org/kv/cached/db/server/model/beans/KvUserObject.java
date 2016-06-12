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
package org.kv.cached.db.server.model.beans;


/**
 *
 */
public class KvUserObject {

	private String hashedPassword;

	private String pwdEncryption = "md5";


	/**
	 * 
	 */
	public KvUserObject() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the hashedPassword
	 */
	public final String getHashedPassword() {
		return hashedPassword;
	}


	/**
	 * @param hashedPassword the hashedPassword to set
	 */
	public final void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}


	/**
	 * @return the pwdEncryption
	 */
	public final String getPwdEncryption() {
		return pwdEncryption;
	}


	/**
	 * @param pwdEncryption the pwdEncryption to set
	 */
	public final void setPwdEncryption(String pwdEncryption) {
		this.pwdEncryption = pwdEncryption;
	}

}
