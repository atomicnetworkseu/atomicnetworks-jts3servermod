package eu.atomicnetworks.jts3servermod.radiosystem.managers;

import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import eu.atomicnetworks.jts3servermod.radiosystem.RadioSystem;
import org.bson.Document;

/**
 *
 * @author Kacper Mura
 * 2019 - 2020 Copyright (c) by atomicnetworks.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class MongoManager {

    private final RadioSystem plugin;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> channels;

    public MongoManager(RadioSystem plugin) {
        this.plugin = plugin;
        this.client = MongoClients.create(new ConnectionString("mongodb://127.0.0.1"));
        this.database = client.getDatabase("teamspeak");
        this.channels = this.database.getCollection("channels");
    }

    public MongoCollection<Document> getCollection(String name) {
        return this.database.getCollection(name);
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoCollection<Document> getChannels() {
        return channels;
    }

}
