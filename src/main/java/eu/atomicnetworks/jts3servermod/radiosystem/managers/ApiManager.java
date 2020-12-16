package eu.atomicnetworks.jts3servermod.radiosystem.managers;

import eu.atomicnetworks.jts3servermod.radiosystem.RadioSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kacper Mura
 * 2019 - 2020 Copyright (c) by MusikBots.net to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ApiManager {
    
    private final RadioSystem plugin;
    private Timer timer;
    private JSONObject jsonObject;
    private long oneEndTimestamp = 0;
    private long danceEndTimestamp = 0;
    private long trapEndTimestamp = 0;

    public ApiManager(RadioSystem plugin) {
        this.plugin = plugin;
        this.timer = new Timer(1000, (e) -> {
            if(oneEndTimestamp == 0 || danceEndTimestamp == 0 || trapEndTimestamp == 0) {
                this.jsonObject = this.readJsonFromApi();
                this.oneEndTimestamp = this.getEnd_at("one");
                this.danceEndTimestamp = this.getEnd_at("dance");
                this.trapEndTimestamp = this.getEnd_at("trap");
                this.updateChannel();
            }
            if(System.currentTimeMillis() > (this.oneEndTimestamp*1000)+5000) {
                this.jsonObject = this.readJsonFromApi();
                this.oneEndTimestamp = this.getEnd_at("one");
                this.updateChannel();
            }
            if(System.currentTimeMillis() > (this.danceEndTimestamp*1000)+5000) {
                this.jsonObject = this.readJsonFromApi();
                this.danceEndTimestamp = this.getEnd_at("dance");
                this.updateChannel();
            }
            if(System.currentTimeMillis() > (this.trapEndTimestamp*1000)+5000) {
                this.jsonObject = this.readJsonFromApi();
                this.trapEndTimestamp = this.getEnd_at("trap");
                this.updateChannel();
            }
        });
        this.timer.setInitialDelay(0);
        this.timer.setRepeats(true);
        this.timer.start();
    }
    
    private void updateChannel() {
        String description = "[center][size=15]#[size=20][B]yoυr[/B]ѕoɴɢ\n[size=10]In this channel you can always see what is playing, \nhave fun with our program! 💙 \n\n[hr]\n[size=11][table]\n [tr][td][center]\n[img]" + this.get100Artwork("one") + "[/img] \nATR.[B]ONE[/B][/td]\n\n[td]\n\n [B]" + this.getTitle("one") + "[/B] \n " + this.getArtist("one") + "[/td][/tr]\n\n\n\n [tr]\n\n\n [td][center]\n[img]" + this.get100Artwork("dance") + "[/img] \nATR.[B]DANCE[/B][/td]\n\n[td]\n\n [B]" + this.getTitle("dance") + "[/B] \n " + this.getArtist("dance") + "[/td][/tr]\n\n\n\n [tr]\n\n\n [td][center]\n[img]" + this.get100Artwork("trap") + "[/img] \nATR.[B]TRAP[/B][/td]\n\n[td]\n\n [B]" + this.getTitle("trap") + "[/B] \n " + this.getArtist("trap") + "[/td][/tr][/table]\n\n[hr]\n\n[size=9]You have a catchy tune that has already run? \nno problem, you can simply write to the bot with [B].lastsong[/B] and you know your new favorite song!\n\n[/center]🎧  [COLOR=#5e5e5e][B]" + this.getAllListeners() + " people[/B] currently listening to our stations[/COLOR]\n";
        if(this.isLive()) {
            if(plugin.getModClass().getChannelName(129).equals("[cspacer]" + this.getStreamer().split(" ")[0] + " is currently streaming")) {
                this.plugin.getQueryLib().doCommand("channeledit cid=129 channel_description=" + this.plugin.getQueryLib().encodeTS3String(description));
                return;
            }
            this.plugin.getQueryLib().doCommand("channeledit cid=129 channel_name=[cspacer]" + this.plugin.getQueryLib().encodeTS3String(this.getStreamer().split(" ")[0] + " is currently streaming") + " channel_description=" + this.plugin.getQueryLib().encodeTS3String(description));
        } else {
            if(plugin.getModClass().getChannelName(129).equals("[cspacer]#yoursong")) {
                this.plugin.getQueryLib().doCommand("channeledit cid=129 channel_description=" + this.plugin.getQueryLib().encodeTS3String(description));
                return;
            }
            this.plugin.getQueryLib().doCommand("channeledit cid=129 channel_name=[cspacer]#yoursong channel_description=" + this.plugin.getQueryLib().encodeTS3String(description));
        }
    }
    
    private JSONObject readJsonFromApi() {
        try {
            InputStream inputStream = new URL("https://api.atomicradio.eu/channels").openStream();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder stringBuilder = new StringBuilder();
                int element;
                while((element = bufferedReader.read()) != -1) {
                    stringBuilder.append((char) element);
                }
                return new JSONObject(stringBuilder.toString());
            } finally {
                inputStream.close();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApiManager.class.getName()).log(Level.SEVERE, null, ex);
            this.timer.restart();
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ApiManager.class.getName()).log(Level.SEVERE, null, ex);
            this.timer.restart();
            return null;
        }
    }
    
    public ArrayList<JSONObject> getJsonArrayList(JSONArray jsonArray) {
        ArrayList<JSONObject> list = new ArrayList<>();
        if(jsonArray != null) {
            for(int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.optJSONObject(i));
            }
        }
        return list;
    }
    
    public int getAllListeners() {
        return this.getJsonObject().getInt("all_listeners");
    }
    
    public boolean isLive() {
        return this.getJsonObject().getJSONObject("one").getJSONObject("live").getBoolean("is_live");
    }
    
    public String getStreamer() {
        return this.getJsonObject().getJSONObject("one").getJSONObject("live").getString("streamer");
    }
    
    public String getTitle(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getString("title");
    }
    
    public String getArtist(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getString("artist");
    }
    
    public String getPlaylist(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getString("playlist");
    }
    
    public Long getStart_at(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getLong("start_at");
    }
    
    public Long getEnd_at(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getLong("end_at");
    }
    
    public String get100Artwork(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getJSONObject("artworks").getString("100");
    }
    
    public String getDurationTimestamp(String channel) {
        long time = ((this.getEnd_at(channel)*1000)-(this.getStart_at(channel)*1000));
        String minutes = String.valueOf((time / 1000) / 60);
        String seconds = String.valueOf((time / 1000) % 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    public String getDurationTimestampNow(String channel) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli((this.getStart_at(channel)*1000)), TimeZone.getDefault().toZoneId());
        long diff = Math.abs(Duration.between(now, start).getSeconds());
        String seconds = String.valueOf(diff % 60);
        String minutes = String.valueOf(diff / 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    public String getLastTitle(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getString("title");
    }
    
    public String getLastArtist(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getString("artist");
    }
    
    public String getLastPlaylist(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getString("playlist");
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
    
}
