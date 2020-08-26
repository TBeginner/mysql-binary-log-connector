package com.github.shyiko.mysql.binlog.network.protocol.command;

public abstract class AbstractAuthCommand implements Command{

    public abstract String getPassword();

    public abstract String getSalt();

    public abstract void setSalt(String salt);
}
