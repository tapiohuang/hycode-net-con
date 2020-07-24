package org.hycode.net;


import io.netty.channel.Channel;

import java.util.HashSet;

public class NetSessionFactory {
    private final ThreadLocal<NetSession> threadLocal = new ThreadLocal<>();
    private final HashSet<NetSession> waitingSessionSet = new HashSet<>();

    public ThreadLocal<NetSession> getThreadLocal() {
        return threadLocal;
    }

    public NetSession getSession() {
        NetSession netSession = threadLocal.get();
        if (netSession == null || !netSession.isOpen()) {
            netSession = this.openSession();
            threadLocal.set(netSession);
        }
        return netSession;
    }

    private NetSession openSession() {
        return new NetSession();
    }

    private void registerWaitingSession(NetSession netSession) {

    }

    private void removeWaitingSession(NetSession netSession) {

    }


    public HashSet<NetSession> getWaitingSessionSet() {
        return waitingSessionSet;
    }
}
