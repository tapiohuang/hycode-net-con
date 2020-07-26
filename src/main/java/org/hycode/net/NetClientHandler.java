package org.hycode.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.Serializable;

public class NetClientHandler extends SimpleChannelInboundHandler<DataPackage> {
    private final NetClient netClient;

    public NetClientHandler(NetClient netClient) {
        this.netClient = netClient;
    }

    protected void channelRead0(ChannelHandlerContext ctx, DataPackage msg) throws Exception {
        ProcessorManager processorManager = netClient.getProcessorManager();
        processorManager.process(msg).callProcess(msg.getData(), ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端关闭");
        netClient.noticeWorkerConnect();
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
