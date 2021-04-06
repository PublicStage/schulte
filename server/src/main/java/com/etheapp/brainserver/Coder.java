package com.etheapp.brainserver;

import com.bear.brain.logic.coding.AESCrypto;
import com.bear.brain.logic.coding.Zip;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Coder {
    public static String encode(String string) throws IOException, BadPaddingException, IllegalBlockSizeException {
        return Base64.getEncoder().encodeToString(AESCrypto.encrypt.doFinal(Zip.zip(string.getBytes(AESCrypto.utf8))));
    }

    public static String decode(String string) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] bytes = Zip.unzip(AESCrypto.decrypt.doFinal(Base64.getDecoder().decode(string)));
        return new String(bytes, "utf-8");
    }

}
