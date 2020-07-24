package org.hycode.net.server.alive.checker;

import org.hycode.net.NetAuthData;

public class HeartCheckerWithAuthData extends NetAuthData {
    private long checkTime;
    private long responseTime;
    private int status;

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTime) {
        this.checkTime = checkTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
