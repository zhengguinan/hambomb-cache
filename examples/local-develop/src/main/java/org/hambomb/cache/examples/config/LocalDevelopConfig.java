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
package org.hambomb.cache.examples.config;

import org.hambomb.cache.HambombCache;
import org.hambomb.cache.context.CacheServerStrategy;
import org.hambomb.cache.context.HambombCacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: <a herf="mailto:jarodchao@126.com>jarod </a>
 * @date: 2019-03-09
 */
@Configuration
@ComponentScan(basePackages = {"org.hambomb.cache"})
public class LocalDevelopConfig {

    @Bean
    public HambombCacheConfiguration hambombCacheConfig() {
        HambombCacheConfiguration hambombCacheConfiguration = new HambombCacheConfiguration();
        hambombCacheConfiguration.addScanPackageName("org.hambomb.cache.examples.mapper");
        hambombCacheConfiguration.addCacheServerStrategy(CacheServerStrategy.STANDALONE);

        return hambombCacheConfiguration;
    }

    @Bean
    @Autowired
    public HambombCache hambombCache(HambombCacheConfiguration hambombCacheConfiguration) {

        HambombCache hambombCache = new HambombCache(hambombCacheConfiguration);
        return hambombCache;
    }
}
