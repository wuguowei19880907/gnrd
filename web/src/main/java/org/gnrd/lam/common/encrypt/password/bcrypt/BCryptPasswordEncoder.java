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

package org.gnrd.lam.common.encrypt.password.bcrypt;

import lombok.extern.slf4j.Slf4j;
import org.gnrd.lam.common.encrypt.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.regex.Pattern;

@Slf4j
public class BCryptPasswordEncoder implements PasswordEncoder {

	private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

	private final int strength;

	private final BCryptVersion version;

	private final SecureRandom random;

	public BCryptPasswordEncoder() {
		this(-1);
	}

	/**
	 * @param strength
	 *            the log rounds to use, between 4 and 31
	 */
	public BCryptPasswordEncoder(int strength) {
		this(strength, null);
	}

	/**
	 * @param version
	 *            the version of bcrypt, can be 2a,2b,2y
	 */
	public BCryptPasswordEncoder(BCryptVersion version) {
		this(version, null);
	}

	/**
	 * @param version
	 *            the version of bcrypt, can be 2a,2b,2y
	 * @param random
	 *            the secure random instance to use
	 */
	public BCryptPasswordEncoder(BCryptVersion version, SecureRandom random) {
		this(version, -1, random);
	}

	/**
	 * @param strength
	 *            the log rounds to use, between 4 and 31
	 * @param random
	 *            the secure random instance to use
	 */
	public BCryptPasswordEncoder(int strength, SecureRandom random) {
		this(BCryptVersion.$2A, strength, random);
	}

	/**
	 * @param version
	 *            the version of bcrypt, can be 2a,2b,2y
	 * @param strength
	 *            the log rounds to use, between 4 and 31
	 */
	public BCryptPasswordEncoder(BCryptVersion version, int strength) {
		this(version, strength, null);
	}

	/**
	 * @param version
	 *            the version of bcrypt, can be 2a,2b,2y
	 * @param strength
	 *            the log rounds to use, between 4 and 31
	 * @param random
	 *            the secure random instance to use
	 */
	public BCryptPasswordEncoder(BCryptVersion version, int strength, SecureRandom random) {
		if (strength != -1 && (strength < BCrypt.MIN_LOG_ROUNDS || strength > BCrypt.MAX_LOG_ROUNDS)) {
			throw new IllegalArgumentException("Bad strength");
		}
		this.version = version;
		this.strength = (strength == -1) ? 10 : strength;
		this.random = random;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		if (rawPassword == null) {
			throw new IllegalArgumentException("rawPassword cannot be null");
		}
		String salt = getSalt();
		return BCrypt.hashpw(rawPassword.toString(), salt);
	}

	private String getSalt() {
		if (this.random != null) {
			return BCrypt.gensalt(this.version.getVersion(), this.strength, this.random);
		}
		return BCrypt.gensalt(this.version.getVersion(), this.strength);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (rawPassword == null) {
			throw new IllegalArgumentException("rawPassword cannot be null");
		}
		if (encodedPassword == null || encodedPassword.length() == 0) {
			log.warn("Empty encoded password");
			return false;
		}
		if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
			log.warn("Encoded password does not look like BCrypt");
			return false;
		}
		return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
	}

	/**
	 * Stores the default bcrypt version for use in configuration.
	 *
	 * @author Lin Feng
	 */
	public enum BCryptVersion {

		$2A("$2a"),

		$2Y("$2y"),

		$2B("$2b");

		private final String version;

		BCryptVersion(String version) {
			this.version = version;
		}

		public String getVersion() {
			return this.version;
		}

	}

	// public static void main(String[] args) {
	// BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	// System.out.println(bCryptPasswordEncoder.encode("123456"));
	// }
}