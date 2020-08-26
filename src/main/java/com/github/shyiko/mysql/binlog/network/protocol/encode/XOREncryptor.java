package com.github.shyiko.mysql.binlog.network.protocol.encode;

/**
 * xor混淆算法
 */
public class XOREncryptor {

    public static byte[] xor(byte[] source, byte[] salt) {
        byte[] r = new byte[source.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (byte) (source[i] ^ salt[i % salt.length]);
        }
        return r;
    }
}
