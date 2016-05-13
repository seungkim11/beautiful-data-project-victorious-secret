package edu.csula.datascience.r;

import com.mongodb.client.MongoDatabase;

import edu.csula.datascience.r.acquisition.CommentCollector;
import edu.csula.datascience.r.acquisition.CommentSource;
import edu.csula.datascience.r.acquisition.MyMongoDriver;
import edu.csula.datascience.r.models.Comment;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by tj on 4/21/16.
 */
public class CommentDriver {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        MyMongoDriver mongoDriver = new MyMongoDriver();
        MongoDatabase db = mongoDriver.getDb();

        CommentSource source = new CommentSource(db, "");

        if (args.length > 0 && args[0] != null) {
            source = new CommentSource(db, args[0]);
        }

        CommentCollector collector = new CommentCollector(db);
        while (source.hasNext()) {

            long rateCheckStart = System.currentTimeMillis();
            // this collection includes json comment blobs for 1000 submissions
//            Collection<JSONObject> commentBlobsOfSubmissions = source.next();

            Map<ObjectId, JSONObject> commentBlobsOfSubmissionsMap = source.nextMap();

            // each submission has collections of comment objects
//            Collection<JSONArray> commentsLists = collector.splitSubmissionComments(commentBlobsOfSubmissionsMap);
            Map<ObjectId, JSONArray> commentsLists = collector.splitSubmissionComments(commentBlobsOfSubmissionsMap);

            // for each list of comment objects for 1 submission:
            for (ObjectId id : commentsLists.keySet()) {

                JSONArray comments = commentsLists.get(id);

                // mungee
                Collection<Comment> cleanedComments = collector.mungee(comments);

                collector.save(id, cleanedComments);
            }

            // sleep to avoid usage limit exception
//            sleep(rateCheckStart);
            TimeUnit.SECONDS.sleep(5);
        }

        long end = System.currentTimeMillis();
        System.out.printf("stop time: %d %ntotal time: %d%n", end, end - start);
        System.out.println("count: " + source.getCount());

    }

    public static void sleep(long start) {
        long end = System.currentTimeMillis();
        double timeElapsed = (end - start) / 1000.0;
        double needToWait = 60.0 - timeElapsed;
        int sleeptime = (needToWait < 0) ? 60 : (int) Math.ceil(needToWait);
        System.out.println("*Need to sleep: " + sleeptime + " seconds");
        try {
            TimeUnit.SECONDS.sleep(sleeptime);;
        } catch (InterruptedException e) {
            System.out.println("Thread sleep encountered exception");
            e.printStackTrace();
        }
    }

}
