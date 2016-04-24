package edu.csula.datascience.r.models;

import java.util.Collection;

/**
 * Created by samskim on 4/21/16.
 */
public class Comment {
    private String id;
    private String author;
    private Collection<Comment> replies;
    private String body;
    private int score;
    private long timestamp;
    private int controversiality;

    public Comment(){

    }

    public Comment(String id, String author, Collection<Comment> replies, String body,
                   int score, long timestamp, int controversiality) {
        this.id = id;
        this.author = author;
        this.replies = replies;
        this.body = body;
        this.score = score;
        this.timestamp = timestamp;
        this.controversiality = controversiality;
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

    public Collection<Comment> getReplies() {
        return replies;
    }

    public void setReplies(Collection<Comment> replies) {
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

    public int getControversiality() {
        return controversiality;
    }

    public void setControversiality(int controversiality) {
        this.controversiality = controversiality;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", replies=" + replies +
                ", body='" + body + '\'' +
                ", score=" + score +
                ", timestamp=" + timestamp +
                ", controversiality=" + controversiality +
                '}';
    }
}
