package eu.atomicnetworks.jts3servermod.radiosystem;

import com.google.gson.Gson;
import de.stefan1200.jts3servermod.interfaces.HandleBotEvents;
import de.stefan1200.jts3servermod.interfaces.HandleTS3Events;
import de.stefan1200.jts3servermod.interfaces.JTS3ServerMod_Interface;
import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TS3ServerQueryException;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;
import eu.atomicradio.AtomicClient;
import eu.atomicnetworks.jts3servermod.radiosystem.managers.ChannelManager;
import eu.atomicnetworks.jts3servermod.radiosystem.managers.MongoManager;
import eu.atomicnetworks.jts3servermod.radiosystem.objects.Channel;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kacper Mura
 * Copyright (c) 2021 atomicnetworks ✨
 * This code is available under the MIT License.
 *
 */
public class RadioSystem implements HandleBotEvents, HandleTS3Events {
    
    private JTS3ServerMod_Interface modClass = null;
    private JTS3ServerQuery queryLib = null;
    private AtomicClient atomicClient;
    private ChannelManager channelManager;
    private MongoManager mongoManager;
    private Gson gson;
    
    private final int channel_creator = 20; //84
    private final int channel_order = 20; //85
    private final int channel_role = 5; // 9
    
    private final int nochat_role = 20; // 20
    private final int nopoke_role = 19; // 19
    private final int bottoggle_role = 43; // 43
    private final int german_role = 31; // 31
    private final int accept_role_1 = 29; // 29
    private final int accept_role_2 = 32; // 32
    private final int accept_role_3 = 54; // 54
    
