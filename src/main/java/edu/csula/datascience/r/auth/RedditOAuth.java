package edu.csula.datascience.r.auth;

import java.util.HashMap;
import java.util.Map;

import net.dean.jraw.http.UserAgent;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;

public class RedditOAuth {

  private final String USERNAME = "REDDIT_USERNAME";
  private final String PASSWORD = "REDDIT_PASSWORD";
  private final String APP_ID = "REDDIT_APP_ID";
  private final String APP_SECRET = "REDDIT_APP_SECRET";

  private Map getVariables(){
    Map<String, String> envMap = new HashMap<>(4);
    if(System.getenv(USERNAME) == null){
      System.err.println("Could not find environment variables. Did you source the environment?");
      System.err.println("source <filename>.env");
      System.exit(1);
    }
    envMap.put(USERNAME, System.getenv(USERNAME));
    envMap.put(PASSWORD, System.getenv(PASSWORD));
    envMap.put(APP_ID, System.getenv(APP_ID));
    envMap.put(APP_SECRET, System.getenv(APP_SECRET));
    return envMap;
  }

  public void authenticate(RedditClient redditClient){
    Map<String, String> envMap = getVariables();
    Credentials credentials = Credentials.script(envMap.get(USERNAME), envMap.get(PASSWORD),
        envMap.get(APP_ID), envMap.get(APP_SECRET));
    try{
      OAuthData authData= redditClient.getOAuthHelper().easyAuth(credentials);
      redditClient.authenticate(authData);
    }
    catch(OAuthException ex){
      System.err.println("Unable to authenticate the reddit client");
      ex.printStackTrace();
    }
  }
}
