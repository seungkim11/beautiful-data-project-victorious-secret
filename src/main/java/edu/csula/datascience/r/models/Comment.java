package edu.csula.datascience.r.models;

import java.util.List;

/**
 * Created by samskim on 4/21/16.
 */
public class Comment {
    private String id;
    private String author;
    private List<Comment> replies;
    private String body;
    private int score;
    private long timestamp;
//    private int controversiality;

    public Comment(){

    }

    public Comment(String id, String author, List<Comment> replies, String body,
                   int score, long timestamp) {
        this.id = id;
        this.author = author;
        this.replies = replies;
        this.body = body;
        this.score = score;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

//    public int getControversiality() {
//        return controversiality;
//    }
//
//    public void setControversiality(int controversiality) {
//        this.controversiality = controversiality;
//    }
}