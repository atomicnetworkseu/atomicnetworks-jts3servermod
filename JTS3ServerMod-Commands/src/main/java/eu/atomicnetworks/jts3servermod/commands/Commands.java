package eu.atomicnetworks.jts3servermod.commands;

import de.stefan1200.jts3servermod.interfaces.HandleBotEvents;
import de.stefan1200.jts3servermod.interfaces.HandleTS3Events;
import de.stefan1200.jts3servermod.interfaces.JTS3ServerMod_Interface;
import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TS3ServerQueryException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kacper Mura
 * Copyright (c) 2021 atomicnetworks ✨
 *
 */
public class Commands implements HandleBotEvents, HandleTS3Events {

    private JTS3ServerMod_Interface modClass = null;
    private JTS3ServerQuery queryLib = null;

    private final int nochat_role = 20; // 20
    private final int nopoke_role = 19; // 19
    private final int bottoggle_role = 43; // 43
    private final int german_role = 31; // 31
    private final int accept_role_1 = 29; // 29
    private final int accept_role_2 = 32; // 32
    private final int accept_role_3 = 54; // 54

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
    }

    @Override
    public void disable() {
    }

    @Override
    public void unload() {
    }

    @Override
    public boolean multipleInstances() {
        return true;
    }

    @Override
    public int getAPIBuild() {
        return 4;
    }

    @Override
    public String getCopyright() {
        return "RadioSystem Commands Plugin created by Kacper Mura (VocalZero) [url]https://github.com/VocalZero[/url].";
    }

    @Override
    public String[] botChatCommandList(HashMap<String, String> eventInfo, boolean isFullAdmin, boolean isAdmin) {
        return new String[]{};
    }

    @Override
    public String botChatCommandHelp(String command) {
        return "";
    }

    @Override
    public boolean handleChatCommands(String msg, HashMap<String, String> eventInfo, boolean isFullAdmin, boolean isAdmin) {
        System.out.println("handleChatCommands: " + eventInfo.toString());
        String cmd = eventInfo.get("msg");
        if (cmd.equalsIgnoreCase("!nochat")) {
            try {
                HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                String[] groups = clientInfo.get("client_servergroups").split(",");
                boolean hasGroup = false;
                boolean hasRight = false;
                for (String group : groups) {
                    if (Integer.valueOf(group) == nochat_role) {
                        hasGroup = true;
                    } else if (Integer.valueOf(group) == accept_role_1) {
                        hasRight = true;
                    }
                }
                if (!hasRight) {
                    return true;
                }
                if (hasGroup) {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you can now be contacted by everyone again.");
                    queryLib.doCommand(MessageFormat.format("servergroupdelclient sgid={0} cldbid={1}", nochat_role, clientInfo.get("client_database_id")));
                } else {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you can now no longer be contacted.");
                    queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", nochat_role, clientInfo.get("client_database_id")));
                }
            } catch (TS3ServerQueryException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (cmd.equalsIgnoreCase("!german")) {
            try {
                HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                String[] groups = clientInfo.get("client_servergroups").split(",");
                boolean hasGroup = false;
                boolean hasRight = false;
                for (String group : groups) {
                    if (Integer.valueOf(group) == german_role) {
                        hasGroup = true;
                    } else if (Integer.valueOf(group) == accept_role_1) {
                        hasRight = true;
                    }
                }
                if (!hasRight) {
                    return true;
                }
                if (hasGroup) {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], the role has been removed again.");
                    queryLib.doCommand(MessageFormat.format("servergroupdelclient sgid={0} cldbid={1}", german_role, clientInfo.get("client_database_id")));
                } else {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], others now see that you speak [B]German[/B].");
                    queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", german_role, clientInfo.get("client_database_id")));
                }
            } catch (TS3ServerQueryException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (cmd.equalsIgnoreCase("!bottoggle")) {
            try {
                HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                String[] groups = clientInfo.get("client_servergroups").split(",");
                boolean hasGroup = false;
                boolean hasRight = false;
                for (String group : groups) {
                    if (Integer.valueOf(group) == bottoggle_role) {
                        hasGroup = true;
                    } else if (Integer.valueOf(group) == accept_role_1) {
                        hasRight = true;
                    }
                }
                if (!hasRight) {
                    return true;
                }
                if (hasGroup) {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you will now receive all messages from the system again.");
                    queryLib.doCommand(MessageFormat.format("servergroupdelclient sgid={0} cldbid={1}", bottoggle_role, clientInfo.get("client_database_id")));
                } else {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you will now only receive relevant messages from the system.");
                    queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", bottoggle_role, clientInfo.get("client_database_id")));
                }
            } catch (TS3ServerQueryException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (cmd.equalsIgnoreCase("!nopoke")) {
            try {
                HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                String[] groups = clientInfo.get("client_servergroups").split(",");
                boolean hasGroup = false;
                boolean hasRight = false;
                for (String group : groups) {
                    if (Integer.valueOf(group) == nopoke_role) {
                        hasGroup = true;
                    } else if (Integer.valueOf(group) == accept_role_1) {
                        hasRight = true;
                    }
                }
                if (!hasRight) {
                    return true;
                }
                if (hasGroup) {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you can now be poked by everyone again.");
                    queryLib.doCommand(MessageFormat.format("servergroupdelclient sgid={0} cldbid={1}", nopoke_role, clientInfo.get("client_database_id")));
                } else {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Successful[/B], you can now no longer be poked.");
                    queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", nopoke_role, clientInfo.get("client_database_id")));
                }
            } catch (TS3ServerQueryException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (cmd.equalsIgnoreCase("!accept")) {
            try {
                HashMap<String, String> clientInfo = queryLib.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, Integer.valueOf(eventInfo.get("invokerid")));
                String[] groups = clientInfo.get("client_servergroups").split(",");
                boolean hasGroup = false;
                for (String group : groups) {
                    if (Integer.valueOf(group) == accept_role_1) {
                        hasGroup = true;
                    }
                }
                if (!hasGroup) {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Verification[/B] » You can now use all the features of our TeamSpeak, how does it feel?");
                    queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", accept_role_1, clientInfo.get("client_database_id")));
                    queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", accept_role_2, clientInfo.get("client_database_id")));
                    queryLib.doCommand(MessageFormat.format("servergroupaddclient sgid={0} cldbid={1}", accept_role_3, clientInfo.get("client_database_id")));
                } else {
                    queryLib.sendTextMessage(Integer.valueOf(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "[B]Verification[/B] » You are already verified, cool right?");
                }
            } catch (TS3ServerQueryException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    @Override
    public void handleTS3Events(String eventType, HashMap<String, String> eventInfo) {
    }

}
