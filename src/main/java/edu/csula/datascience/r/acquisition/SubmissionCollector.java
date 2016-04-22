package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Collector;
import edu.csula.datascience.r.models.Comment;
import edu.csula.datascience.r.models.Post;
import net.dean.jraw.models.Submission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by tj on 4/21/16.
 */
public class SubmissionCollector implements Collector<Post, Submission> {
  @Override
  public Collection<Post> mungee(Collection<Submission> src) {
    List<Post> posts = new ArrayList<>(src.size());

    String id;
    String author;
    String title;
    String selftext;
    String subreddit;
    String url;
    String domain;
    boolean isSelf;
    String mediaType;
    List<Comment> comments;
    boolean isNSFW;
    long timestamp;
    int score;
    Post post;
    for(Submission sub : src){
      id = sub.getId();
      author = sub.getAuthor();
      title = sub.getTitle();
      selftext = sub.getSelftext(); // may need to sanitize html
      subreddit = sub.getSubredditName();
      url = sub.getUrl();
      domain = sub.getDomain();
      isSelf = sub.isSelfPost(); // is this correct?
      mediaType = "text";// one of text or image?
      comments = new ArrayList<>(0);
      isNSFW = sub.isNsfw();
      timestamp = sub.getCreatedUtc().getTime();
      score = sub.getScore();
      post = new Post(id, author, title, selftext, subreddit, url, domain, isSelf, mediaType, comments,
          isNSFW, timestamp, score);
      posts.add(post);
    }
    return posts;
  }

  @Override
  public void save(Collection<Post> data) {
    System.out.println("saving data");
  }

}
