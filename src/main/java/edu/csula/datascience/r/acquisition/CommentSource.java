package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import edu.csula.datascience.r.auth.RedditOAuthHttp;
import edu.csula.datascience.r.models.Comment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by samskim on 4/21/16.
 */
public class CommentSource implements Source<JSONObject> {

    private String subreddit;
    private String postId;
    private String token;
    private RedditOAuthHttp oAuth;


    public CommentSource(){

        authenticate();
    }

    public void authenticate(){
        oAuth = new RedditOAuthHttp();
        oAuth.getEnvKeys();
        String token = oAuth.getToken();
        if (!token.trim().isEmpty()) {
            System.out.println("Token successfully retrieved");
            this.token = token;
        }else{
            System.out.println("Failed to retrieve token");
            System.exit(1);
        }
    }

    public void setSubreddit(String subreddit){
        this.subreddit = subreddit;
    }

    public void setPostId(String postId){
        this.postId = postId;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Collection<JSONObject> next() {

        return null;
    }

    // FIXME
    public void download(){
        String url = "https://oauth.reddit.com/r/" + subreddit + "/comments/" + postId + ".json?limit=500";

        String requestMethod = "GET";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(requestMethod);

            con.setRequestProperty("Authorization", "bearer " + token);
            con.setRequestProperty("User-Agent", "github:csula-students/beautiful-data-project-victorious-secret v1.0 by /u/" + oAuth.getUsername());
            int responseCode = con.getResponseCode();
            System.out.println("\nSending '" + requestMethod + "' request to URL : " + url);
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());

            List<JSONObject> commentList = new ArrayList<>();

            JSONArray rootblob = new JSONArray(response.toString());

            // first object in the array is submission post itself
            // second object in the array is the comments
            JSONObject commentObject = rootblob.getJSONObject(1);
            JSONObject data = (JSONObject) commentObject.get("data");

            // here are all root comments in the submission
            JSONArray comments = (JSONArray) data.get("children");

            for (Object commentObj: comments) {
                JSONObject commentNode = (JSONObject) commentObj;
                commentList.add(commentNode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
