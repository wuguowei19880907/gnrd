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

package org.gnrd.lam.common.tools;

import com.google.common.base.Preconditions;
import org.gnrd.lam.configuration.AppProperties;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component("sessionUtil")
public class SessionUtils {

	@Resource
	private AppProperties appProperties;

	@Resource
	private RedissonClient redisson;

	public String getUUid() {
		// 分布式锁确保唯一
		RLock lock = redisson.getLock("uuidLock");
		lock.lock();
		try {
			return UUID.randomUUID().toString();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * session 存储信息
	 * 
	 * @author wuguowei
	 * @param id
	 *            session id
	 * @param info
	 *            被存储的信息
	 */
	public void storeInfo(final String id, final Object info) {
		Preconditions.checkNotNull(id, "id cannot be null");
		Preconditions.checkNotNull(info, "info cannot be null");
		RBucket<Object> bucket = redisson.getBucket(appProperties.getSessionNamespace() + ":" + id);
		bucket.set(info, appProperties.getSessionTimeout());
	}

	/**
	 * 从session中获取信息
	 * 
	 * @author wuguowei
	 * @param id
	 *            session id
	 */
	public Object getInfo(final String id) {
		Preconditions.checkNotNull(id, "id cannot be null");
		RBucket<Object> bucket = redisson.getBucket(appProperties.getSessionNamespace() + ":" + id);
		return bucket.get();
	}

	/**
	 * 延长session 中信息的过期时间
	 * 
	 * @author wuguowei
	 * @param id
	 *            session id
	 */
	public void refreshInfo(final String id) {
		Preconditions.checkNotNull(id, "id cannot be null");
		RBucket<Object> bucket = redisson.getBucket(appProperties.getSessionNamespace() + ":" + id);
		Object value = bucket.get();
		if (value != null) {
			bucket.expire(appProperties.getSessionTimeout());
		}
	}

	/**
	 * 删除 session 中信息的过期时间
	 * 
	 * @author wuguowei
	 * @param id
	 *            session id
	 */
	public void deleteInfo(final String id) {
		Preconditions.checkNotNull(id, "id cannot be null");
		RBucket<Object> bucket = redisson.getBucket(appProperties.getSessionNamespace() + ":" + id);
		bucket.delete();
	}
}
