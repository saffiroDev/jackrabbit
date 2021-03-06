/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.performance;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

/**
 * Performance test to check performance of queries on sub-trees.
 */
public class DescendantSearchTest extends AbstractTest {

    private static final int NODE_COUNT = 100;

    private Session session;

    private Node root;

    protected Query createQuery(QueryManager manager, int i)
            throws RepositoryException {
        return manager.createQuery("/jcr:root/testroot//element(*,nt:base)[@testcount=" + i + "]", Query.XPATH);
    }

    public void beforeSuite() throws RepositoryException {
        session = getRepository().login(getCredentials());

        root = session.getRootNode().addNode("testroot", "nt:unstructured");
        for (int i = 0; i < NODE_COUNT; i++) {
            Node node = root.addNode("node" + i, "nt:unstructured");
            for (int j = 0; j < NODE_COUNT; j++) {
                Node child = node.addNode("node" + j, "nt:unstructured");
                child.setProperty("testcount", j);
            }
            session.save();
        }
    }

    public void runTest() throws Exception {
        QueryManager manager = session.getWorkspace().getQueryManager();
        for (int i = 0; i < NODE_COUNT; i++) {
            Query query = createQuery(manager, i);
            NodeIterator iterator = query.execute().getNodes();
            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                if (node.getProperty("testcount").getLong() != i) {
                    throw new Exception("Invalid test result: " + node.getPath());
                }
            }
        }
    }

    public void afterSuite() throws RepositoryException {
        for (int i = 0; i < NODE_COUNT; i++) {
            root.getNode("node" + i).remove();
            session.save();
        }

        root.remove();
        session.save();
        session.logout();
    }

}
