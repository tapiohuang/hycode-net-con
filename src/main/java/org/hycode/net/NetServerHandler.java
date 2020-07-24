package org.hycode.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.Serializable;

public class NetServerHandler extends SimpleChannelInboundHandler<DataPackage> {
    private final NetServer netServer;

    public NetServerHandler(NetServer netServer) {
        this.netServer = netServer;
    }

    protected void channelRead0(ChannelHandlerContext ctx, DataPackage msg) throws Exception {
        ProcessorManager processorManager = netServer.getProcessorManager();
        processorManager.process(msg).callProcess(msg.getData(), ctx);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        netServer.getNetServerChannelManager().registerChannel(channel);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        netServer.getNetServerChannelManager().removeChannel(channel);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
