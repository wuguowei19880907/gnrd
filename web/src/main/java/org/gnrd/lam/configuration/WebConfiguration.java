/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * Copyright 2023 gnrd
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

package org.gnrd.lam.configuration;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import org.gnrd.lam.common.constants.AuthToken;
import org.gnrd.lam.common.encrypt.password.PasswordEncoder;
import org.gnrd.lam.common.encrypt.password.bcrypt.BCryptPasswordEncoder;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("http://localhost:*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                // 解决跨域读取不出header的内容
                .allowedHeaders("*").exposedHeaders("Accept", "Origin", "X-Requested-With",
                        "Content-Type", "Last-Modified", "content-disposition")
                .allowCredentials(true);
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver(AuthToken.HEADER);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();
        config.put("login-user", new CacheConfig(24 * 60 * 1000, 12 * 60 * 1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Lazy(false)
    public CriteriaBuilderFactory cbf() {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        // do some configuration
        return config.createCriteriaBuilderFactory(entityManagerFactory);
    }
}
