package edu.csula.datascience.r.es;

import com.google.api.client.util.Lists;
import com.google.appengine.repackaged.com.google.gson.Gson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import java.util.List;

import edu.csula.datascience.r.acquisition.CommentCollector;
import edu.csula.datascience.r.models.Comment;
import edu.csula.datascience.r.models.Post;

/**
 * Created by samskim on 5/22/16.
 */
public class MongoToElasticSearch {
    private final static String indexName = "reddit";
    private final static String typeName = "post";
    private long count;

    public MongoToElasticSearch() {
        count = 0;
    }

    public void migrateToEs() {

        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "victorious-secret").build();

        Client client = TransportClient.builder().settings(settings).build();

        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {

                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

                        System.out.println("Added data: " + executionId + ", count: " + count);
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        System.out.println("Facing error while importing data to elastic search");
                        failure.printStackTrace();
                    }
                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();

        Gson gson = new Gson();


        CommentCollector collector = new CommentCollector();

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("reddit");
        MongoCollection collection = db.getCollection("posts_2016_04_23");

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Post post = parsePost(cursor.next());
            count++;
            bulkProcessor.add(new IndexRequest(indexName, typeName)
                    .source(gson.toJson(post))
            );
        }

    }


    public Post parsePost(Document doc) {
        String id = (String) doc.get("id");
        String author = (String) doc.get("author");
        String title = (String) doc.get("title");
        String selftext = (String) doc.get("selftext");
        String subreddit = (String) doc.get("subreddit");
        String url = (String) doc.get("url");
        String domain = (String) doc.get("domain");
        boolean is_self = (boolean) doc.get("is_self");
        String mediaType = (String) doc.get("mediaType");
        boolean isNSFW = (boolean) doc.get("isNSFW");
        long timestamp = (long) doc.get("timestamp");
        int score = (int) doc.get("score");

        List<Document> commentDocs = (List<Document>) doc.get("comments");
        List<Comment> comments = null;
        if (commentDocs != null && commentDocs.size() > 0) {
            comments = Lists.newArrayList();
            ;

            for (Document d : commentDocs) {
                comments.add(parseComment(d));
            }
        }

        return new Post(id, author, title, selftext, subreddit, url, domain, is_self,
                mediaType, comments, isNSFW, timestamp, score);
    }


    private Comment parseComment(Document comment) {

        Comment c = null;

        String id = (String) comment.get("comment_id");
        String author = (String) comment.get("author");
        String body = (String) comment.get("body");
        int score = (int) comment.get("score");
        long timestamp = (long) comment.get("timestamp");
        int controversiality = (int) comment.get("controversiality");
        List<Document> replyDocs = (List<Document>) comment.get("replies");

        List<Comment> replies = null;

        if (replyDocs != null && replyDocs.size() > 0) {

            replies = Lists.newArrayList();

            for (Document reply : replyDocs) {
                replies.add(parseComment(reply));
            }
        }

        c = new Comment(id, author, replies, body, score, timestamp, controversiality);

//        System.out.println("id: " + id + ", author: " + ", body: " + body +
//                ", score: " + score + ", controversiality: " + controversiality +
//                ", timestamp: " + timestamp + ", likes: " + comment.get("likes") + ", replies: " + comment.get("replies") + "\n");

        return c;

    }


}
