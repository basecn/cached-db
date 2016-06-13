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
package org.kv.cached.db.server.boot;


import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.kv.cached.db.server.CachedDbException;
import org.kv.cached.db.server.model.metas.MetaStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class CachedDbServer {

	// logger
	private static final Logger LOG = LoggerFactory.getLogger(CachedDbServer.class);

	/**
	 * 命令行
	 */
	private final CommandLine cmd;

	/**
	 * 配置参数
	 */
	private final Configuration config;


	/**
	 * 构造
	 * 
	 * @param cmd
	 * @param config
	 */
	public CachedDbServer(CommandLine cmd, Configuration config) {
		this.cmd = cmd;
		this.config = config;
	}


	/**
	 * 启动服务
	 */
	public synchronized void start() {
		String cn = config.getString("kv-cluster-name");
		// meta
		MetaStore ms = new MetaStore(cn, this);
		ms.start();
		if (!ms.ready()) {
			LOG.error("ZK no ready");
		}

		Properties p = new Properties();
		p.put("name", "cms-vsop");
		p.put("disabled", "false");

		Properties p1 = new Properties();
		p.put("name1", "cms-vsop");
		p.put("disabled1", "false");

		MapConfiguration mCfg = new MapConfiguration(p1);
		String node = "/core-cluster/dbs/ctyun";
		try {
			// ms.createNode(node, mCfg.getMap(), true);
			ms.disableNode(node, mCfg.getMap());
		} catch (CachedDbException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the cmd
	 */
	public final CommandLine getCmd() {
		return cmd;
	}


	/**
	 * @return the config
	 */
	public final Configuration getConfig() {
		return config;
	}

}
