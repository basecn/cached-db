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


import java.util.Map;

import org.kv.cached.db.server.CachedDbException;

/**
 *
 */
public interface MetaOperator {

	/**
	 * 返回对象对应MetaStore的路径
	 * 
	 * @return
	 */
	String getPath();


	/**
	 * 返回对象所有属性定义
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();


	/**
	 * 设置对象属性
	 * 
	 * @param ps
	 */
	void setProperties(Map<String, Object> ps);


	/**
	 * 对象是否已同步<br>
	 * 
	 * 不同步的情况：<br>
	 * <ul>
	 * <li>初始对象</li>
	 * <li>对象被改变</li>
	 * </ul>
	 * 
	 * @return
	 */
	boolean isSynchronized();


	/**
	 * 设置对象的同步状态
	 * 
	 * @param syn
	 */
	void setSynchronized(boolean syn);


	/**
	 * 同步对象和元数据
	 * 
	 * @return
	 * @throws KvException 冲突时
	 */
	boolean synchronizeWithMeta() throws CachedDbException;
}
