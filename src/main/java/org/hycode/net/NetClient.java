package org.hycode.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.hycode.net.worker.ReconnectServerWorker;

import java.util.LinkedList;

public class NetClient implements NetSessionAdvisor, FilterAdvisor, ProcessorAdvisor, SchedulableAdvisor {
    private final Bootstrap bootstrap;
    private final NioEventLoopGroup group;
    private final ProcessorManager processorManager;
    private final NetSessionFactory netSessionFactory;
    private final LinkedList<Filter> filters;
    private final ScheduleManager scheduleManager;
    private final boolean reconnect = true;
    private String host;
    private Integer port;
    private Channel channel;
    private ReconnectServerWorker reconnectServerWorker;

    public NetClient() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        processorManager = new ProcessorManager();
        netSessionFactory = new NetSessionFactory();
        filters = new LinkedList<>();
        scheduleManager = new ScheduleManager();
    }

    public boolean isConnect() {
        synchronized (this) {
            if (this.channel == null) {
                return true;
            }
            return this.channel.isOpen() || this.channel.isActive();
        }
    }

    public NetClient host(String host) {
        this.host = host;
        return this;
    }

    public NetClient port(int port) {
        this.port = port;
        return this;
    }


    public void connect0() {
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(this.host, this.port).sync();
            if (!future.isSuccess()) {
                throw new RuntimeException("连接失败");
            } else {
                synchronized (this) {
                    channel = future.channel();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public NetClient connect() {
        this.processorManager.map();
        this.registerClose();
        this.registerReconnectWorker();
        try {
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new DataToByteEncoder());
                    //ch.pipeline().addLast(new NetDataToDataPackage());

                    ch.pipeline().addLast(new ByteToDataDecoder());
                    ch.pipeline().addLast(new NetClientHandler(NetClient.this));
                }
            });
            this.connect0();
        } catch (Throwable e) {
            e.printStackTrace();
            this.group.shutdownGracefully();
        }
        return this;
    }

    public NetSession openSession() {
        return netSessionFactory.getSession();
    }

    public ProcessorManager getProcessorManager() {
        return processorManager;
    }


    @Override
    public NetSessionFactory getNetSessionFactory() {
        return netSessionFactory;
    }

    @Override
    public FilterAdvisor filter(Filter filter) {
        this.filters.addLast(filter);
        return this;
    }

    @Override
    public ProcessorAdvisor processor(Processor<? extends NetData> processor) {
        processorManager.processor(processor);
        return this;
    }

    @Override
    public SchedulableAdvisor schedule(Schedule schedule) {
        return scheduleManager.schedule(schedule);
    }

    private void registerClose() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("关闭客户端");
                channel.close();
            }
        }, "ShutdownHook-Thread");
        Runtime.getRuntime().addShutdownHook(t);
    }

    private void registerReconnectWorker() {
        if (!reconnect) {
            return;
        }
        this.reconnectServerWorker = new ReconnectServerWorker(this);
        reconnectServerWorker.start();
        WorkerThreadPool.POOL.execute(reconnectServerWorker);
    }

    public void noticeWorkerConnect() {
        if (reconnect) {
            reconnectServerWorker.notifyWorker();
        }
    }
}
