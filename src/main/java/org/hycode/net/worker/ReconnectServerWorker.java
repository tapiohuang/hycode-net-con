package org.hycode.net.worker;

import org.hycode.net.NetClient;
import org.hycode.net.Worker;

public class ReconnectServerWorker extends Worker {
    private final NetClient netClient;

    public ReconnectServerWorker(NetClient netClient) {
        this.netClient = netClient;
    }


    @Override
    protected void work() {
        synchronized (object) {
            while (true) {
                try {
                    if (netClient.isConnect()) {
                        object.wait();
                    } else {
                        if (netClient.isReconnect()) {
                            netClient.connect0();
                            object.wait(10000);
                        } else if (netClient.getReconnectTimes() == 0) {
                            netClient.connect0();
                            break;
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        if (!netClient.isConnect()) {
            throw new RuntimeException("无法连接服务器!");
        }
    }

    public void notifyWorker() {
        synchronized (object) {
            object.notify();
        }
    }

}
