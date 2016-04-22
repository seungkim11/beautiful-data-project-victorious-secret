package edu.csula.datascience.r.models;

import java.util.List;

/**
 * Created by samskim on 4/21/16.
 */
public class Post {
    private String id;
    private String author;
    private String title;
    private String selftext;
    private String subreddit;
    private String url;
    private String domain;
    private boolean is_self; // is this a selfpost or a link?
    private String mediaType; // ["TEXT", "IMAGE",  "VIDEO"]
    private List<Comment> comments;
    private boolean isNSFW;
    private long timestamp;
    private int score;
}
