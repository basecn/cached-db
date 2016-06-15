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
package org.kv.cached.db.server.app;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.StringUtil;

/**
 * KV集群的初始化器
 * 
 */
public final class KvClusterInitiator {

	private static final Logger LOG = LoggerFactory.getLogger(KvClusterInitiator.class);

	public CommandLine cmd;


	/**
	 * 
	 */
	public KvClusterInitiator(CommandLine cmd) {
		this.cmd = cmd;
	}


	public void connectMetaStore(String connectString) throws Exception {

		CuratorFramework client = CuratorFrameworkFactory.builder()//
				.connectString(connectString)//
				.sessionTimeoutMs(Integer.getInteger("curator-default-session-timeout", 60 * 1000))//
				.connectionTimeoutMs(Integer.getInteger("curator-default-connection-timeout", 15 * 1000))//
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))//
				.namespace("kv-cluster")//
				.build();
		client.start();

		String cNode = "/" + cmd.getOptionValue("c");

		LOG.info("Check ROOT node: {}", "kv-cluster");
		Stat s = client.checkExists().forPath(cNode);
		if (s != null) {
			LOG.warn(" Cluster Node already exist.");
			LOG.info("You can DROP the cluster if want to create a NEW cluster");
			System.exit(0);
		}

		// 当前时间
		LocalDateTime ldt = LocalDateTime.now();
		String dt = ldt.toString();

		// 创建新的集群节点
		String s1 = client.create().forPath(cNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}   S1=[{}]", cNode, s1);

		// 用户信息
		String usrsNode = cNode + "/users";
		s1 = client.create().forPath(usrsNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}", usrsNode, dt);

		// 用户-权限信息
		String priviegesNode = usrsNode + "/privileges";
		s1 = client.create().forPath(priviegesNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}", priviegesNode, dt);

		// 数据库
		String dbsNode = cNode + "/dbs";
		s1 = client.create().forPath(dbsNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}", dbsNode, dt);

		// 数据库-表
		String tbsNode = dbsNode + "/tbs";
		s1 = client.create().forPath(tbsNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}", tbsNode, dt);

		// 服务器注册信息
		String serverNode = cNode + "/server";
		s1 = client.create().forPath(serverNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}", serverNode, dt);

		// 服务实例信息
		String sisNode = serverNode + "/sis";
		s1 = client.create().forPath(sisNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}", sisNode, dt);

		// 资源锁信息
		String locksNode = cNode + "/locks";
		s1 = client.create().forPath(locksNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}", locksNode, dt);

		// 任务信息
		String tasksNode = cNode + "/tasks";
		s1 = client.create().forPath(tasksNode, dt.getBytes());
		LOG.info(" CREATE NODE: {}, DATES: {}", tasksNode, dt);
	}


	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// log4j-slf4j, default: log4j.properties

		// show start window
		loadfileAndShow("kvClusterInitiator.txt");

		// parse cmd line
		CommandLine cmd = parseCmdLine(args);
		if (null == cmd) {
			System.exit(1);
		}

		// Init Logger
		if (cmd.hasOption("lc")) {
			String v = cmd.getOptionValue("lc");
			if (!StringUtil.isNullOrEmpty(v)) {
				LOG.info("Loading logger config [{}]", v);
				PropertyConfigurator.configure(v);
			}
		}

		// 输出 启动参数
		Arrays.stream(cmd.getOptions()).forEach((Option e) -> {
			String k = e.getOpt();
			String v = cmd.getOptionValue(k);
			if (cmd.hasOption(k) && !StringUtil.isNullOrEmpty(v)) {
				LOG.info("-{}=[{}] (-{})", k, v);
			}
		});

		KvClusterInitiator kci = new KvClusterInitiator(cmd);
		kci.connectMetaStore("127.0.0.1:2181");
	}


	/**
	 * 分析命令行
	 * 
	 * @param args
	 * @return
	 */
	public static final CommandLine parseCmdLine(String[] args) {
		Options options = new Options();
		options.addOption("c", "cluster-name", true, "New cluster's name");
		options.addOption("u", "admin", true, "Administrator's username");
		options.addOption("p", "pwd", true, "Administrator's password");
		options.addOption("a", "action", true, "Init or Remove");
		options.addOption("lc", "logger-config", true, "Config Logger's Implementation.");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			return cmd;
		} catch (ParseException e) {
			LOG.error("Params error", e);
			LOG.error("");

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ant", options);
		}
		return null;
	}


	/**
	 * 加载并显示文件内容
	 * 
	 * @param file classpath内的文件名
	 */
	public static final void loadfileAndShow(String file) {
		byte[] buf = new byte[1024];
		int len = 0;
		StringBuilder sb = new StringBuilder();
		try (InputStream is = KvClusterInitiator.class.getClassLoader().getResourceAsStream(file);) {
			while (true) {
				len = is.read(buf);
				if (-1 == len)
					break;
				sb.append(new String(buf, 0, len));
			}
			LOG.info(sb.toString());
		} catch (IOException e) {
			LOG.error("Load content from file [{}] faild.", file);
			LOG.error("IOException", e);
		}
	}
}
