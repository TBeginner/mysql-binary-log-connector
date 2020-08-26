package com.github.shyiko.mysql.binlog.network.protocol.encode;

/**
 * 合并字节数组
 */
public class UnionEncryptor {

    /**
     * 合并字节数组
     * @param a 字节数组a
     * @param b 字节数组b
     * @return 复制数组a，并从尾部复制数组b的全部内容
     */
    public static byte[] union(byte[] a, byte[] b) {
        byte[] r = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }
}
