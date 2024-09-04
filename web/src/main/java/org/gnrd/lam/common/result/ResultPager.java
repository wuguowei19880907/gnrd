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

package org.gnrd.lam.common.result;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果数据结构
 *
 * @author WUGUOWEI 2024年08月28日
 */
public class ResultPager<T> implements Serializable {

	/**
	 * 查询结果
	 * 
	 * @since v1.0.0
	 */
	private List<T> content;
	/**
	 * 查询参数 number，从1开始
	 * 
	 * @mock 0
	 * @since v1.0.0
	 */
	private int number;
	/**
	 * 查询参数 size
	 * 
	 * @mock 10
	 * @since v1.0.0
	 */
	private int size;
	/**
	 * 此页实际有多少条记录
	 * 
	 * @mock 10
	 * @since v1.0.0
	 */
	private int numberOfElements;
	/**
	 * 总记录数
	 * 
	 * @mock 100
	 * @since v1.0.0
	 */
	private long totalElements;

	/**
	 * 总分页数
	 * 
	 * @mock 10
	 * @since v1.0.0
	 */
	private int totalPages;
	/**
	 * 是否为最后一页
	 * 
	 * @mock false
	 * @since v1.0.0
	 */
	private boolean last;
	/**
	 * 是否为第一页
	 * 
	 * @mock true
	 * @since v1.0.0
	 */
	private boolean first;

	public ResultPager(Page<T> page) {
		super();
		this.content = page.getContent();
		this.number = page.getNumber();
		this.numberOfElements = page.getNumberOfElements();
		this.size = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.last = page.isLast();
		this.first = page.isFirst();
	}

	public static <S> ResultPager<S> of(final Page<S> page) {
		return new ResultPager<>(page);
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getNumber() {
		return number + 1;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

}
