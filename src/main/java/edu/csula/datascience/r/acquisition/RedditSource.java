package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;

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
    subreddits = new Arrays.asList("askreddit");
  }

  public boolean hasNext(){

  }

  public Collection<Post> next(){
    return null;
  }

  public void remove(){

  }

  private List<Post> getPosts(){
    // posts for which subreddit?
    List<Post> posts = new ArrayList<>();
  }
}
