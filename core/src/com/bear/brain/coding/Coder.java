package com.bear.brain.coding;

import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Coder {
    static Json json;

    static {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static String encode(Object state) throws IOException, BadPaddingException, IllegalBlockSizeException {
        byte[] b1 = new Json().toJson(state).getBytes(AESCrypto.utf8);
        b1 = AESCrypto.encrypt.doFinal(b1);
        return String.valueOf(Base64Coder.encode(b1));
    }

    public static String encodeZip(Object state) throws IOException, BadPaddingException, IllegalBlockSizeException {
        return String.valueOf(Base64Coder.encode(AESCrypto.encrypt.doFinal(Zip.zip(json.toJson(state).getBytes(AESCrypto.utf8)))));
    }

    public static String encodeZip(String json) throws IOException, BadPaddingException, IllegalBlockSizeException {
        return String.valueOf(Base64Coder.encode(AESCrypto.encrypt.doFinal(Zip.zip(json.getBytes(AESCrypto.utf8)))));
    }

    public static String decodeZip(String string) throws IOException, BadPaddingException, IllegalBlockSizeException {
        return new String(Zip.unzip(AESCrypto.decrypt.doFinal(Base64Coder.decode(string))), "utf-8");
    }


    public static <T> T decode(Class<T> type, String string) throws BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] b2 = Base64Coder.decode(string);
        b2 = AESCrypto.decrypt.doFinal(b2);
        return new Json().fromJson(type, new String(b2, "utf-8"));
    }
}
