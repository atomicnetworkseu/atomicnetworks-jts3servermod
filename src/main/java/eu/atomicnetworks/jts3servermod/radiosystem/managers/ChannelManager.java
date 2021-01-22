package eu.atomicnetworks.jts3servermod.radiosystem.managers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import eu.atomicnetworks.jts3servermod.radiosystem.RadioSystem;
import eu.atomicnetworks.jts3servermod.radiosystem.objects.Channel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.swing.Timer;
import org.bson.Document;

/**
 *
 * @author Kacper Mura
 * 2019 - 2020 Copyright (c) by atomicnetworks.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ChannelManager {
    
    private final RadioSystem plugin;
    private Timer timer;
    private LoadingCache<String, Channel> channelCache;
    
    private String oneLastTitle = "";
    private String danceLastTitle = "";
    private String trapLastTitle = "";

    public ChannelManager(RadioSystem plugin) {
        this.plugin = plugin;
        this.timer = new Timer(1000, (e) -> {
            if(oneLastTitle.isEmpty() || danceLastTitle.isEmpty() || trapLastTitle.isEmpty()) {
                this.oneLastTitle = this.plugin.getAtomicClient().getChannelOne().getSong().getTitle();
                this.danceLastTitle = this.plugin.getAtomicClient().getChannelDance().getSong().getTitle();
                this.trapLastTitle = this.plugin.getAtomicClient().getChannelTrap().getSong().getTitle();
                this.updateChannel();
            }
            if(!this.oneLastTitle.equals(this.plugin.getAtomicClient().getChannelOne().getSong().getTitle())) {
                this.oneLastTitle = this.plugin.getAtomicClient().getChannelOne().getSong().getTitle();
                this.updateChannel();
            }
            if(!this.danceLastTitle.equals(this.plugin.getAtomicClient().getChannelDance().getSong().getTitle())) {
                this.danceLastTitle = this.plugin.getAtomicClient().getChannelDance().getSong().getTitle();
                this.updateChannel();
            }
            if(!this.trapLastTitle.equals(this.plugin.getAtomicClient().getChannelTrap().getSong().getTitle())) {
                this.trapLastTitle = this.plugin.getAtomicClient().getChannelTrap().getSong().getTitle();
                this.updateChannel();
            }
        });
        this.timer.setInitialDelay(5000);
        this.timer.setRepeats(true);
        this.timer.start();
        initCache();
    }
    
    private void initCache() {
        this.channelCache = CacheBuilder.newBuilder().maximumSize(100L).expireAfterWrite(30L, TimeUnit.MINUTES).build(new CacheLoader<String, Channel>() {
            @Override
            public Channel load(String clientUid) throws Exception {
                CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
                getChannel(clientUid, result -> {
                    completableFuture.complete(result);
                });
                return completableFuture.get();
            }
        });
    }
    
    private void updateChannel() {
        String description = "[center][size=15]#[size=20][B]yoυr[/B]ѕoɴɢ\n[size=10]In this channel you can always see what is playing, \nhave fun with our program! 💙 \n\n[hr]\n[size=11][table]\n [tr][td][center]\n[img]" + this.plugin.getAtomicClient().getChannelOne().getSong().getArtworks().getArt100() + "[/img] \nATR.[B]ONE[/B][/td]\n\n[td]\n\n [B]" + this.plugin.getAtomicClient().getChannelOne().getSong().getTitle() + "[/B] \n " + this.plugin.getAtomicClient().getChannelOne().getSong().getArtist() + "[/td][/tr]\n\n\n\n [tr]\n\n\n [td][center]\n[img]" + this.plugin.getAtomicClient().getChannelDance().getSong().getArtworks().getArt100() + "[/img] \nATR.[B]DANCE[/B][/td]\n\n[td]\n\n [B]" + this.plugin.getAtomicClient().getChannelDance().getSong().getTitle() + "[/B] \n " + this.plugin.getAtomicClient().getChannelDance().getSong().getArtist() + "[/td][/tr]\n\n\n\n [tr]\n\n\n [td][center]\n[img]" + this.plugin.getAtomicClient().getChannelTrap().getSong().getArtworks().getArt100() + "[/img] \nATR.[B]TRAP[/B][/td]\n\n[td]\n\n [B]" + this.plugin.getAtomicClient().getChannelTrap().getSong().getTitle() + "[/B] \n " + this.plugin.getAtomicClient().getChannelTrap().getSong().getArtist() + "[/td][/tr][/table]\n\n[hr]\n\n[size=9]You have a catchy tune that has already run? \nno problem, you can simply write to the bot with [B].lastsong[/B] and you know your new favorite song!\n\n[/center]🎧  [COLOR=#5e5e5e][B]" + this.plugin.getAtomicClient().getAllListeners() + " people[/B] currently listening to our stations[/COLOR]\n";
        if(this.plugin.getAtomicClient().isLive()) {
            if(plugin.getModClass().getChannelName(129).equals("[cspacer]" + this.plugin.getAtomicClient().getStreamer().split(" ")[0] + " is currently streaming")) {
                this.plugin.getQueryLib().doCommand("channeledit cid=129 channel_description=" + this.plugin.getQueryLib().encodeTS3String(description));
                return;
            }
            this.plugin.getQueryLib().doCommand("channeledit cid=129 channel_name=[cspacer]" + this.plugin.getQueryLib().encodeTS3String(this.plugin.getAtomicClient().getStreamer().split(" ")[0] + " is currently streaming") + " channel_description=" + this.plugin.getQueryLib().encodeTS3String(description));
        } else {
            if(plugin.getModClass().getChannelName(129).equals("[cspacer]#yoursong")) {
                this.plugin.getQueryLib().doCommand("channeledit cid=129 channel_description=" + this.plugin.getQueryLib().encodeTS3String(description));
                return;
            }
            this.plugin.getQueryLib().doCommand("channeledit cid=129 channel_name=[cspacer]#yoursong channel_description=" + this.plugin.getQueryLib().encodeTS3String(description));
        }
    }
    
    public void createChannel(String clientUid, int channelId, Consumer<Channel> consumer) {
        this.plugin.getMongoManager().getChannels().find(Filters.eq("clientUid", clientUid)).first((Document t, Throwable thrwbl) -> {
            if(t == null) {
                Channel channel = new Channel();
                channel.setClientUid(clientUid);
                channel.setChannelId(channelId);
                
                t = this.plugin.getGson().fromJson(this.plugin.getGson().toJson(channel), Document.class);
                this.plugin.getMongoManager().getChannels().insertOne(t, (Void t1, Throwable thrwbl1) -> {
                    consumer.accept(channel);
                });
            } else {
                Channel channel = new Channel();
                channel.setClientUid(clientUid);
                channel.setChannelId(channelId);
                Document document = this.plugin.getGson().fromJson(this.plugin.getGson().toJson(channel), Document.class);
                this.plugin.getMongoManager().getChannels().replaceOne(Filters.eq("clientUid", clientUid), document, (UpdateResult t1, Throwable thrwbl1) -> {
                });
            }
        });
    }
    
    private void getChannel(String clientUid, Consumer<Channel> consumer) {
        this.plugin.getMongoManager().getChannels().find(Filters.eq("clientUid", clientUid)).first((Document t, Throwable thrwbl) -> {
            if(t != null) {
                Channel channel = this.plugin.getGson().fromJson(t.toJson(), Channel.class);
                consumer.accept(channel);
            } else {
                Channel channel = new Channel();
                channel.setClientUid(clientUid);
                channel.setChannelId(0);
                consumer.accept(channel);
            }
        });
    }

    public LoadingCache<String, Channel> getChannelCache() {
        return channelCache;
    }
    
    public Channel getChannel(String clientUid) {
        try {
            return this.channelCache.get(clientUid);
        } catch (ExecutionException ex) {
            return null;
        }
    }
    
}
