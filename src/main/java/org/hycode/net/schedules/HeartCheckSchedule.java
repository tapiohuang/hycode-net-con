package org.hycode.net.schedules;

import io.netty.channel.Channel;
import org.hycode.net.NetServer;
import org.hycode.net.NetServerChannelManager;
import org.hycode.net.Schedule;
import org.hycode.net.server.alive.checker.HeartCheckerWithAuthData;

import java.util.HashSet;

public class HeartCheckSchedule extends Schedule {
    private final NetServer netServer;
    private NetServerChannelManager netServerChannelManager;

    /**
     * @param netServer NetServer
     * @param cycleTime 周期 秒
     */
    public HeartCheckSchedule(NetServer netServer, int cycleTime) {
        super(netServer.getScheduleManager());
        this.netServer = netServer;
        this.cycleTime(cycleTime);
    }

    @Override
    public void task() {
        HashSet<Channel> channels = this.netServer.getNetServerChannelManager().getChannels();
        channels.forEach((channel) -> {
            HeartCheckerWithAuthData heartCheckerWithAuthData = new HeartCheckerWithAuthData();
            heartCheckerWithAuthData.setCheckTime(System.currentTimeMillis());
            heartCheckerWithAuthData.setStatus(0);
            this.netServer.openSession().sendDataPackage(channel, heartCheckerWithAuthData, false);
        });
    }

}
