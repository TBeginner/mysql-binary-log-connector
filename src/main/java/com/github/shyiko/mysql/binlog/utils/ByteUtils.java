package com.github.shyiko.mysql.binlog.utils;

import java.util.Map;

/**
 * Byte数组和字符串的相互转换工具类
 * @author chentiefeng
 *
 */
public class ByteUtils {

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813
     * @param byteArr
     * @return 转换后的字符串
     * @throws Exception
     */
    public static String byteArrToHexStr(byte[] byteArr) {
        int iLen = byteArr.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = byteArr[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组
     * @param hexStr  需要转换的字符串
     * @return 转换后的byte数组
     */
    public static byte[] hexStrToByteArr(String hexStr) throws Exception {
        byte[] arrB = hexStr.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    public static byte[] toBytes(int number){
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (number & 0xFF);
        bytes[1] = (byte) ((number >> 8) & 0xFF);
        bytes[2] = (byte) ((number >> 16)& 0xFF);
        bytes[3] = (byte) ((number >> 24)& 0xFF);
        return bytes;
    }
}
