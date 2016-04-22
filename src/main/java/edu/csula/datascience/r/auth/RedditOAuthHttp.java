package edu.csula.datascience.r.auth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samskim on 4/21/16.
 */
public class RedditOAuthHttp {

    private final String USERNAME = "REDDIT_USERNAME";
    private final String PASSWORD = "REDDIT_PASSWORD";
    private final String APP_ID = "REDDIT_APP_ID";
    private final String SECRET = "REDDIT_APP_SECRET";
    private Map<String, String> envmap;

    public RedditOAuthHttp(){
        envmap = new HashMap<>();

    }

    public void getEnvKeys(){
        envmap.put("username", System.getenv(USERNAME));
        envmap.put("password", System.getenv(PASSWORD));
        envmap.put("app_id", System.getenv(APP_ID));
        envmap.put("secret", System.getenv(SECRET));
        if (envmap.get("username").trim().isEmpty()) System.exit(1);

        for (String s: envmap.keySet()){
            System.out.println(s + ": " + envmap.get(s));
        }

//        return envmap.get("username").isEmpty() ? false : true;
    }

    public String getUsername(){

        return envmap.get("username");
    }

    public String getToken(){
        String token = "";

        String url = "https://www.reddit.com/api/v1/access_token";
        String requestMethod = "POST";
        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(requestMethod);

            String parameters = "grant_type=password&username=" + envmap.get("username") + "&password=" + envmap.get("password");
            String credentials = envmap.get("app_id") + ":" + envmap.get("secret");
            String encoding = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", "github:csula-students/beautiful-data-project-victorious-secret v1.0 by /u/" + envmap.get("username"));
            con.setRequestProperty("Authorization", "Basic " + encoding);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending '" + requestMethod + "' request to URL : " + url);
//            System.out.println("Parameters: " + parameters);
//            System.out.println("Credentials: " + credentials);
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
            JSONObject json = new JSONObject(response.toString());
            token = (String) json.get("access_token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return token;
        }
    }
}