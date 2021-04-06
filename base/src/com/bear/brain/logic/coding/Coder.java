package com.bear.brain.logic.coding;

import com.badlogic.gdx.utils.Base64Coder;
import com.google.gson.Gson;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Coder {

    public static String encode(Object state) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] b1 = new Gson().toJson(state).getBytes(AESCrypto.utf8);
        b1 = AESCrypto.encrypt.doFinal(Zip.zip(b1));
        return String.valueOf(Base64Coder.encode(b1));
    }

    public static <T> T decode(Class<T> type, String string) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] b2 = Base64Coder.decode(string);
        b2 = AESCrypto.decrypt.doFinal(b2);
        return new Gson().fromJson(new String(b2, "utf-8"), type);
    }

    public static String decode(String string) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] b2 = Base64Coder.decode(string);
        b2 = AESCrypto.decrypt.doFinal(b2);
        b2 = Zip.unzip(b2);
        return new String(b2, "utf-8");
    }
}
