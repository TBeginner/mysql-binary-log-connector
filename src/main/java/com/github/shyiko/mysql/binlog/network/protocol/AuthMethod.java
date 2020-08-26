package com.github.shyiko.mysql.binlog.network.protocol;

import com.github.shyiko.mysql.binlog.network.protocol.command.plugin.AuthPlugin;
import com.github.shyiko.mysql.binlog.network.protocol.command.plugin.CachingSHA2PasswordPlugin;
import com.github.shyiko.mysql.binlog.network.protocol.command.plugin.MysqlNativePasswordPlugin;

public enum AuthMethod {

    CACHING_SHA2_PASSWORD("caching_sha2_password"),
    MYSQL_NATIVE_PASSWORD("mysql_native_password")
    ;

    AuthMethod(String value) {
        this.value = value;
    }

    private String value;

    public String getValue(){
        return this.value;
    }

    public static AuthMethod convert(String value) {
        for (AuthMethod item : AuthMethod.values()) {
            if(item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

    public static AuthPlugin getAuthPlugin(AuthMethod authMethod, byte[] salt, byte[] password) {
        switch (authMethod) {
            case MYSQL_NATIVE_PASSWORD:
                return new MysqlNativePasswordPlugin(salt, password);
            case CACHING_SHA2_PASSWORD:
                return new CachingSHA2PasswordPlugin(salt, password);
        }
        return null;
    }

    public static AuthPlugin getAuthPlugin(String authMethodStr, byte[] salt, byte[] password) {
        AuthMethod authMethod = convert(authMethodStr);
        switch (authMethod) {
            case MYSQL_NATIVE_PASSWORD:
                return new MysqlNativePasswordPlugin(salt, password);
            case CACHING_SHA2_PASSWORD:
                return new CachingSHA2PasswordPlugin(salt, password);
        }
        return null;
    }
}
