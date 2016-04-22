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
    private int controversiality;
}
