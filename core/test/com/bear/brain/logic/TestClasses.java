package com.bear.brain.logic;

import com.badlogic.gdx.utils.Base64Coder;
import com.bear.brain.logic.coding.AESCrypto;
import com.bear.brain.logic.coding.Zip;

import org.junit.Test;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class TestClasses {
    @Test
    public void name() throws ClassNotFoundException, IOException, BadPaddingException, IllegalBlockSizeException {

        String string = "test string";

        Class.forName("com.badlogic.gdx.utils.Base64Coder");
        Class.forName("java.util.Base64");

        System.out.println(Base64.getEncoder().encodeToString(AESCrypto.encrypt.doFinal(Zip.zip(string.getBytes(AESCrypto.utf8)))));
        System.out.println(Base64Coder.encode(AESCrypto.encrypt.doFinal(Zip.zip(string.getBytes(AESCrypto.utf8)))));

    }
}
