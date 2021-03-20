package eu.atomicnetworks.jts3servermod.radiosystem.objects;

/**
 *
 * @author Kacper Mura
 * Copyright (c) 2021 atomicnetworks ✨
 *
 */
public class Channel {
    
    private String clientUid;
    private int channelId;

    public String getClientUid() {
        return clientUid;
    }

    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
    
}
