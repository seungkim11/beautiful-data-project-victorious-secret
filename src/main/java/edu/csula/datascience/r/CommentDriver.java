package edu.csula.datascience.r;

import com.google.appengine.repackaged.com.google.gson.JsonObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.csula.datascience.r.acquisition.CommentCollector;
import edu.csula.datascience.r.acquisition.CommentSource;
import edu.csula.datascience.r.acquisition.MongoDriver;
import edu.csula.datascience.r.acquisition.SubmissionSource;
import edu.csula.datascience.r.models.Comment;
import net.dean.jraw.models.Submission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by tj on 4/21/16.
 */
public class CommentDriver {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        MongoDriver mongoDriver = new MongoDriver();
        MongoDatabase db = mongoDriver.getDb();
        CommentSource source= new CommentSource(db);
        CommentCollector collector = new CommentCollector(db);
        while (source.hasNext()){

            // this collection includes json comment blobs for 1000 submissions
            Collection<JSONObject> commentBlobsOfSubmissions = source.next();

            // each submission has collections of comment objects
            Collection<JSONArray> commentsLists = collector.splitSubmissionComments(commentBlobsOfSubmissions);

            // for each list of comment objects for 1 submission:
            for (JSONArray comments: commentsLists){

                // mungee
                Collection<Comment> cleanedComments = collector.mungee(comments);
            }

        }


        long end = System.currentTimeMillis();
        System.out.printf("stop time: %d %ntotal time: %d%n", end, end - start);

    }
}
