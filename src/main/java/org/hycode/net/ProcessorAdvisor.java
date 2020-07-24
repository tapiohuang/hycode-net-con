package org.hycode.net;

public interface ProcessorAdvisor {
    ProcessorAdvisor processor(Processor<? extends NetData> processor);
}
