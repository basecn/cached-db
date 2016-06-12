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
package org.kv.cached.db.server.model.reslock;


/**
 * 元数据对象的抽象<br>
 * 包括“基本属性操作方法”、“基本动作操作方法”的定义
 * 
 */
public interface MetaObject {

	/**
	 * 设置元数据属性
	 * 
	 * @param name 名称
	 * @param property 值
	 */
	void setProperty(String name, Object property);


	/**
	 * 取元数据属性
	 * 
	 * @param name
	 * @return
	 */
	Object getProperty(String name);


	// ===================== 动作类操作 =====================

	/**
	 * 当前对象的元数据信息是否被修改且未提交
	 * 
	 * @return the dirty
	 */
	boolean isDirty();


	/**
	 * @param dirty the dirty to set
	 */
	void setDirty(boolean dirty);


	/**
	 * 检测元数据对象是否存在
	 * 
	 * @return the exist
	 */
	boolean isExist();


	/**
	 * @param exist the exist to set
	 */
	void setExist(boolean exist);

}
