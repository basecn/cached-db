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


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.kv.cached.db.server.CachedDbException;
import org.kv.cached.db.server.app.KvClusterInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KvServer启动类
 * 
 */
public class BootKvServer {

	// logger
	private static final Logger LOG = LoggerFactory.getLogger(BootKvServer.class);


	/**
	 * 启动方法
	 * 
	 * @param args
	 * @throws CachedDbException
	 */
	public static void main(String[] args) throws CachedDbException {

		// log4j-slf4j, default: log4j.properties

		// 1. 显示欢迎窗口
		final String key = "cached-db.welcome";
		String welcome = "cached-db.welcome-info.txt";
		if (System.getProperties().contains(key)) {
			welcome = (String) System.getProperties().get(key);
		}
		loadfileAndShow(welcome);

		// 2. 创建命令行参数定义
		Options options = buildCliOptions();
		// 输出命令行参数的完全定义和帮助信息
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("[-lsp]", options);
		System.out.println("");

		// 3. 解析命令行
		final CommandLine cmd;
		// try {
		try {
			cmd = new DefaultParser().parse(options, args);
			// 输出传入的命令行参数
			Stream<Option> stream = Arrays.stream(cmd.getOptions());
			Map<String, String> realArgs = stream
					.collect(Collectors.toMap(Option::getOpt, (Option a) -> cmd.getOptionValue(a.getOpt())));
			realArgs.entrySet().stream()//
					.sorted(Comparator.comparing(a -> a.hashCode()))
					.forEach((Map.Entry<String, String> e) -> LOG.info("{} -> {}", e.getKey(), e.getValue()));
		} catch (ParseException e2) {
			throw new CachedDbException(e2);
		}

		// 4. 加载配置参数
		CompositeConfiguration config = null;
		try {
			config = loadConfiguration(cmd);
		} catch (ConfigurationException e1) {
			throw new CachedDbException("Load configuration error.", e1);
		} finally {
			Optional<CompositeConfiguration> nullable = Optional.ofNullable(config);
			nullable.ifPresent(e -> {
				LOG.info("[Application's configuration] ({} items)", e.getNumberOfConfigurations());
				Iterator<String> keys = e.getKeys();
				keys.forEachRemaining(k -> LOG.info(" | {}:[{}]", k, e.getProperty(k)));
			});
		}

		// 5. 加载自定义目志配置文件
		if (cmd.hasOption("lc")) {
			String v = cmd.getOptionValue("lc");
			Optional.ofNullable(v).ifPresent(e -> {
				LOG.info("Loading logger config [{}]", v);
				PropertyConfigurator.configure(v);
			});
		}

		// 启动服务
		CachedDbServer server = new CachedDbServer(cmd, config);
		server.start();

	}


	/**
	 * 加载配置文件
	 * 
	 * @param cmd
	 * @throws ConfigurationException
	 */
	private static final CompositeConfiguration loadConfiguration(CommandLine cmd) throws ConfigurationException {
		CompositeConfiguration config = new CompositeConfiguration();

		if (cmd.hasOption("load_system_properties")) {
			LOG.debug("Loading system properties as application's configurations.");
			config.addConfiguration(new SystemConfiguration());
		}

		if (cmd.hasOption("app_config")) {
			config.addConfiguration(new PropertiesConfiguration(new File(cmd.getOptionValue("app-config"))));
		} else {
			config.addConfiguration(new PropertiesConfiguration("application.properties"));
		}

		LOG.debug("meta_store.zk.connect_string: {}", config.getString("meta_store.zk.connect_string"));
		return config;
	}


	/**
	 * 建立命令行参数配置
	 *
	 * @return
	 */
	private static final Options buildCliOptions() {
		Options options = new Options();
		options.addOption("c", "cluster-name", true, "New cluster's name");
		options.addOption("u", "admin", true, "Administrator's username");
		options.addOption("p", "pwd", true, "Administrator's password");
		options.addOption("a", "action", true, "Init or Remove");
		options.addOption("lc", "logger_config", true, "Config Logger's Implementation.");
		options.addOption("conf", "app_config", true, "Config Logger's Implementation.");
		options.addOption("lsp", "load_system_properties", false, "Loading system properties as app's configurations.");
		return options;
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
