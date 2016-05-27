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
    private double title_sentiment;
    private int comments_count;

    // for old constructors
    public Post(String id, String author, String title, String selftext, String subreddit, String url,
                String domain, boolean is_self, String mediaType, List<Comment> comments,
                boolean isNSFW, long timestamp, int score) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.selftext = selftext;
        this.subreddit = subreddit;
        this.url = url;
        this.domain = domain;
        this.is_self = is_self;
        this.mediaType = mediaType;
        this.comments = comments;
        this.isNSFW = isNSFW;
        this.timestamp = timestamp;
        this.score = score;
    }


    public Post(String id, String author, String title, String selftext, String subreddit, String url,
                String domain, boolean is_self, String mediaType, List<Comment> comments,
                boolean isNSFW, long timestamp, int score, double title_sentiment, int comments_count) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.selftext = selftext;
        this.subreddit = subreddit;
        this.url = url;
        this.domain = domain;
        this.is_self = is_self;
        this.mediaType = mediaType;
        this.comments = comments;
        this.isNSFW = isNSFW;
        this.timestamp = timestamp;
        this.score = score;
        this.title_sentiment = title_sentiment;
        this.comments_count = comments_count;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean is_self() {
        return is_self;
    }

    public void setIs_self(boolean is_self) {
        this.is_self = is_self;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isNSFW() {
        return isNSFW;
    }

    public void setNSFW(boolean NSFW) {
        isNSFW = NSFW;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getTitle_sentiment() {
        return title_sentiment;
    }

    public void setTitle_sentiment(double title_sentiment) {
        this.title_sentiment = title_sentiment;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", selftext='" + selftext + '\'' +
                ", subreddit='" + subreddit + '\'' +
                ", url='" + url + '\'' +
                ", domain='" + domain + '\'' +
                ", is_self=" + is_self +
                ", mediaType='" + mediaType + '\'' +
                ", comments=" + comments +
                ", isNSFW=" + isNSFW +
                ", timestamp=" + timestamp +
                ", score=" + score +
                '}';
    }
}
