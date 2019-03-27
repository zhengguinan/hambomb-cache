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
package org.hambomb.cache.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hambomb.cache.CacheUtils;
import org.hambomb.cache.context.HanmbombRuntimeException;
import org.hambomb.cache.handler.annotation.AfterUpdateProcess;
import org.hambomb.cache.loader.CacheObjectLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: <a herf="mailto:jarodchao@126.com>jarod </a>
 * @date: 2019-03-26
 */
@Aspect
@Component
public class AfterUpdateProcessInterceptor extends AbstractCacheLoaderProcessInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(AfterUpdateProcessInterceptor.class);

    @Around("@annotation(org.hambomb.cache.handler.annotation.AfterUpdateProcess)")
    public Object afterUpdateServiceProcess(ProceedingJoinPoint joinPoint) throws Throwable {

        log(joinPoint);

        Object object = invokeProcess(joinPoint);

        try {
            updateCacheObject(joinPoint);
        } catch (HanmbombRuntimeException e) {
            LOG.warn("If a runtime exception occurs, processing is skipped.");
        }

        return object;

    }

    private void updateCacheObject(ProceedingJoinPoint joinPoint) {
        Object[] argValue = joinPoint.getArgs();

        if (argValue.length > 1) {
            throw new RuntimeException("不支持！");
        }

        InterceptorMetaData metaData = getInterceptorAnnotation(joinPoint);

        if (metaData == null) {
            LOG.warn("Skip HambombCache related processing.");
            return;
        }

        AfterUpdateProcess afterUpdateProcess = (AfterUpdateProcess) metaData.methodAnnotation;

        String entityLoaderName = afterUpdateProcess.cacheObjectClass() == Object.class ?
                argValue[0].getClass().getSimpleName() : afterUpdateProcess.cacheObjectClass().getSimpleName();

        CacheObjectLoader cacheObjectLoader = processor.getEntityLoader(entityLoaderName);

        String id;

        if (afterUpdateProcess.byPrimaryKey()) {

            if (argValue[0].getClass() == cacheObjectLoader.cacheObjectClazz) {

                id = cacheObjectLoader.getPkey(argValue[0], null);
            }else {
                List<Method> pkGetters = cacheObjectLoader.buildGetters(cacheObjectLoader.pk, argValue[0].getClass());

                id = cacheObjectLoader.getPkey(argValue[0], pkGetters);
            }


        } else {

            String[] args = afterUpdateProcess.attrs();

            String[] values = cacheObjectLoader.getEntityCacheKey(argValue[0], args);

            String cacheKey = cacheObjectLoader.indexRepository.toCacheKey(cacheObjectLoader.cacheObjectClassName, values);

            id = (String) cacheObjectLoader.cacheHandler.get(cacheKey);
        }

        Object cacheObject = cacheObjectLoader.cacheHandler.get(id);

        BeanUtils.copyProperties(argValue[0], cacheObject, CacheUtils.getNullPropertyNames(argValue[0]));

        cacheObjectLoader.cacheHandler.update(id, cacheObject);

    }

    @Override
    Class getMethodAnnotation() {
        return AfterUpdateProcess.class;
    }

    @Override
    String getLoaderName(InterceptorMetaData metaData) {
        return null;
    }

    @Override
    String getCacheKey(ProceedingJoinPoint joinPoint, InterceptorMetaData metaData) {
        return null;
    }
}
