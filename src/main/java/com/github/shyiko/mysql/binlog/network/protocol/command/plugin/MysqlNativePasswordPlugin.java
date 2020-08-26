/*
 * Copyright 2013 Stanley Shyiko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shyiko.mysql.binlog.network.protocol.command.plugin;

import com.github.shyiko.mysql.binlog.network.protocol.command.Command;
import com.github.shyiko.mysql.binlog.network.protocol.encode.UnionEncryptor;
import com.github.shyiko.mysql.binlog.network.protocol.encode.XOREncryptor;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="mailto:ben.osheroff@gmail.com">Ben Osheroff</a>
 */
public class MysqlNativePasswordPlugin implements AuthPlugin {
    private final byte[] scramble, password;

    public MysqlNativePasswordPlugin(byte[] scramble, byte[] password) {
        this.scramble = scramble;
        this.password = password;
    }
    @Override
    public byte[] toByteArray() throws IOException {
        return passwordCompatibleWithMySQL411();
    }

    /**
     * <p>mysql_native_password加密，算法过程如下：</p>
     * <p><b>xor( sha1( password ), sha1( union( salt, sha1( sha1(password) ) )</b></p>
     * <p>see mysql/sql/password.c scramble(...)</p>
     */
    private byte[] passwordCompatibleWithMySQL411() {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] passwordHash = sha.digest(password);
        return XOREncryptor.xor(passwordHash, sha.digest(UnionEncryptor.union(scramble, sha.digest(passwordHash))));
    }
}
