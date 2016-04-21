package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tj on 4/21/16.
 */
public class RedditSource implements Source<Post> {

  private final List<String> subreddits;

  public RedditSource(){
    /*
    TODO:
    perhaps each source should correspond to a subreddit? or subset of subreddit
    funny, news, gifs, reddit pics, etc]
    showerthoughts, explainlikeimfive, askreddit, jokes, askscience, an
     */
    // FIXME: eventually, we should get a list containeing what subreddits to use as source
    this(new Arrays.asList("askreddit"));
  }

  public RedditSource(List<String> subreddits){
    this.subreddits = subreddits;
  }

  public boolean hasNext(){
    // FIXME
    return false;
  }

  public Collection<Post> next(){
    // FIXME
    return null;
  }

  public void remove(){
    // FIXME
  }

  private List<Post> getPosts(){
    // posts for which subreddit?
    List<Post> posts = new ArrayList<>();
    UserAgent myUserAgent = UserAgent.of("desktop", "awesomescript", "v0.1", "victorious-secret");
    RedditClient redditClient = new RedditClient(myUserAgent);
    if(!redditClient.isAuthenticated()){
      RedditOAuth auth = new RedditOAuth();
      auth.authenticate(redditClient);
    }


  }
}
