package org.hycode.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.hycode.net.schedules.HeartCheckSchedule;

import java.util.LinkedList;

public class NetServer implements NetSessionAdvisor, FilterAdvisor, SchedulableAdvisor, ProcessorAdvisor {
    private final ServerBootstrap bootstrap;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;
    private final NetServerChannelManager netServerChannelManager;
    private final ProcessorManager processorManager;
    private final NetSessionFactory netSessionFactory;
    private final LinkedList<Filter> filters;
    private final ScheduleManager scheduleManager;
    private Integer port = null;
    private boolean heartCheck = false;

    public NetServer() {
        this.bootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1);
        this.workGroup = new NioEventLoopGroup(200);
        this.bootstrap.group(bossGroup, workGroup);
        this.bootstrap.channel(NioServerSocketChannel.class);
        this.bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        this.bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        this.bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                nioSocketChannel.pipeline().addLast(new DataToByteEncoder());
                //nioSocketChannel.pipeline().addLast(new NetDataToDataPackage());

                nioSocketChannel.pipeline().addLast(new ByteToDataDecoder());
                nioSocketChannel.pipeline().addLast(new NetServerHandler(NetServer.this));
            }
        });
        netServerChannelManager = new NetServerChannelManager();
        processorManager = new ProcessorManager();
        netSessionFactory = new NetSessionFactory();
        filters = new LinkedList<>();
        scheduleManager = new ScheduleManager();
    }

    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    public ProcessorManager getProcessorManager() {
        return processorManager;
    }

    public NetServer port(int port) {
        this.port = port;
        return this;
    }

    public NetServer bind() {
        processorManager.map();
        scheduleManager.startAllSchedule();
        registerClose();
        if (port == null) {
            throw new NullPointerException("未指定端口");
        }
        try {
            bootstrap.bind(this.port).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        throw new RuntimeException("连接失败");
                    }
                }
            }).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        return this;
    }


    public NetServerChannelManager getNetServerChannelManager() {
        return netServerChannelManager;
    }

    public NetSession openSession() {
        return this.netSessionFactory.getSession();
    }


    @Override
    public NetSessionFactory getNetSessionFactory() {
        return this.netSessionFactory;
    }

    @Override
    public FilterAdvisor filter(Filter filter) {
        this.filters.addLast(filter);
        return this;
    }

    @Override
    public SchedulableAdvisor schedule(Schedule schedule) {
        return scheduleManager.schedule(schedule);
    }

    @Override
    public ProcessorAdvisor processor(Processor<? extends NetData> processor) {
        return processorManager.processor(processor);
    }

    public NetServer heartCheck(boolean heartCheck, int cycleTime) {
        this.heartCheck = heartCheck;
        HeartCheckSchedule heartCheckSchedule = new HeartCheckSchedule(this, cycleTime);
        this.schedule(heartCheckSchedule);
        return this;
    }

    public NetServer heartCheck(boolean heartCheck) {
        return this.heartCheck(heartCheck, 60);//默认心跳检测一分钟
    }


    private void registerClose() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("关闭服务端");
                netServerChannelManager.getChannels().forEach(channel -> {
                    try {
                        channel.close().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, "ShutdownHook-Thread");
        Runtime.getRuntime().addShutdownHook(t);
    }
}
