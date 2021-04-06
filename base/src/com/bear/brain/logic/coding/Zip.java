package com.bear.brain.logic.coding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class Zip {

    public static byte[] unzip(byte[] bytes) throws IOException {
        int bufferSize = 1024 * 10;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        InflaterInputStream inputStream = new InflaterInputStream(byteArrayInputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bufferSize);
        try {
            byte[] buffer = new byte[bufferSize];
            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } finally {
            inputStream.close();
            byteArrayInputStream.close();
            byteArrayOutputStream.close();
        }
    }

    public static byte[] zip(byte[] bytes) throws IOException {
        int bufferSize = 1024 * 10;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bufferSize);
        DeflaterOutputStream outputStream = new DeflaterOutputStream(byteArrayOutputStream);
        try {
            outputStream.write(bytes);
            outputStream.close();
            byteArrayOutputStream.close();
            CRC32 crc32 = new CRC32();
            crc32.update(byteArrayOutputStream.toByteArray());
            //System.out.println("CRC32 = " + crc32.getValue());
            return byteArrayOutputStream.toByteArray();

        } finally {
            outputStream.close();
            byteArrayOutputStream.close();
        }
    }
}