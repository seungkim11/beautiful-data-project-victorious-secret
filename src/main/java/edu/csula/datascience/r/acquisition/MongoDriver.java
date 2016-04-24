package edu.csula.datascience.r.acquisition;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import edu.csula.datascience.r.utils.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samskim on 4/24/16.
 */
public class MongoDriver {

    private final String USERNAME = "MONGO_USERNAME";
    private final String PASSWORD = "MONGO_PASSWORD";
    private final String HOST = "MONGO_HOST";
    private final String DB_NAME = "MONGO_DB_NAME";
    private MongoClient mongoClient;
    private MongoDatabase db;
    private String collectionName;

    public MongoDriver(){
        readDatabaseCredentials();
        connectDatabase();
    }

    private void connectDatabase(){
        Map<String, String> envMap = readDatabaseCredentials();
        String dbUser = envMap.get(USERNAME);
        String dbPass = envMap.get(PASSWORD);
        String host = envMap.get(HOST);
        String dbName = envMap.get(DB_NAME);
        MongoClientURI uri = new MongoClientURI(
                "mongodb://" + dbUser + ":" + dbPass + "@" + host + "/?authSource=" + dbName);
        mongoClient = new MongoClient(uri);
        db = mongoClient.getDatabase(dbName);
    }

    public MongoDatabase getDb(){
        return db;
    }

    public Map<String, String> readDatabaseCredentials(){
        Map<String, String> envMap = new HashMap<>(4);
        Util util = new Util();
        Util.checkIfSourced(USERNAME);
        envMap.put(USERNAME, System.getenv(USERNAME));
        envMap.put(PASSWORD, System.getenv(PASSWORD));
        envMap.put(HOST, System.getenv(HOST));
        envMap.put(DB_NAME, System.getenv(DB_NAME));
        return envMap;
    }

}
