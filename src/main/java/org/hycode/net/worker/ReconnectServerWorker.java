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
                        netClient.connect0();
                        object.wait(10000);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    public void notifyWorker() {
        synchronized (object) {
            object.notify();
        }
    }

}
