package eu.atomicnetworks.jts3servermod.radiosystem;

import de.stefan1200.jts3servermod.interfaces.HandleBotEvents;
import de.stefan1200.jts3servermod.interfaces.HandleTS3Events;
import de.stefan1200.jts3servermod.interfaces.JTS3ServerMod_Interface;
import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import eu.atomicnetworks.atomicradio.java.AtomicClient;
import eu.atomicnetworks.jts3servermod.radiosystem.managers.ChannelManager;
import java.util.HashMap;

/**
 *
 * @author kacpe
 */
public class RadioSystem implements HandleBotEvents, HandleTS3Events {
    
    private JTS3ServerMod_Interface modClass = null;
    private JTS3ServerQuery queryLib = null;
    private AtomicClient atomicClient;
    private ChannelManager channelManager;
    
    public static void main(String[] args) {
    }

    @Override
    public void initClass(JTS3ServerMod_Interface modClass, JTS3ServerQuery queryLib, String prefix) {
        this.modClass = modClass;
        this.queryLib = queryLib;
    }

    @Override
    public void handleOnBotConnect() {
    }

    @Override
    public void handleAfterCacheUpdate() {
    }

    @Override
    public void activate() {
        System.out.println("RadioSystem Plugin v1.0 created by Kacper Mura (VocalZero) https://github.com/VocalZero.");
        this.channelManager = new ChannelManager(this);
        this.atomicClient = new AtomicClient();
    }

    @Override
    public void disable() {
    }

    @Override
    public void unload() {
    }

    @Override
    public boolean multipleInstances() {
        return false;
    }

    @Override
    public int getAPIBuild() {
        return 4;
    }

    @Override
    public String getCopyright() {
        return "RadioSystem Plugin v1.1.0 created by Kacper Mura (VocalZero) [url]https://github.com/VocalZero[/url].";
    }

    @Override
    public String[] botChatCommandList(HashMap<String, String> eventInfo, boolean isFullAdmin, boolean isAdmin) {
        return null;
    }

    @Override
    public String botChatCommandHelp(String command) {
        return "";
    }

    @Override
    public boolean handleChatCommands(String msg, HashMap<String, String> eventInfo, boolean isFullAdmin, boolean isAdmin) {
        return false;
    }

    @Override
    public void handleTS3Events(String eventType, HashMap<String, String> eventInfo) {
    }

    public JTS3ServerQuery getQueryLib() {
        return queryLib;
    }

    public JTS3ServerMod_Interface getModClass() {
        return modClass;
    }

    public AtomicClient getAtomicClient() {
        return atomicClient;
    }
    
}
