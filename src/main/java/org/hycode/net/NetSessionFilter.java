package org.hycode.net;

public class NetSessionFilter implements Filter {
    private final NetSessionAdvisor netSessionAdvisor;
    private final NetSessionFactory netSessionFactory;

    public NetSessionFilter(NetSessionAdvisor netSessionAdvisor) {
        this.netSessionAdvisor = netSessionAdvisor;
        netSessionFactory = netSessionAdvisor.getNetSessionFactory();
    }


    @Override
    public boolean filter(NetData netData) {
        return false;
    }
}
