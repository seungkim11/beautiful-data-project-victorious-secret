package edu.csula.datascience.r;

import edu.csula.datascience.r.acquisition.CommentSource;
import edu.csula.datascience.r.acquisition.SubmissionCollector;
import edu.csula.datascience.r.acquisition.SubmissionSource;
import net.dean.jraw.models.Submission;

import java.util.*;

/**
 * Created by tj on 4/21/16.
 */
public class CollectDriver {
  public static void main(String[] args) throws Exception{
    long start = System.currentTimeMillis();
    System.out.println("start time: " + start);

    SubmissionSource source = new SubmissionSource();
    Collection<Submission> submissions = source.hasNext() ? source.next() : null;
    List<Submission> submissionsForComments = new ArrayList<>();
    Iterator<Submission> it = submissions.iterator();
    submissionsForComments.add(it.next());
    submissionsForComments.add(it.next());
    submissionsForComments.add(it.next());
    CommentSource cs = new CommentSource(submissionsForComments);
    for(Submission sub : submissionsForComments){
      cs.downloadComments(sub.getSubredditName());
    }

    System.out.println("submission size: " + submissions.size());
    long end = System.currentTimeMillis();
    System.out.printf("stop time: %d %ntotal time: %d%n", end, end - start);

  }
}
