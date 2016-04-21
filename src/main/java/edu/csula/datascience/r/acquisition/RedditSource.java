package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import edu.csula.datascience.r.dto.Post;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    this(Arrays.asList("news"));
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
    Collection<Post> result = new ArrayList<>();
    return result;
  }

  public void remove(){
    // FIXME
  }

  // FIXME should be private
  public List<Submission> downloadSubmissions(){
    // posts for which subreddit?
    List<Submission> submissions = new ArrayList<>();
    UserAgent myUserAgent = UserAgent.of("desktop", "awesomescript", "v0.1", "victorious-secret");
    RedditClient redditClient = new RedditClient(myUserAgent);
    if(!redditClient.isAuthenticated()){
      RedditOAuth auth = new RedditOAuth();
      auth.authenticate(redditClient);
    }

    for(String subreddit : subreddits){
      SubredditPaginator paginator = new SubredditPaginator(redditClient, subreddit);
      paginator.setLimit(1);
      List<Listing<Submission>> listings = paginator.accumulate(1000);
      System.out.println(listings.size());
      for(Listing<Submission> listing : listings){
        int count = 0;
        for(Submission submission : listing){
          System.out.println(submission);
          count++;
        }
        System.out.printf("list had %d submissions%n", count);
      }

    }
    return null;
  }
}
