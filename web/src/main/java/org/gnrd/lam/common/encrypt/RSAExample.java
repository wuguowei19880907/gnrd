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

package org.gnrd.lam.common.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;

public class RSAExample {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // 创建 RSA 密钥对生成器
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(2048); // 指定密钥长度

        // 生成密钥对
        KeyPair keyPair = generator.generateKeyPair();

        System.out.println("RSA 密钥对生成成功:");

        // 获取公钥和私钥
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        String publicKeyBase64 = java.util.Base64.getEncoder().encodeToString(publicKeyBytes);
        String privateKeyBase64 = java.util.Base64.getEncoder().encodeToString(privateKeyBytes);

        System.out.println("公钥(base64编码): " + publicKeyBase64);
        System.out.println("私钥(base64编码): " + privateKeyBase64);
    }
}
