package org.hycode.net.test;

import org.hycode.net.NetServer;
import org.hycode.net.NetSessionFilter;
import org.hycode.net.processors.HeartCheckProcessor;
import org.hycode.net.schedules.HeartCheckSchedule;

public class ServerTest {
    public static void main(String[] args) {
        NetServer netServer = new NetServer();
        netServer.processor(new HeartCheckProcessor(netServer));
        netServer.filter(new NetSessionFilter(netServer));
        HeartCheckSchedule heartCheckSchedule = new HeartCheckSchedule(netServer);
        heartCheckSchedule.cycleTime(10);
        netServer.schedule(heartCheckSchedule);
        netServer.port(10056).bind();
    }
}
