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


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class KvClusterImplTest extends AbstractMetaTest {

	// logger
	private static Logger LOG = LoggerFactory.getLogger(KvClusterImplTest.class);

	String nCluster1 = "ctyun_core-cluster";

	String nCluster2 = "ctyun_asia_cluster";


	public KvClusterImplTest() {
		super("ctyun_core-cluster");
	}


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		// KvClusterImpl kc = new KvClusterImpl(nCluster1);
		//
		// List<String> nodeStack = new ArrayList<String>();
		// String currentNode = kc.getPath();
		//
		// nodeStack.add(currentNode);
		// do {
		// String n = nodeStack.get(nodeStack.size() - 1);
		// // 以栈的方式实现递归删除节点
		// List<String> children = getClient().getChildren().forPath(n);
		// if (0 == children.size()) {
		// LOG.info("Remove Node {} ", n);
		// getClient().delete().forPath(n);
		// nodeStack.remove(nodeStack.size() - 1);
		// } else {
		// nodeStack.addAll(children);
		// }
		// } while (nodeStack.size() == 0);

		super.tearDown();
	}


	@Test
	public void testInit() throws Exception {

		// KvClusterImpl kc = new KvClusterImpl(nCluster1);
		// Assert.assertFalse(kc.isExist());
		//
		// kc.create();
		// Assert.assertTrue(kc.isExist());
		//
		// Stat s = getClient().checkExists().forPath(kc.getPath());
		// Assert.assertNotNull(s);
		//
		// byte[] ds = getClient().getData().forPath(kc.getPath());
		// @SuppressWarnings("unchecked")
		// Map<String, Object> data = getObjectmapper().readValue(ds,
		// Map.class);
		//
		// LOG.info("data: {}", new String(ds));
		//
		// Assert.assertEquals(nCluster1, data.get("name"));
		// Assert.assertEquals(nCluster1, data.get("id"));

	}

}
