package edu.csula.datascience.r.acquisition;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.csula.datascience.acquisition.Collector;
import edu.csula.datascience.r.models.Comment;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by samskim on 4/21/16.
 */
public class CommentCollector implements Collector<Comment, JSONObject> {
    MongoDatabase db;
    MongoCollection collection;

    public CommentCollector(){

    }

    public CommentCollector(MongoDatabase db){
        this.db = db;
        this.collection = db.getCollection("posts_2016_04_23");

    }

    @Override
    public Collection<Comment> mungee(Collection<JSONObject> src) {
        // this override method not applicable in my case
        // check below mungee method
        return null;
    }

    public Collection<Comment> mungee(JSONArray src){
        List<Comment> list = new ArrayList<>();
        Iterator<Object> iterator = src.iterator();

        while (iterator.hasNext()){
            JSONObject commentNode = (JSONObject) iterator.next();

            // type t1 = comment
            // type t3 = post
            // type listing = array
            if (commentNode.get("kind").equals("t1")) {

                JSONObject commentObj = (JSONObject) commentNode.get("data");
                Comment c = parseComment(commentObj);
                list.add(c);

            }
        }

        return list;
    }

    // the input blobs contains comment blobs of 1000 submissions
    public Collection<JSONArray> splitSubmissionComments(Collection<JSONObject> blobs){

        Collection<JSONArray> commentsList = new ArrayList<>();

//        // for comment blob of each submission
//        for (JSONObject blob: blobs){
//
//            // get data object
//            JSONObject data = (JSONObject) blob.get("data");
//
//            // get children (aka comment objects)
//            JSONArray comments = (JSONArray) data.get("children");
//
//            // now "comments" is list of all comments in a submission
//            commentsList.add(comments);
//        }

        return commentsList;
    }


    public Map<ObjectId, JSONArray> splitSubmissionComments(Map<ObjectId, JSONObject> blobs){

        Map<ObjectId, JSONArray> commentsList = new HashMap<>();

        // for comment blob of each submission
        for (ObjectId id: blobs.keySet()){

            JSONObject blob = blobs.get(id);

            // get data object
            JSONObject data = (JSONObject) blob.get("data");

            // get children (aka comment objects)
            JSONArray comments = (JSONArray) data.get("children");

            // now "comments" is list of all comments in a submission
            commentsList.put(id, comments);
        }

        return commentsList;
    }


    private Comment parseComment(JSONObject comment) {
        // type t1 = comment
        // type t3 = post
        // type listing = array
        Comment c = null;

        String id = (String) comment.get("id");
        String author = (String) comment.get("author");
        String body = (String) comment.get("body");
        int score = (int) comment.get("score");
        long timestamp = (long) (double) comment.get("created_utc");
        int controversiality = (int) comment.get("controversiality");
        Collection<Comment> replies = new ArrayList<>();
        // FIXME: do we need controversiality? everything is 0. maybe need to be removed

        // TODO: recursively parse comments
        if (hasReplies(comment.get("replies"))) {
//            replies = getComments(comment.get("replies"));
            JSONObject replyblob = (JSONObject) comment.get("replies");
            JSONObject data = (JSONObject) replyblob.get("data");
            JSONArray comments = (JSONArray) data.get("children");

            replies = mungee(comments);
        }

        c = new Comment(id, author, replies, body, score, timestamp, controversiality);

//            System.out.println("id: " + id + ", author: " + ", body: " + body +
//                    ", score: " + score + ", controversiality: " + controversiality +
//                    ", timestamp: " + timestamp + ", likes: " + comment.get("likes") + ", replies: " + comment.get("replies") + "\n");

        return c;

    }

    private void setCollection(String collectionName){

    }

    private List<Comment> getComments(Object blob) {
        // get data object
        JSONObject jsonObject = (JSONObject) blob;
        JSONObject data = (JSONObject) jsonObject.get("data");

        // get children (aka comment objects)
        List<Comment> comments = (ArrayList<Comment>) data.get("children");
        return comments;
    }

    @Override
    public void save(Collection<Comment> data) {

//        System.out.println("saving data");
//
//        List<Document> documents = data.stream()
//                .map(item -> new Document()
//                        .append("comment_id", item.getId())
//                        .append("author", item.getAuthor())
//                        .append("body", item.getBody())
//                        .append("score", item.getScore())
//                        .append("timestamp", item.getTimestamp())
//                        .append("controversiality", item.getControversiality())
//                        .append("replies", item.getReplies()))
//                .collect(Collectors.toList());
//
//        collection.insertMany(documents);

    }

    public boolean hasReplies(Object repliesObj){
        return !repliesObj.toString().trim().isEmpty();
    }

    public void save(ObjectId id, Collection<Comment> data) {
        List<Document> documents = data.stream()
                .map(item -> new Document()
                        .append("comment_id", item.getId())
                        .append("author", item.getAuthor())
                        .append("body", item.getBody())
                        .append("score", item.getScore())
                        .append("timestamp", item.getTimestamp())
                        .append("controversiality", item.getControversiality())
                        .append("replies", item.getReplies()))
                .collect(Collectors.toList());

        Document doc = (Document) collection.find(eq("_id", id)).first();
        doc.append("comments", documents);

        System.out.println(doc);

//        collection.replaceOne(eq("_id", id), doc);

    }
}
