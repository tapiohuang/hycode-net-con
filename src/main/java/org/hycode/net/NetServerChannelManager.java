package org.hycode.net;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.HashSet;

public class NetServerChannelManager implements ClientIdChannelManager {
    private final HashSet<Channel> channels;
    private final HashMap<String, Channel> clientIdChannelMap;
    private final HashMap<Channel, String> channelClientIdMap;
    
    public NetServerChannelManager() {
        channels = new HashSet<Channel>();
        clientIdChannelMap = new HashMap<String, Channel>();
        channelClientIdMap = new HashMap<Channel, String>();
    }

    public HashSet<Channel> getChannels() {
        return channels;
    }

    public void registerChannel(Channel channel) {
        channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        String clientId = channelClientIdMap.get(channel);
        clientIdChannelMap.remove(clientId);
        channelClientIdMap.remove(channel);
    }


    public void registerChannel(Channel channel, String clientId) {
        channels.add(channel);
        clientIdChannelMap.put(clientId, channel);
        channelClientIdMap.put(channel, clientId);
    }

    public Channel getChannel(String clientId) {
        return this.clientIdChannelMap.get(clientId);
    }
}
