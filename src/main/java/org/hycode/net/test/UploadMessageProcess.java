package org.hycode.net.test;

import io.netty.channel.ChannelHandlerContext;
import org.hycode.net.DataPackage;
import org.hycode.net.NetData;
import org.hycode.net.Processor;

public class UploadMessageProcess implements Processor {

    @Override
    public Class<? extends NetData> getDataClazz() {
        return null;
    }

    @Override
    public void callProcess(NetData netData, ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public boolean onDataClazz(DataPackage dataPackage) {
        return false;
    }
}
