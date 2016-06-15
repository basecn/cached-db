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
package org.kv.cached.db.server.model.metas;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.kv.cached.db.server.CachedDbException;
import org.kv.cached.db.server.CachedDbRuntimeException;
import org.kv.cached.db.server.boot.CachedDbServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.util.internal.StringUtil;

/**
 * 元数据存储封装（ZooKeeper）
 * 
 */
public final class MetaStore {

	// logger
	private static final Logger LOG = LoggerFactory.getLogger(MetaStore.class);

	/**
	 * ZK的命名空间，固定为kv-cluster
	 */
	public static final String NAME_SPACE = "kv-cluster";

	/**
	 * ZK根目录
	 */
	public final String ROOT;

	// server handler
	// private final KvClusterServer kvServer;

	// Zk handler
	private static CuratorFramework client = null;

	// 启动标记
	private boolean started = false;

	// jackson mapper
	private final static ObjectMapper objectMapper = new ObjectMapper();


	static {
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		// objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
		// objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES,true);
		// objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,a false);
	}

	private static final Map<String, MetaStore> storeMap = new ConcurrentHashMap<>();


	/**
	 * 取MetsaStore实例
	 * 
	 * @param clusterName
	 * @return
	 */
	public static MetaStore getMetaStore(String clusterName) {
		if (storeMap.containsKey(clusterName)) {
			return storeMap.get(clusterName);
		}
		throw new CachedDbRuntimeException("No MetaStore for " + clusterName);
	}


	/**
	 * 构造
	 * 
	 * @param server
	 */
	public MetaStore(String clusterName, CachedDbServer server) {
		if (StringUtil.isNullOrEmpty(clusterName)) {
			throw new CachedDbRuntimeException("Null or empty cluster name");
		}
		// this.kvServer = server;
		this.ROOT = "/" + clusterName;

		synchronized (MetaStore.class) {
			if (storeMap.containsKey(clusterName)) {
				LOG.warn("Already init MetaStore for cluster {}", clusterName);
			} else {
				storeMap.put(clusterName, this);
				LOG.info("Create MetaStore for cluster [{}]", clusterName);
			}
		}
	}


	/**
	 * 启动元数据存储
	 * 
	 * @throws CachedDbException
	 */
	public final synchronized void start() throws CachedDbException {
		if (started)
			return;

		client = CuratorFrameworkFactory.builder()//
				.connectString("")//
				.sessionTimeoutMs(Integer.getInteger("curator-default-session-timeout", 60 * 1000))//
				.connectionTimeoutMs(Integer.getInteger("curator-default-connection-timeout", 15 * 1000))//
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))//
				.namespace("kv-cluster")//
				.build();
		client.start();

		started = true;
	}


	/**
	 * 创建节点
	 * 
	 * @param node
	 * @param properties
	 * @param updateDate
	 * @return
	 * @throws KvException
	 */
	public String createNode(String node, Map<String, Object> properties, boolean updateDate) throws CachedDbException {

		if (null == node) {
			throw new CachedDbException("Node's name is null");
		}
		String nodeName = node.trim();
		// check node's name
		if (StringUtil.isNullOrEmpty(nodeName)) {
			throw new CachedDbException("Node's name is null or  empty");
		}

		// TODO 检查格式
		if (!node.startsWith("/")) {
			nodeName = "/" + nodeName;
		}

		try {
			String v = (null == properties || properties.isEmpty()) ? "" : objectMapper.writeValueAsString(properties);
			LOG.info(" CREATE NODE: {}, DATAS: {}", nodeName, v);
			String nn = client.create().forPath(nodeName, v.getBytes());
			return nn;
		} catch (Exception e) {
			LOG.error("Create Node error", e);
			throw new CachedDbException(e);
		}
	}


	/**
	 * 删除节点
	 * 
	 * @param node
	 * @param properties
	 * @return
	 * @throws KvException
	 */
	public String deleteNode(String node) throws CachedDbException {
		// check node's name
		if (StringUtil.isNullOrEmpty(node)) {
			throw new CachedDbException("Node's name is null or  empty");
		}

		try {
			return deleteNode(node, client);
		} catch (Exception e) {
			LOG.error("Delete Node error", e);
			throw new CachedDbException(e);
		}
	}


	/**
	 * 迭代删除节点及子节点
	 * 
	 * @param node
	 * @param client
	 * @return
	 * @throws Exception
	 */
	private static final String deleteNode(String node, CuratorFramework client) throws Exception {
		List<String> nodes = client.getChildren().forPath(node);
		for (String s : nodes) {
			LOG.info("=======Delete: {}", s);
			deleteNode(s, client);
		}
		client.delete().forPath(node);
		return node;
	}


	/**
	 * 使节点失效
	 * 
	 * @param node
	 * @param properties
	 * @return
	 * @throws KvException
	 */
	public String disableNode(String node, Map<String, Object> properties) throws CachedDbException {

		final String key = "disabled";
		Map<String, Object> ps = (null == properties) ? new HashMap<String, Object>() : properties;
		ps.put(key, true);

		return updateNode(node, properties);
	}


	/**
	 * 更新节点信息
	 * 
	 * @param node
	 * @param properties
	 * @return
	 * @throws KvException
	 */
	public String updateNode(String node, Map<String, Object> properties) throws CachedDbException {
		// check node's name
		if (StringUtil.isNullOrEmpty(node)) {
			throw new CachedDbException("Node's name is null or  empty");
		}

		if (null == properties || properties.isEmpty()) {
			LOG.warn("Null properties for updating node " + node);
			return node;
		}

		try {
			byte[] d = client.getData().forPath(node);
			LOG.info("--------------{}", new String(d));

			@SuppressWarnings("unchecked")
			Map<String, Object> m = objectMapper.readValue(new String(d), Map.class);
			m.putAll(properties);

			String newV = objectMapper.writeValueAsString(m);
			System.out.println(newV);
			Stat s = client.setData().forPath(node, newV.getBytes());
			if (null == s) {
				throw new CachedDbException("fdsafds");
			}
			return node;
		} catch (Exception e) {
			LOG.error("Update Node error", e);
			throw new CachedDbException(e);
		}

	}


	public boolean ready() {
		return true;
	}


	public void touchNode(String node) {

	}


	public static void main(String[] node) {
		Map<String, String> m = new HashMap<String, String>();
		m.put("a", "1");
		m.put("a", "b");

	}

}
