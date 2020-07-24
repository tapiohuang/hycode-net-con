package org.hycode.net;


import io.netty.channel.Channel;

public interface ClientIdChannelManager {
    void registerChannel(Channel channel, String clientId);

    Channel getChannel(String clientId);
}
