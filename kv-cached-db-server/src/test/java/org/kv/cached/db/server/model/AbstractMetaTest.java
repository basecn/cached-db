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
package org.kv.cached.db.server.model;


import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.kv.cached.db.server.boot.CachedDbServer;
import org.kv.cached.db.server.model.metas.MetaStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * MetaStore测试类的父类
 * 
 */
public abstract class AbstractMetaTest {

	// logger
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMetaTest.class);

	// cli
	private static CommandLine cmd;

	// config
	private static Configuration config;

	// Zk handler
	private static CuratorFramework client = null;

	// jackson mapper
	private final static ObjectMapper objectMapper = new ObjectMapper();

	// server
	private CachedDbServer server;

	// meta
	private MetaStore metaStore;


	static {
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		// objectMapper.configure(JsonParser.Feature.INTERN_FIELD_NAMES, true);
		// objectMapper.configure(JsonParser.Feature.CANONICALIZE_FIELD_NAMES,
		// true);
		// objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,a
		// false);
	}

	final String clusterName;


	public AbstractMetaTest(String clusterName) {
		this.clusterName = clusterName;
	}


	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		String[] cmds = {};

		Options options = new Options();
		options.addOption("c", "cluster-name", true, "New cluster's name");
		options.addOption("u", "admin", true, "Administrator's username");
		options.addOption("p", "pwd", true, "Administrator's password");
		options.addOption("a", "action", true, "Init or Remove");
		options.addOption("lc", "logger-config", true, "Config Logger's Implementation.");

		CommandLineParser parser = new DefaultParser();
		cmd = parser.parse(options, cmds);

		// config
		config = new MapConfiguration(new HashMap<String, Object>());

		// zk
		client = CuratorFrameworkFactory.builder()//
				.connectString("")//
				.sessionTimeoutMs(Integer.getInteger("curator-default-session-timeout", 60 * 1000))//
				.connectionTimeoutMs(Integer.getInteger("curator-default-connection-timeout", 15 * 1000))//
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))//
				.namespace("kv-cluster")//
				.build();
		client.start();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (null != client)
			client.close();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// config
		config.addProperty("kv-cluster-name", clusterName);

		// server
		server = new CachedDbServer(cmd, config);
		String cn = config.getString("kv-cluster-name");

		// meta
		metaStore = new MetaStore(cn, server);
		metaStore.start();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		server = null;
		metaStore = null;
	}


	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return LOG;
	}


	/**
	 * @return the cmd
	 */
	public static CommandLine getCmd() {
		return cmd;
	}


	/**
	 * @return the config
	 */
	public static Configuration getConfig() {
		return config;
	}


	/**
	 * @return the server
	 */
	public CachedDbServer getServer() {
		return server;
	}


	/**
	 * @return the metaStore
	 */
	public MetaStore getMetaStore() {
		return metaStore;
	}


	/**
	 * @return the client
	 */
	public static CuratorFramework getClient() {
		return client;
	}


	/**
	 * @return the objectmapper
	 */
	public static ObjectMapper getObjectmapper() {
		return objectMapper;
	}

}
