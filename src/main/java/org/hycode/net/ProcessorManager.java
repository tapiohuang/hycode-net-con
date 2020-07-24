package org.hycode.net;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ProcessorManager implements ProcessorAdvisor {
    private final HashSet<Processor<? extends NetData>> processes;
    private final HashMap<Class<? extends NetData>, Processor<? extends NetData>> processorHashMap;


    public ProcessorManager() {
        processes = new HashSet<>();
        processorHashMap = new HashMap<>();
    }

    public Processor<? extends NetData> process(DataPackage dataPackage) {
        Processor<? extends NetData> processor = processorHashMap.get(dataPackage.getData().getClass());
        if (processor == null) {
            return new NoSuchProcessor();
        } else {
            return processor;
        }
    }

    private ProcessorManager addProcessor(Processor<? extends NetData> processor) {
        if (processorHashMap.size() != 0) {
            throw new RuntimeException("已经初始化!");
        }
        this.processes.add(processor);
        return this;
    }

    public void map() {
        synchronized (processes) {
            for (Processor<?> processor : processes) {
                processorHashMap.put(processor.getDataClazz(), processor);
            }
        }
    }

    @Override
    public ProcessorAdvisor processor(Processor<? extends NetData> processor) {
        return this.addProcessor(processor);
    }

    public static class NoSuchProcessor implements Processor<NetData> {

        @Override
        public Class<? extends NetData> getDataClazz() {
            return null;
        }

        @Override
        public void callProcess(NetData netData, ChannelHandlerContext channelHandlerContext) {
            throw new NullPointerException("无法处理该数据包:" + netData);
        }

        @Override
        public boolean onDataClazz(DataPackage dataPackage) {
            return false;
        }
    }
}
