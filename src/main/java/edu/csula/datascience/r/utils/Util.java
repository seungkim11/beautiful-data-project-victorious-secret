package edu.csula.datascience.r.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samskim on 4/24/16.
 */
// TODO: Discuss redundancy of code as well as security issue
public class Util {
    private final String REDDIT_USERNAME = "REDDIT_USERNAME";
    private final String REDDIT_PASSWORD = "REDDIT_PASSWORD";
    private final String REDDIT_APP_ID = "REDDIT_APP_ID";
    private final String REDDIT_APP_SECRET = "REDDIT_APP_SECRET";

    private final String MONGO_USERNAME = "MONGO_USERNAME";
    private final String MONGO_PASSWORD = "MONGO_PASSWORD";
    private final String MONGO_HOST = "MONGO_HOST";
    private final String MONGO_DB_NAME = "MONGO_DB_NAME";

    public Map<String, String> readRedditOAuthHttpCredentials(){
        Map<String, String> envMap = new HashMap<>(4);
        checkIfSourced(REDDIT_USERNAME);
        envMap.put(REDDIT_USERNAME, System.getenv(REDDIT_USERNAME));
        envMap.put(REDDIT_PASSWORD, System.getenv(REDDIT_PASSWORD));
        envMap.put(REDDIT_APP_ID, System.getenv(REDDIT_APP_ID));
        envMap.put(REDDIT_APP_SECRET, System.getenv(REDDIT_APP_SECRET));
        return envMap;
    }

    public Map<String, String> readDatabaseCredentials() {
        Map<String, String> envMap = new HashMap<>(4);
        checkIfSourced(MONGO_USERNAME);
        envMap.put(MONGO_USERNAME, System.getenv(MONGO_USERNAME));
        envMap.put(MONGO_PASSWORD, System.getenv(MONGO_PASSWORD));
        envMap.put(MONGO_HOST, System.getenv(MONGO_HOST));
        envMap.put(MONGO_DB_NAME, System.getenv(MONGO_DB_NAME));
        return envMap;
    }

    public static void checkIfSourced(String variableName){
        if(System.getenv(variableName) == null){
            System.err.printf("Could not find environment variable %s. Did you source the environment?", variableName);
            System.err.println("source <filename>.env");
            System.exit(1);
        }
    }
}
