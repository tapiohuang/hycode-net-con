package org.hycode.net;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface Processor<T extends NetData> {
    Class<? extends NetData> getDataClazz();

    void callProcess(T netData, ChannelHandlerContext channelHandlerContext);


    boolean onDataClazz(DataPackage dataPackage);


}
