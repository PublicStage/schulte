package com.bear.brain.logic.coding;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto {
    public static final Charset utf8 = Charset.forName("UTF-8");
    public static final Cipher encrypt;
    public static final Cipher decrypt;
    private static final String keyString = "xxxxxxx";

    static {
        byte[] keyBytes = keyString.getBytes(utf8);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        String cipherString = "AES";
        Cipher tmpEncrypt = null;
        Cipher tmpDecrypt = null;
        try {
            tmpEncrypt = Cipher.getInstance(cipherString);
            tmpDecrypt = Cipher.getInstance(cipherString);
            tmpEncrypt.init(Cipher.ENCRYPT_MODE, key);
            tmpDecrypt.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        encrypt = tmpEncrypt;
        decrypt = tmpDecrypt;
    }
}
