package org.gnrd.lam.common.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component("rsaUtil")
public class RSAUtil {

    private final byte[] pubKey;

    public RSAUtil(@Value("${rsa.private-key}") String key) {
        Security.addProvider(new BouncyCastleProvider());
        this.pubKey = Base64.getDecoder().decode(key);
    }

    public String decrypt(String txt) throws Exception {
        // 创建私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(pubKey));
        // 解密
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedText = decryptCipher.doFinal(Base64.getDecoder().decode(txt));
        return new String(decryptedText, StandardCharsets.UTF_8);
    }
}
