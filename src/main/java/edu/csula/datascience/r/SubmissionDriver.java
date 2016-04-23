package edu.csula.datascience.r;

import edu.csula.datascience.r.acquisition.CommentSource;
import edu.csula.datascience.r.acquisition.SubmissionCollector;
import edu.csula.datascience.r.acquisition.SubmissionSource;
import edu.csula.datascience.r.models.Post;
import net.dean.jraw.models.Submission;

import java.util.*;

/**
 * Created by tj on 4/21/16.
 */
public class SubmissionDriver {
  public static void main(String[] args) throws Exception{
    long start = System.currentTimeMillis();
    System.out.println("start time: " + start);


    SubmissionSource source = new SubmissionSource();
    SubmissionCollector collector = new SubmissionCollector();
    while(source.hasNext()) {
      Collection<Submission> submissions = source.next();
      System.out.println("submission size: " + submissions.size());
      Collection<Post> cleanedPosts = collector.mungee(submissions);
      System.out.println("posts size: " + submissions.size());
      collector.save(cleanedPosts);
    }

    long end = System.currentTimeMillis();
    System.out.printf("stop time: %d %ntotal time: %d%n", end, end - start);

  }
}
