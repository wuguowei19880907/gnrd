package org.gnrd.lam.common.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncryptionExample {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // 假设这是从上一步中获取到的公钥和私钥的 base64 编码形式
        String publicKeyBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4xg8XEY89wr4P9DQGRdeRldLk7PsnHt6zgvm+J6CNteN733llu8OpVTJAXLhVXg4Ntj2e+KEghHd+CyCOry8u0iLhnH4j/Zrdc1+xYRkBvt2Zcxb20O7XLWJoltbNZ1uHo/TcOhDOgX1MY3UvKoxvDAp0Wj5Eq8GK0NbHXRZENmrQ1O1OuDH5uJ5MEB7ceF8Mc0PwV5skk0EfrPL6S5dGUulc7QhopynBLX/iKGOJ/LXyXPp5XpaVuh7dNu/rAAegXufRK4q8zmfUrmTrrOddSUMmNBAFOZmrbmiLmoE950HgI5LVg9eLCBRVp4gwQPaEQYt+AT323urNJ4xEkFpewIDAQAB";
        String privateKeyBase64 = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDjGDxcRjz3Cvg/0NAZF15GV0uTs+yce3rOC+b4noI2143vfeWW7w6lVMkBcuFVeDg22PZ74oSCEd34LII6vLy7SIuGcfiP9mt1zX7FhGQG+3ZlzFvbQ7tctYmiW1s1nW4ej9Nw6EM6BfUxjdS8qjG8MCnRaPkSrwYrQ1sddFkQ2atDU7U64Mfm4nkwQHtx4XwxzQ/BXmySTQR+s8vpLl0ZS6VztCGinKcEtf+IoY4n8tfJc+nlelpW6Ht027+sAB6Be59ErirzOZ9SuZOus511JQyY0EAU5matuaIuagT3nQeAjktWD14sIFFWniDBA9oRBi34BPfbe6s0njESQWl7AgMBAAECggEADnUko2oNuvT8U/4kjKX9TwFP776hkcUv6GRylzs3WZ8RS4ENqxvCDqPrF65rV3uLwwlYbw+loi0M0oSgfKidlvDDgGyXrf0HePYugj88KdBvIh3HohoNGHxh0q/G16itPsENhx+KTbkP+jbBp4pp5MWBZaV6TrmzOb4UIJ5sUyzhr6eHQmqXFaMuWCrroIACSIRsnEJjWlWYuTHKB8Vo+JmpvP3givvJfWrkh12krpKRW3cLk6AIZT6cvSO+5PtRY02jpeeoY6l5czCTCOQaWR+VSfTgg72t3kdM68mBnIafJzzNRg6WaBxhg7IJBoBtooMs2m4T3VNjfD56Udmm4QKBgQD5VztNLtKT5uLFkp17YR1fpT6vJ6qZuSsMhlVGckaSP2uQ0KQtMOzY66INI2RJARitcmjRY45c99tfOoxXDEttnt/Kus8Se1qU7RP2evSjRY9ETWUCJT3zJArLfgeiYG607+BaONevSBUhVmH/O8VewStiazJoCpYhehFFJjDZmwKBgQDpKOfJSHI4n+dOozKkwK/QzVrZFpayWiOUKtjN7fjx+lMwsTKzAMm2LkJqdJFrGzQefv+3sz8F4HPnGgZqj6EoIAs6GBJ7gnORHvS7g79E91sGsTmAZaK+msj7nt198ptng9PyGsDa7tfipLYv7lVHnChdY6sfLsN1qbdjlcYdoQKBgQDxENdyvew1tqpVHnVHjhMaH+dE7PLSAOaQC2hy3ZVBXDOhJTc1wfGbn+VjEm6o7nhvaQfSOZCwbaoYncCFgIuEhnATzxzY344qIjSEv0e6e0wSW0lnE56bbqNJLqYu5VDjem/MitxsySI0wxGn9J/UBqrtUnq/gEy6PQxpcI0WNQKBgQDeq/c5Dxwuod/pBQjvuSnvsr8phl4jNNy3u3fUq4nNpvDeFszBkWYSu/kn6+2ZKaDL4yPaz6/HsAVg10T7bfJN/NRcBfNYy0wNc0/s6TS467aeHJE641XazL5XN/B2QUVTRd4bYJuAOwwoYn+wJOCDBEq0beRWYnW/IHeI/tf3IQKBgQDeeYIZoHo7Fy8uTqAfG2qvN47andswrQ6KQ0JElmXyzmA8wvcQfgYXPj3dCagoSRa1pzDY+7Ni99jOLPNFxLSd01PhKR4RFG6gTZiOnPH39MSW8MtJ4sAOPW9nJErHtEZA6curIjfahzaMpFy86zML6smxdefMHJ5rAGU8zf8+qg==";

        // 将 base64 编码的公钥和私钥转换为字节数组
        byte[] publicKeyBytes = java.util.Base64.getDecoder().decode(publicKeyBase64);
        byte[] privateKeyBytes = java.util.Base64.getDecoder().decode(privateKeyBase64);

        // 创建公钥和私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        // 加密
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        String plaintext = "Hello, 张三3!";
        byte[] encryptedText = encryptCipher.doFinal(plaintext.getBytes());

        System.out.println("加密后(base64编码): " + java.util.Base64.getEncoder().encodeToString(encryptedText));

        // 解密
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedText = decryptCipher.doFinal(encryptedText);

        System.out.println("解密后: " + new String(decryptedText));
    }
}
