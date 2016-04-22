package edu.csula.datascience.r;

import edu.csula.datascience.r.acquisition.CommentSource;
import edu.csula.datascience.r.acquisition.SubmissionSource;
import edu.csula.datascience.r.models.Comment;
import net.dean.jraw.models.Submission;

import java.util.*;

/**
 * Created by tj on 4/21/16.
 */
public class CollectDriver {
  public static void main(String[] args) throws Exception{
    long start = System.currentTimeMillis();

//    System.out.println("start time: " + start);
//
//    SubmissionSource source = new SubmissionSource();
//    Collection<Submission> submissions = source.hasNext() ? source.next() : null;
//    List<Submission> submissionsForComments = new ArrayList<>();
//    Iterator<Submission> it = submissions.iterator();
//    submissionsForComments.add(it.next());
//    submissionsForComments.add(it.next());
//    submissionsForComments.add(it.next());
//    System.out.println("submission size: " + submissions.size());

    String subreddit = "jokes";
    String postId = "4fs6bv";

    CommentSource commentSource = new CommentSource();
    commentSource.setSubreddit(subreddit);
    commentSource.setPostId(postId);
    System.out.println(commentSource.next());

    long end = System.currentTimeMillis();
    System.out.printf("stop time: %d %ntotal time: %d%n", end, end - start);

  }
}
