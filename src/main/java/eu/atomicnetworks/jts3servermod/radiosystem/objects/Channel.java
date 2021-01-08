package eu.atomicnetworks.jts3servermod.radiosystem.objects;

/**
 *
 * @author Kacper Mura
 * 2019 - 2020 Copyright (c) by atomicnetworks.eu to present.
 * All rights reserved. https://github.com/VocalZero
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
