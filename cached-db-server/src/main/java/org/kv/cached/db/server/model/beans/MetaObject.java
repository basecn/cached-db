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
 * 元数据对象基本信息封装
 * 
 */
public class MetaObject {

	/**
	 * 创建人
	 */
	private String creater;

	/**
	 * 最后更新人
	 */
	private String updater;

	/**
	 * 对象标识
	 */
	private String id;

	/**
	 * 对象名称（显示用）
	 */
	private String name;

	/**
	 * 短名称，用于生成K1键
	 */
	private String shortName;

	/**
	 * 备注
	 */
	private String comment;

	/**
	 * 禁用状态
	 */
	private boolean disabled;


	/**
	 * @param id
	 * @param name
	 */
	public MetaObject(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}


	/**
	 * @return the creater
	 */
	public final String getCreater() {
		return creater;
	}


	/**
	 * @param creater the creater to set
	 */
	public final void setCreater(String creater) {
		this.creater = creater;
	}


	/**
	 * @return the updater
	 */
	public final String getUpdater() {
		return updater;
	}


	/**
	 * @param updater the updater to set
	 */
	public final void setUpdater(String updater) {
		this.updater = updater;
	}


	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the shortName
	 */
	public final String getShortName() {
		return shortName;
	}


	/**
	 * @param shortName the shortName to set
	 */
	public final void setShortName(String shortName) {
		this.shortName = shortName;
	}


	/**
	 * @return the comment
	 */
	public final String getComment() {
		return comment;
	}


	/**
	 * @param comment the comment to set
	 */
	public final void setComment(String comment) {
		this.comment = comment;
	}


	/**
	 * @return the disabled
	 */
	public final boolean isDisabled() {
		return disabled;
	}


	/**
	 * @param disabled the disabled to set
	 */
	public final void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
