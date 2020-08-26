package com.github.shyiko.mysql.binlog.network.protocol.encode;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAEncryptor {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] encrypt(byte[] publicKey, byte[] content, String padding) throws Exception {

        String pubKeyStr = new String(publicKey);
        pubKeyStr = pubKeyStr.replaceAll("-----BEGIN PUBLIC KEY-----", "");
        pubKeyStr = pubKeyStr.replaceAll("-----END PUBLIC KEY-----", "");
        pubKeyStr = pubKeyStr.replaceAll("\n", "");
        publicKey = Base64.getDecoder().decode(pubKeyStr);

//        publicKey = getPublicKey();

        X509EncodedKeySpec encodedKey = new X509EncodedKeySpec(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(encodedKey);
        int maxBlockSize = pubKey.getModulus().bitLength() / 8 - 11;

        //RSA加密
        // https://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html#Cipher
        // 8.0.5之前版本 RSA_PKCS1_PADDING            PKCS1Padding
        // 8.0.5以及之后版本 RSA_PKCS1_OAEP_PADDING   OAEPPadding
        // RSA/None/PKCS1Padding
        // RSA/None/OAEPPadding
        Cipher cipher = Cipher.getInstance(padding);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        int inputLen = content.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlockSize) {
                cache = cipher.doFinal(content, offSet, maxBlockSize);
            } else {
                cache = cipher.doFinal(content, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlockSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    private static byte[] getPublicKey() throws IOException {
        String path = "D:\\Program Files\\mysql-8.0.18-winx64\\data\\public_key.pem";
        InputStream inputStream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String readLine = null;
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
            }
        }

        String pubKeyStr = sb.toString();
        pubKeyStr = pubKeyStr.replaceAll("-----BEGIN PUBLIC KEY-----", "");
        pubKeyStr = pubKeyStr.replaceAll("-----END PUBLIC KEY-----", "");
        pubKeyStr = pubKeyStr.replaceAll("\n", "");
        byte[] publicKey = Base64.getDecoder().decode(pubKeyStr);
        return publicKey;
    }
}
