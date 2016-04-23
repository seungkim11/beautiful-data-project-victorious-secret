package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;

import java.util.*;

/**
 * Created by tj on 4/21/16.
 */
public class NewSubmissionSource implements Source<Submission> {

  private RedditClient redditClient;
  private int maxPostsPerPage = 100;
  private String subreddit = "all";
  private SubredditPaginator paginator;

  public NewSubmissionSource(){
    UserAgent myUserAgent = UserAgent.of("desktop", "awesomescript", "v0.1", "victorious-secret");
    redditClient = new RedditClient(myUserAgent);
    if(!redditClient.isAuthenticated()){
      RedditOAuth auth = new RedditOAuth();
      auth.authenticate(redditClient);
    }
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

    if(!redditClient.isAuthenticated()){
      RedditOAuth auth = new RedditOAuth();
      auth.authenticate(redditClient);
    }

    List<Submission> submissions = new ArrayList<>();
    for(int i = 0; i < maxPages; i++){
      if(paginator.hasNext()){
        Listing<Submission> listing = paginator.next();
        submissions.addAll(listing);
      }
    }
    System.out.printf("downloaded %d submissions", submissions.size());
    return submissions;
  }
}
