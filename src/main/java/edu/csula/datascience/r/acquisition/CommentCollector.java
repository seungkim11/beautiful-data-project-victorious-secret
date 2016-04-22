package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Collector;
import edu.csula.datascience.r.models.Comment;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by samskim on 4/21/16.
 */
public class CommentCollector implements Collector<Comment, JSONObject> {


    // TODO: mungee jsonobject, add to list and return
    @Override
    public Collection<Comment> mungee(Collection<JSONObject> src) {
        List<Comment> list = new ArrayList<>();
        for (JSONObject commentNode : src) {

            // type t1 = comment
            // type t3 = post
            // type listing = array
            if (commentNode.get("kind").equals("t1")) {

                JSONObject comment = (JSONObject) commentNode.get("data");

                String id = (String) comment.get("id");
                String author = (String) comment.get("author");
                String body = (String) comment.get("body");
                int score = (int) comment.get("score");

                // FIXME: do we need controversiality? everything is 0. maybe need to be removed
                int controversiality = (int) comment.get("controversiality");
                long timestamp = (long) (double) comment.get("created_utc");

                Comment c = new Comment(id, author, replies, body, score, timestamp, controversiality);

                // TODO: recursively parse comments
                if (hasReplies(comment.get("replies"))) {

                }

                System.out.println("id: " + id + ", author: " + ", body: " + body +
                        ", score: " + score + ", controversiality: " + controversiality +
                        ", timestamp: " + timestamp + ", likes: " + comment.get("likes") + ", replies: " + comment.get("replies") + "\n");

                list.add(c);

            }


        }

        return list;
    }

    @Override
    public void save(Collection<Comment> data) {

    }

    public boolean hasReplies(Object repliesObj){
        return !repliesObj.toString().trim().isEmpty();
    }

}
