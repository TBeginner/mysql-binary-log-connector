package com.github.shyiko.mysql.binlog.network.protocol.command.plugin;

import com.github.shyiko.mysql.binlog.network.protocol.command.Command;
import com.github.shyiko.mysql.binlog.network.protocol.encode.UnionEncryptor;
import com.github.shyiko.mysql.binlog.network.protocol.encode.XOREncryptor;
import com.github.shyiko.mysql.binlog.utils.ByteUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * https://dev.mysql.com/doc/dev/mysql-server/latest/page_caching_sha2_authentication_exchanges.html
 *
 * 加密-XOR（SHA256（密码），SHA256（SHA256（SHA256（密码）），随机数））
 */
public class CachingSHA2PasswordPlugin implements AuthPlugin {

    private final byte[] salt;
    private final byte[] password;

    public CachingSHA2PasswordPlugin(byte[] scramble, byte[] password) {
        this.salt = scramble;
        this.password = password;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return passwordCompatibleWithMySQL8();
    }

    /**
     * <p>caching_sha2_password算法</p>
     * <p><b>XOR（SHA256（密码），SHA256（SHA256（SHA256（密码）），随机数））</b></p>
     * @return
     */
    protected byte[] passwordCompatibleWithMySQL8() {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] first = sha.digest(password);
        byte[] second = XOREncryptor.xor(first, sha.digest(UnionEncryptor.union(sha.digest(first), salt)));
        return second;
    }
}
