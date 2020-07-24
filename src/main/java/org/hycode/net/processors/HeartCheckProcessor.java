package org.hycode.net.processors;

import io.netty.channel.ChannelHandlerContext;
import org.hycode.net.DataPackage;
import org.hycode.net.NetData;
import org.hycode.net.NetSessionAdvisor;
import org.hycode.net.Processor;
import org.hycode.net.server.alive.checker.HeartCheckerWithAuthData;

public class HeartCheckProcessor implements Processor<HeartCheckerWithAuthData> {
    private final NetSessionAdvisor netSessionAdvisor;

    public HeartCheckProcessor(NetSessionAdvisor netSessionAdvisor) {
        this.netSessionAdvisor = netSessionAdvisor;
    }

    @Override
    public Class<? extends NetData> getDataClazz() {
        return HeartCheckerWithAuthData.class;
    }

    @Override
    public void callProcess(HeartCheckerWithAuthData heartCheckerWithAuthData, ChannelHandlerContext channelHandlerContext) {
        int status = heartCheckerWithAuthData.getStatus();
        if (status == 0) {
            heartCheckerWithAuthData.setStatus(1);
            heartCheckerWithAuthData.setResponseTime(System.currentTimeMillis());
            this.netSessionAdvisor.openSession().sendDataPackage(channelHandlerContext.channel(), heartCheckerWithAuthData, false);
        } else {
            System.out.println("心跳检测成功");
        }
    }


    @Override
    public boolean onDataClazz(DataPackage dataPackage) {
        return dataPackage.getData().getClass() == HeartCheckerWithAuthData.class;
    }
}
