package com.github.shyiko.mysql.binlog.network.protocol.command.plugin;

import java.io.IOException;

public interface AuthPlugin {

    byte[] toByteArray() throws IOException;
}
