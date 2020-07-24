package org.hycode.net;

public interface NetSessionAdvisor {
    NetSessionFactory getNetSessionFactory();

    NetSession openSession();
}
