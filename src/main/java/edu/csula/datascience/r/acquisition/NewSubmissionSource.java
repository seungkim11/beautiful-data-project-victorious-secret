package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;

import java.util.*;

/**
 * Created by tj on 4/21/16.
 */
public class NewSubmissionSource implements Source<Submission> {

  private final List<String> subreddits;
  private RedditClient redditClient;
  private int step;
  private int index;

  public NewSubmissionSource(){
    /*
    TODO: funny, news, gifs, reddit pics, etc
    showerthoughts, explainlikeimfive, askreddit, jokes, askscience, an
     */
    // FIXME: eventually, we should get a list containeing what subreddits to use as source
    this(Arrays.asList("showerthoughts", "jokes", "explainlikeimfive", "askreddit", "askscience"));
  }

  public NewSubmissionSource(List<String> subreddits){
    this.subreddits = subreddits;
    UserAgent myUserAgent = UserAgent.of("desktop", "awesomescript", "v0.1", "victorious-secret");
    redditClient = new RedditClient(myUserAgent);
    step = 1;
    index = 0;
  }

  public boolean hasNext(){
    return index < subreddits.size();
  }

  public Collection<Submission> next(){
    if(index >= subreddits.size()) throw new NoSuchElementException("no more subreddits to download");
    Collection<Submission> result = downloadSubmissions(index, Math.min(index + step, subreddits.size()));
    index += step;
    return result;
  }


  // FIXME should be private
  public List<Submission> downloadSubmissions(int start, int end){
    int maxPostsPerPage = 100;
    int maxPages = 10;

    if(!redditClient.isAuthenticated()){
      RedditOAuth auth = new RedditOAuth();
      auth.authenticate(redditClient);
    }

    List<Submission> submissions = new ArrayList<>();
    for(int i = start; i < end; i++){
      SubredditPaginator paginator = new SubredditPaginator(redditClient, subreddits.get(i));
      paginator.setLimit(maxPostsPerPage);
      List<Listing<Submission>> listings = paginator.accumulate(maxPages);
      for(Listing<Submission> listing : listings){
        submissions.addAll(listing);
      }
    }
    return submissions;
  }
}
