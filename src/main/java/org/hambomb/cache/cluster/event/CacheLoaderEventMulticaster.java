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
package org.hambomb.cache.cluster.event;

import org.hambomb.cache.cluster.listener.CacheLoaderEventListener;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: <a herf="mailto:jarodchao@126.com>jarod </a>
 * @date: 2019-03-04
 */
public class CacheLoaderEventMulticaster {


    private Map<CacheLoaderEvent, Set<CacheLoaderEventListener>> listeners = new LinkedHashMap<>();

    public void addListener(CacheLoaderEvent event, CacheLoaderEventListener listener) {


        Set<CacheLoaderEventListener> loaderEventListenerSet = listeners.get(event);

        if (loaderEventListenerSet != null) {
            loaderEventListenerSet.add(listener);
        } else {
            loaderEventListenerSet = new LinkedHashSet<>(16);
            loaderEventListenerSet.add(listener);
            this.listeners.put(event, loaderEventListenerSet);
        }

    }

    public void publishEvent(CacheLoaderEvent event) {

        listeners.get(event).stream().forEach(listener -> listener.onApplicationEvent(event));

    }


}
