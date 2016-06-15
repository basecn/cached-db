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
import java.util.Map;

import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kv.cached.db.server.CachedDbException;
import org.kv.cached.db.server.model.AbstractMetaTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class MetaStoreTest extends AbstractMetaTest {

	// logger
	private static final Logger LOG = LoggerFactory.getLogger(MetaStoreTest.class);

	// Node 1
	private String node1 = "/core-cluster/dbs/_testDb_";


	public MetaStoreTest() {
		super("core-cluster");
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
		super.tearDown();
		Stat e = getClient().checkExists().forPath(node1);
		if (e != null) {
			getClient().delete().forPath(node1);
		}
	}


	/**
	 * 测试输入异常
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateNodeWithException() throws Exception {
		Map<String, Object> ps = new HashMap<String, Object>();

		try {
			getMetaStore().createNode(null, ps, true);
			Assert.fail();
		} catch (Exception e) {
			LOG.info("Expected msg", e);
		}

		try {
			getMetaStore().createNode("", ps, true);
			Assert.fail();
		} catch (Exception e) {
			LOG.info("Expected msg", e);
		}

		try {
			getMetaStore().createNode(node1, null, true);
		} catch (Exception e) {
			LOG.info("error", e);
			Assert.fail();
		}
	}


	@Test
	public void testCreateNodeWithoutProperties() throws Exception {

		try {
			// create
			String nn = getMetaStore().createNode(node1, null, true);
			Assert.assertNotNull(nn);
			// check
			Stat s = getClient().checkExists().forPath(node1);
			Assert.assertTrue(s != null);
		} catch (Exception e) {
			Assert.fail();
		}

	}


	@Test
	public void testCreateNodeWithEmpytProperties() throws Exception {
		Map<String, Object> ps = new HashMap<String, Object>();
		try {
			String nn = getMetaStore().createNode(node1, ps, true);
			Assert.assertNotNull(nn);

			Stat s = getClient().checkExists().forPath(node1);
			Assert.assertTrue(s != null);
		} catch (Exception e) {
			Assert.fail();
		}
	}


	@Test
	public void testCreateNodeWithProperties() throws Exception {
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("name", "kv-cluster-test");
		ps.put("memo", "test");

		String nn = getMetaStore().createNode(node1, ps, true);
		Assert.assertNotNull(nn);

		Stat s = getClient().checkExists().forPath(node1);
		Assert.assertTrue(s != null);

		byte[] ds = getClient().getData().forPath(node1);
		Assert.assertNotNull(ds);
		Assert.assertTrue(0 < ds.length);

		Map<?, ?> m = getObjectmapper().readValue(ds, Map.class);
		Assert.assertEquals("kv-cluster-test", m.get("name"));
	}


	@Test
	public void testDeleteNodeWithNullOrEmptyName() throws Exception {
		MetaStore ms = getMetaStore();

		try {
			ms.deleteNode(null);
			Assert.fail();
		} catch (Exception e) {
		}

		try {
			ms.deleteNode("");
			Assert.fail();
		} catch (Exception e) {
		}
	}


	@Test
	public void testDeleteNode() throws Exception {
		getMetaStore().createNode(node1, null, true);
		getMetaStore().deleteNode(node1);

		Stat s = getClient().checkExists().forPath(node1);
		Assert.assertNull(s);
	}


	@Test
	public void testDeleteNodeNotExist() {
		String nd = "/_not_exist_node_";
		MetaStore ms = getMetaStore();

		try {
			ms.deleteNode(nd);
			Assert.fail();
		} catch (Exception e) {
			LOG.info("", e);
		}
	}


	@Test
	public void testUpdateNodeWithNullProperties() throws CachedDbException {
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("name", "kv-cluster-test");
		ps.put("memo", "test");
		getMetaStore().createNode(node1, ps, true);

		getMetaStore().updateNode(node1, null);

		getMetaStore().updateNode(node1, new HashMap<String, Object>());
	}


	@Test
	public void testUpdateNodeNotExistNode() throws CachedDbException {
		String nd = "/_not_exist_node_";

		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("name", "kv-cluster-test");
		ps.put("memo", "test");
		try {
			getMetaStore().updateNode(nd, ps);
			Assert.fail();
		} catch (Exception e) {
			LOG.info("", e);
		}
	}


	@Test
	public void testUpdateNode() throws Exception {
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("name", "kv-cluster-test");
		ps.put("memo", "test");
		getMetaStore().createNode(node1, ps, true);

		ps.put("name", "test_name");
		ps.put("max_length", 123);
		getMetaStore().updateNode(node1, ps);

		byte[] ds = getClient().getData().forPath(node1);
		Assert.assertNotNull(ds);

		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = getObjectmapper().readValue(ds, Map.class);

		Assert.assertEquals("test_name", dataMap.get("name"));
		Assert.assertEquals("test", dataMap.get("memo"));
		Assert.assertEquals(123, dataMap.get("max_length"));
	}

}
