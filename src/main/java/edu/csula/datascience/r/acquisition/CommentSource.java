package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.CommentNode;
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
public class CommentSource implements Source<Submission> {

  private final List<String> subreddits;
  private RedditClient redditClient;

  public CommentSource(){
    /*
    TODO:
    perhaps each source should correspond to a subreddit? or subset of subreddit
    funny, news, gifs, reddit pics, etc]
    showerthoughts, explainlikeimfive, askreddit, jokes, askscience, an
     */
    // FIXME: eventually, we should get a list containeing what subreddits to use as source
    this(Arrays.asList("showerthoughts"));
  }

  public CommentSource(List<String> subreddits){
    this.subreddits = subreddits;
    UserAgent myUserAgent = UserAgent.of("desktop", "awesomescript", "v0.1", "victorious-secret");
    redditClient = new RedditClient(myUserAgent);
  }

  public boolean hasNext(){
    // FIXME
    return false;
  }

  public Collection<Submission> next(){
    // FIXME
    Collection<Submission> result = new ArrayList<>();
    return result;
  }

  public void remove(){
    // FIXME
  }

  // FIXME should be private
  public List<Submission> downloadSubmissions(){
    int maxPostsPerPage = 100;
    int maxPages = 10;

    if(!redditClient.isAuthenticated()){
      RedditOAuth auth = new RedditOAuth();
      auth.authenticate(redditClient);
    }

    List<Submission> submissions = new ArrayList<>();
    for(String subreddit : subreddits){
      SubredditPaginator paginator = new SubredditPaginator(redditClient, subreddit);
      paginator.setLimit(maxPostsPerPage);
      List<Listing<Submission>> listings = paginator.accumulate(maxPages);
      for(Listing<Submission> listing : listings){
        submissions.addAll(listing);
      }
    }
    return submissions;
  }

  public CommentNode downloadComments(Submission submission){
    if(!redditClient.isAuthenticated()){
      RedditOAuth auth = new RedditOAuth();
      auth.authenticate(redditClient);
    }
    CommentNode rootNode = submission.getComments();
//    Iterable<CommentNode> iterable = rootNode.walkTree();
//    for (CommentNode node : iterable) {
//      comments.add(node);
//    }
    return rootNode;
  }
}