    private final int support_end_channel = 26; // 26
    private final int main_channel = 43; // 43
    
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
        this.gson = new Gson();
        this.mongoManager = new MongoManager(this);
        this.channelManager = new ChannelManager(this);
        this.atomicClient = new AtomicClient();
        this.queryLib.setTeamspeakActionListener(new TeamspeakActionListener() {
            @Override
            public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo) {
                if(eventType.equals("notifytextmessage")) {
                    String message = eventInfo.get("msg");
                    if(message.startsWith("!")) {
                        switch(message.toLowerCase()) {
                            case "!nochat":
                                try {
                                    HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                                    String[] groups = clientInfo.get("client_servergroups").split(",");
                                    boolean hasGroup = false;
                                    for(String group : groups) {
                                        if(Integer.valueOf(group) == nochat_role) {
                                            hasGroup = true;
                                        }
                                    }
                                    if(hasGroup) {
                                        queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you can now be contacted by everyone again.");
                                        queryLib.doCommand(MessageFormat.format("servergroupdelclient sgid={0} cldbid={1}", nochat_role, clientInfo.get("client_database_id")));
                                    } else {
                                        queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you can now no longer be contacted.");
                                        queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", nochat_role, clientInfo.get("client_database_id")));
                                    }
                                } catch (TS3ServerQueryException ex) {
                                    Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            case "!german":
                                try {
                                    HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                                    String[] groups = clientInfo.get("client_servergroups").split(",");
                                    boolean hasGroup = false;
                                    for(String group : groups) {
                                        if(Integer.valueOf(group) == german_role) {
                                            hasGroup = true;
                                        }
                                    }
                                    if(hasGroup) {
                                        queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], the role has been removed again.");
                                        queryLib.doCommand(MessageFormat.format("servergroupdelclient sgid={0} cldbid={1}", german_role, clientInfo.get("client_database_id")));
                                    } else {
                                        queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], others now see that you speak [B]German[/B].");
                                        queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", german_role, clientInfo.get("client_database_id")));
                                    }
                                } catch (TS3ServerQueryException ex) {
                                    Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            case "!bottoggle":
                                try {
                                    HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                                    String[] groups = clientInfo.get("client_servergroups").split(",");
                                    boolean hasGroup = false;
                                    for(String group : groups) {
                                        if(Integer.valueOf(group) == bottoggle_role) {
                                            hasGroup = true;
                                        }
                                    }
                                    if(hasGroup) {
                                        queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you will now receive all messages from the system again.");
                                        queryLib.doCommand(MessageFormat.format("servergroupdelclient sgid={0} cldbid={1}", bottoggle_role, clientInfo.get("client_database_id")));
                                    } else {
                                        queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you will now only receive relevant messages from the system.");
                                        queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", bottoggle_role, clientInfo.get("client_database_id")));
                                    }
                                } catch (TS3ServerQueryException ex) {
                                    Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            case "!nopoke":
                                try {
                                    HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                                    String[] groups = clientInfo.get("client_servergroups").split(",");
                                    boolean hasGroup = false;
                                    for(String group : groups) {
                                        if(Integer.valueOf(group) == nopoke_role) {
                                            hasGroup = true;
                                        }
                                    }
                                    if(hasGroup) {
                                        queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you can now be poked by everyone again.");
                                        queryLib.doCommand(MessageFormat.format("servergroupdelclient sgid={0} cldbid={1}", nopoke_role, clientInfo.get("client_database_id")));
                                    } else {
                                        queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you can now no longer be poked.");
                                        queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", nopoke_role, clientInfo.get("client_database_id")));
                                    }
                                } catch (TS3ServerQueryException ex) {
                                    Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            case "!accept":
                                try {
                                    HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                                    String[] groups = clientInfo.get("client_servergroups").split(",");
                                    boolean hasGroup = false;
                                    for(String group : groups) {
                                        if(Integer.valueOf(group) == accept_role_1) {
                                            hasGroup = true;
                                        }
                                    }
                                    if(!hasGroup) {
                                        queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", accept_role_1, clientInfo.get("client_database_id")));
                                        queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", accept_role_2, clientInfo.get("client_database_id")));
                                        queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", accept_role_3, clientInfo.get("client_database_id")));
                                    }
                                } catch (TS3ServerQueryException ex) {
                                    Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                        }
                    }
                } else if(eventType.equals("notifyclientmoved")) {
                    if(Integer.valueOf(eventInfo.get("ctid")) == channel_creator) {
                        HashMap<String, String> clientInfo = modClass.getClientListEntry(Integer.valueOf(eventInfo.get("clid")));
                        if(channelManager.getChannel(queryLib.decodeTS3String(clientInfo.get("client_unique_identifier"))).getChannelId() != 0) {
                            Channel channel = channelManager.getChannel(queryLib.decodeTS3String(clientInfo.get("client_unique_identifier")));
                            HashMap<String, String> channelReponse = queryLib.doCommand(MessageFormat.format("channelinfo cid={0}", channel.getChannelId()));
                            if(channelReponse.get("msg").equalsIgnoreCase("invalid channelID")) {
                                try {
                                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("clid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]ChannelCreator[/B] » Your channel has been deleted because you haven't used it for a long time, we will create a new one...");
                                } catch (TS3ServerQueryException ex) {
                                    Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                createChannel(eventInfo, clientInfo);
                                return;
                            }
                            try {
                                queryLib.moveClient(Integer.valueOf(eventInfo.get("clid")), channel.getChannelId(), "");
                            } catch (TS3ServerQueryException ex) {
                                Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return;
                        }
                        createChannel(eventInfo, clientInfo);
                    } else if (Integer.valueOf(eventInfo.get("ctid")) == support_end_channel) {
                        try {
                            queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("clid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Thank you for using our support, we are happy if we could help you!");
                            queryLib.moveClient(Integer.valueOf(eventInfo.get("clid")), main_channel, "");
                        } catch (TS3ServerQueryException ex) {
                            Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if(eventType.equals("notifycliententerview")) {
                    try {
                        HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("clid")));
                        String[] groups = clientInfo.get("client_servergroups").split(",");
                        boolean hasGroup1 = false;
                        boolean hasGroup2 = false;
                        boolean hasGroup3 = false;
                        for(String group : groups) {
                            if(Integer.valueOf(group) == accept_role_1) {
                                hasGroup1 = true;
                            }
                            if(Integer.valueOf(group) == accept_role_2) {
                                hasGroup2 = true;
                            }
                            if(Integer.valueOf(group) == accept_role_3) {
                                hasGroup3 = true;
                            }
                        }
                        if(hasGroup1) {
                            if(!hasGroup2 || !hasGroup3) {
                                queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", accept_role_2, clientInfo.get("client_database_id")));
                                queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", accept_role_3, clientInfo.get("client_database_id")));
                            }
                        }
                    } catch (TS3ServerQueryException ex) {
                        Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
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
        /*if(eventType.equalsIgnoreCase("notifyclientmoved")) {
            if(Integer.valueOf(eventInfo.get("ctid")) == this.channel_creator) {
                HashMap<String, String> clientInfo = this.modClass.getClientListEntry(Integer.valueOf(eventInfo.get("clid")));
                if(this.getChannelManager().getChannel(this.queryLib.decodeTS3String(clientInfo.get("client_unique_identifier"))).getChannelId() != 0) {
                    Channel channel = this.getChannelManager().getChannel(this.queryLib.decodeTS3String(clientInfo.get("client_unique_identifier")));
                    HashMap<String, String> channelReponse = this.queryLib.doCommand(MessageFormat.format("channelinfo cid={0}", channel.getChannelId()));
                    if(channelReponse.get("msg").equalsIgnoreCase("invalid channelID")) {
                        try {
                            this.queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("clid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]ChannelCreator[/B] » Your channel has been deleted because you haven't used it for a long time, we will create a new one...");
                        } catch (TS3ServerQueryException ex) {
                            Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        this.createChannel(eventInfo, clientInfo);
                        return;
                    }
                    try {
                        this.queryLib.moveClient(Integer.valueOf(eventInfo.get("clid")), channel.getChannelId(), "");
                    } catch (TS3ServerQueryException ex) {
                        Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }
                this.createChannel(eventInfo, clientInfo);
            }
        }*/
    }
    
    private void createChannel(HashMap<String, String> eventInfo, HashMap<String, String> clientInfo) {
        try {
            HashMap<String, String> channelReponse = this.queryLib.doCommand(MessageFormat.format("channelcreate channel_name={0} channel_topic={1} channel_flag_permanent=1 channel_order={2} channel_codec_quality=10", this.queryLib.encodeTS3String(this.queryLib.decodeTS3String(clientInfo.get("client_nickname")) + "'s Channel"), this.queryLib.encodeTS3String("🐹  Created by " + this.queryLib.decodeTS3String(clientInfo.get("client_nickname") + " • " + this.queryLib.decodeTS3String(clientInfo.get("client_unique_identifier")))), this.channel_order));
            HashMap<String, String> channelInfo = getTS3Reponse(channelReponse.get("response").split(" "));
            this.queryLib.moveClient(Integer.valueOf(eventInfo.get("clid")), Integer.valueOf(channelInfo.get("cid")), "");
            this.queryLib.doCommand(MessageFormat.format("setclientchannelgroup cgid={0} cid={1} cldbid={2}", this.channel_role, Integer.valueOf(channelInfo.get("cid")), Integer.valueOf(clientInfo.get("client_database_id"))));
            this.queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("clid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]ChannelCreator[/B] » You've been moved to your channel.");
            this.getChannelManager().getChannelCache().invalidate(this.queryLib.decodeTS3String(clientInfo.get("client_unique_identifier")));
            this.getChannelManager().createChannel(this.queryLib.decodeTS3String(clientInfo.get("client_unique_identifier")), Integer.valueOf(channelInfo.get("cid")), (Channel t) -> {
                Channel channel = this.getChannelManager().getChannel(this.queryLib.decodeTS3String(clientInfo.get("client_unique_identifier")));
            });
        } catch (TS3ServerQueryException ex) {
            Logger.getLogger(RadioSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private HashMap<String, String> getTS3Reponse(String[] response) {
        HashMap<String, String> responseMap = new HashMap<>();
        for (int i = 0; i < response.length; i++) {
            String responseValue = response[i];
            if (responseValue.contains("=")) {
                String key = responseValue.split("=")[0];
                String value = responseValue.split("=")[1];
                responseMap.put(key, value);
            }
        }
        return responseMap;
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

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public Gson getGson() {
        return gson;
    }
    
}
