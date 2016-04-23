package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Created by tj on 4/21/16.
 */
public class NewSubmissionSource implements Source<Submission> {

  private RedditClient redditClient;
  private int maxPostsPerPage = 100;
  private String subreddit = "all";
  private SubredditPaginator paginator;
  private Instant expiration;

  public NewSubmissionSource(){
    UserAgent myUserAgent = UserAgent.of("desktop", "awesomescript", "v0.1", "victorious-secret");
    redditClient = new RedditClient(myUserAgent);
    authenticate();
    paginator = new SubredditPaginator(redditClient, subreddit);
    paginator.setLimit(maxPostsPerPage);
    paginator.setSorting(Sorting.NEW);
  }

  public boolean hasNext(){
    return paginator.hasNext();
  }

  public Collection<Submission> next(){
    if(!hasNext()) throw new NoSuchElementException("no more data to download");
    Collection<Submission> result = downloadSubmissions();
    return result;
  }


  // FIXME should be private
  public List<Submission> downloadSubmissions(){
    int maxPages = 50;
    ensureAuthenticated();
    List<Submission> submissions = new ArrayList<>();
    Listing<Submission> listing;
    for(int i = 0; i < maxPages; i++){
      if(paginator.hasNext()){
        try {
          listing = paginator.next();
          submissions.addAll(listing);
        } catch (NetworkException ex){
          System.err.println("exception while downloading. trying to reauthenticate");
          System.err.println("Is client authenticated: ?" + redditClient.isAuthenticated());
          authenticate();
          System.err.println("Is client authenticated: ?" + redditClient.isAuthenticated());
          listing = paginator.next();
          submissions.addAll(listing);
        }
      }
    }
    System.out.printf("downloaded %d submissions", submissions.size());
    return submissions;
  }

  private void ensureAuthenticated(){
    if(!redditClient.isAuthenticated()){
      System.out.println("client is not authenticated, authenticating...");
      authenticate();
    }

    System.out.println("expiring at: " + expiration);
    Clock utcClock = Clock.systemUTC();
    Instant now = Instant.now(utcClock);
    System.out.println("now is: " + now);
    long millisRemaining = expiration.toEpochMilli() - now.toEpochMilli();
    System.out.printf("time remaining on auth token: %d", millisRemaining);
    if(millisRemaining < 1000 * 60 *10){
      System.out.println("client token expires soon, authenticating...");
      authenticate();
    }
  }

  private void authenticate() {
    RedditOAuth auth = new RedditOAuth();
    auth.authenticate(redditClient);
    Duration tokenLife = Duration.ofHours(1L);
    Clock utcClock = Clock.systemUTC();
    expiration = Instant.now(utcClock);
    expiration = expiration.plus(tokenLife);
  }
}
