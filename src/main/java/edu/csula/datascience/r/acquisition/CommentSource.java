package edu.csula.datascience.r.acquisition;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import edu.csula.datascience.r.auth.RedditOAuthHttp;
import edu.csula.datascience.r.models.Comment;
import edu.csula.datascience.r.utils.Util;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by samskim on 4/21/16.
 */
public class CommentSource implements Source<JSONObject> {

    private String token;
    private RedditOAuthHttp oAuth;
    private MongoCollection collection;
    private long collectionSize;
    private long count;
    private int blockSize;
    private long timerStart;
    private long timerEnd;


    private long rateTimerEnd;

    public CommentSource() {
        // connect reddit
        oAuth = new RedditOAuthHttp();
        oAuth.getEnvKeys();

        //retrieve token
        authenticate();
    }

    public CommentSource(String str) {
        // for testing
    }

    public CommentSource(MongoDatabase db) {
        // connect to db collection
        collection = db.getCollection("posts_2016_04_23");
        collectionSize = collection.count();
        count = 0;

        blockSize = 60;
        // connect reddit
        oAuth = new RedditOAuthHttp();
        oAuth.getEnvKeys();

        //retrieve token
        authenticate();

    }

    // retrieve token
    public void authenticate() {
        String token = oAuth.getToken();
        if (!token.trim().isEmpty()) {
            System.out.println("Token successfully retrieved");
            timerStart = System.currentTimeMillis();
            this.token = token;
        } else {
            System.out.println("Failed to retrieve token");
            System.exit(1);
        }
    }

    @Override
    public boolean hasNext() {

        return count < collectionSize;
    }

    @Override
    public Collection<JSONObject> next() {

//
//        Collection<JSONObject> commentBlob = new ArrayList<>();
//        String id, subreddit, jsonString;
//        Document doc;
//        MongoCursor<Document> cursor = collection.find().skip((int) count).limit(blockSize).iterator();
//        while (cursor.hasNext()) {
//            doc = cursor.next();
//            subreddit = (String) doc.get("subreddit");
//            id = (String) doc.get("id");
//
//            if (getElapsedMinutes() > 55.0){
//                authenticate();
//            }
//
//            jsonString = download(subreddit, id);
//            commentBlob.add(parseComments(jsonString));
//        }
//
//        count += blockSize;
//        return commentBlob;
        return null;
    }

    public Map<ObjectId, JSONObject> nextMap() {

        Map<ObjectId, JSONObject> commentBlobMap = new HashMap<>();
        String id, subreddit, jsonString;
        Document doc;
        MongoCursor<Document> cursor = collection.find().skip((int) count).limit(blockSize).iterator();


        while (cursor.hasNext()) {
            doc = cursor.next();
            subreddit = (String) doc.get("subreddit");
            id = (String) doc.get("id");

            if (getElapsedMinutes() > 55) {
                System.out.println("need to authenticate");
                authenticate();
            }

            jsonString = download(subreddit, id);
            if (jsonString.isEmpty()){
                System.out.println("Downloading " + subreddit + "/" + id + " unauthorized. Skipping to next source");
            }else{
                commentBlobMap.put((ObjectId) doc.get("_id"), parseComments(jsonString));
            }
            count++;
            System.out.println("Count: " + count);
        }

        return commentBlobMap;
    }

    public double getElapsedMinutes() {
        timerEnd = System.currentTimeMillis();
        double tDelta = timerEnd - timerStart;
        return (tDelta / 1000.0) / 60.0;

    }

    // FIXME
    public String download(String subreddit, String postId) {
        String url = "https://oauth.reddit.com/r/" + subreddit + "/comments/" + postId + ".json?limit=500";

        String requestMethod = "GET";
        StringBuffer response = new StringBuffer();
        URL obj = null;
        HttpURLConnection con = null;
        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(requestMethod);

            con.setRequestProperty("Authorization", "bearer " + token);
            con.setRequestProperty("User-Agent", "github:csula-students/beautiful-data-project-victorious-secret v1.0 by /u/" + oAuth.getUsername());
            int responseCode = con.getResponseCode();
            System.out.println("\nSending '" + requestMethod + "' request to URL : " + url + ", Response: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

//            System.out.println(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return "";

        } finally {
            if (con != null) {
                con.disconnect();
            }
            return response.toString();
        }
    }

    public JSONObject parseComments(String response) {
        Collection<JSONObject> commentList = new ArrayList<>();

        JSONArray rootblob = new JSONArray(response.toString());

        // first object in the array is submission post itself
        // second object in the array is the comments
        JSONObject commentObject = rootblob.getJSONObject(1);
        return commentObject;

    }

    public long getCount() {
        return count;
    }
}
