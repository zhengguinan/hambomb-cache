/*
 * Copyright 2019 The  Project
 *
 * The   Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.hambomb.cache.cluster.listener;

import org.I0Itec.zkclient.ZkClient;
import org.hambomb.cache.HambombCacheProcessor;
import org.hambomb.cache.cluster.event.CacheLoaderEvent;
import org.hambomb.cache.cluster.node.CacheMasterLoaderData;
import org.hambomb.cache.cluster.node.ClusterRoot;

/**
 * @author: <a herf="mailto:jarodchao@126.com>jarod </a>
 * @date: 2019-03-01
 */
public class CacheLoadInterruptedListener  implements CacheLoaderEventListener {

    private ZkClient zkClient;

    private HambombCacheProcessor processor;

    public CacheLoadInterruptedListener(ZkClient zkClient, HambombCacheProcessor processor) {
        this.zkClient = zkClient;
        this.processor = processor;
    }

    @Override
    public void onApplicationEvent(CacheLoaderEvent event) {

        if (zkClient.exists(ClusterRoot.getMasterData())) {
            CacheMasterLoaderData data = zkClient.readData(ClusterRoot.getMasterData());

            if (data.getFlag().equals(CacheMasterLoaderData.UNFINISH_FLAG)) {
                processor.restart();
            }
        }
    }
}
