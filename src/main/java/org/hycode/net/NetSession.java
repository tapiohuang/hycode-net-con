package org.hycode.net;


import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.HashSet;

public class NetSession {
    private final HashMap<Object, DataPackage> dataPackageHashMap = new HashMap<>();
    private final boolean open;
    private final HashSet<Long> waitSessionIdSet = new HashSet<>();

    public NetSession() {
        this.open = true;
    }

    public boolean isOpen() {
        return open;
    }

    public void sendDataPackage(Channel channel, NetData netData, boolean wait) {
        DataPackage dataPackage = DataPackage.warp(netData);
        if (wait) {
            dataPackage.setSessionId(0L);
        }
        channel.writeAndFlush(dataPackage);
    }

    public void addDataPackage(DataPackage dataPackage) {

    }

    public boolean onData(DataPackage dataPackage) {
        return false;
    }
}
