package org.hycode.net.server.alive.checker;

import org.hycode.net.Filter;
import org.hycode.net.NetData;

public class AuthFilter implements Filter {
    @Override
    public boolean filter(NetData netData) {
        return false;
    }
}
