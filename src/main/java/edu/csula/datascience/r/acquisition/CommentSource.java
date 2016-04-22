package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Source;
import edu.csula.datascience.r.auth.RedditOAuth;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.CommentStream;
import net.dean.jraw.paginators.SubredditPaginator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by tj on 4/21/16.
 */
public class CommentSource implements Source<CommentNode> {

  private RedditClient redditClient;
  private List<Submission> submissions;

  public CommentSource(List<Submission> submissions){
    UserAgent myUserAgent = UserAgent.of("desktop", "awesomescript", "v0.1", "victorious-secret");
    redditClient = new RedditClient(myUserAgent);
    this.submissions = submissions;
  }

  public boolean hasNext(){
    // FIXME
    return false;
  }

  public Collection<CommentNode> next(){
    // FIXME
    Collection<CommentNode> result = new ArrayList<>();
    return result;
  }

  public void remove(){
    // FIXME
  }

  public List<Comment> downloadComments(String subreddit){
    int limit = 1000;
    if(!redditClient.isAuthenticated()){
      RedditOAuth auth = new RedditOAuth();
      auth.authenticate(redditClient);
    }
    CommentStream cs = new CommentStream(redditClient, subreddit);
    cs.setLimit(limit);
    List<Comment> comments = new ArrayList<>();
    System.out.printf("downloading comments for %s", subreddit);
    while(cs.hasNext()){
      // FIXME move intialization outside of loop
      List<Comment> temp = cs.next();
      System.out.printf("next retrieved %d records%n", temp.size());
      comments.addAll(temp);
    }
    System.out.printf("total comments retrieved %d %n", comments.size());
    return comments;
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
