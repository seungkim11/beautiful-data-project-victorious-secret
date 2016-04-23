package edu.csula.datascience.r.acquisition;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import edu.csula.datascience.acquisition.Collector;
import edu.csula.datascience.r.models.Comment;
import edu.csula.datascience.r.models.Post;
import net.dean.jraw.models.Submission;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by tj on 4/21/16.
 */
public class SubmissionCollector implements Collector<Post, Submission> {

  private final String USERNAME = "MONGO_USERNAME";
  private final String PASSWORD = "MONGO_PASSWORD";
  private final String HOST = "MONGO_HOST";
  private final String DB_NAME = "MONGO_DB_NAME";
  private MongoClient mongoClient;
  private MongoDatabase db;

  @Override
  public Collection<Post> mungee(Collection<Submission> src) {
    List<Post> posts = new ArrayList<>(src.size());

    String id;
    String author;
    String title;
    String selftext;
    String subreddit;
    String url;
    String domain;
    boolean isSelf;
    String mediaType;
    List<Comment> comments;
    boolean isNSFW;
    long timestamp;
    int score;
    Post post;
    for(Submission sub : src){
      id = sub.getId();
      author = sub.getAuthor();
      title = sub.getTitle();
      selftext = sub.getSelftext(); // may need to sanitize html
      subreddit = sub.getSubredditName();
      url = sub.getUrl();
      domain = sub.getDomain();
      isSelf = sub.isSelfPost(); // is this correct?
      mediaType = "text";// one of text or image?
      // FIXME have a method to download all comments for this submission
      comments = new ArrayList<>(0);
      isNSFW = sub.isNsfw();
      timestamp = sub.getCreatedUtc().getTime();
      score = sub.getScore();
      post = new Post(id, author, title, selftext, subreddit, url, domain, isSelf, mediaType, comments,
          isNSFW, timestamp, score);
      posts.add(post);
    }
    return posts;
  }

  @Override
  public void save(Collection<Post> data) {
    String collection = "posts";
    System.out.println("saving data");
    List<Document> docs;
    docs = data.stream()
            .map(post -> createDocument(post))
            .collect(Collectors.toList());
    if(docs.size() < 1){
      return;
    }

    connectDatabase();
//    insertMany(docs, collection);
    for(Document doc : docs){
      replaceOne(doc, collection);
    }
    closeDatabase();
  }

  private String connectDatabase(){
    Map<String, String> envMap = readDatabaseCredentials();
    String dbUser = envMap.get(USERNAME);
    String dbPass = envMap.get(PASSWORD);
    String host = envMap.get(HOST);
    String dbName = envMap.get(DB_NAME);
    MongoClientURI uri = new MongoClientURI(
            "mongodb://" + dbUser + ":" + dbPass + "@" + host + "/?authSource=" + dbName);
    mongoClient = new MongoClient(uri);
    db = mongoClient.getDatabase(dbName);
    return dbName;
  }

  private void closeDatabase(){
    mongoClient.close();
  }

  private Map<String, String> readDatabaseCredentials(){
    Map<String, String> envMap = new HashMap<>(4);
    checkIfSourced(USERNAME);
    envMap.put(USERNAME, System.getenv(USERNAME));
    envMap.put(PASSWORD, System.getenv(PASSWORD));
    envMap.put(HOST, System.getenv(HOST));
    envMap.put(DB_NAME, System.getenv(DB_NAME));
    return envMap;
  }

  private Document createDocument(Post post){
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    String json = gson.toJson(post);
    Document doc = Document.parse(json);
    return doc;
  }

  private void insertMany(List<Document> documents, String collection){
    db.getCollection(collection).insertMany(documents);
  }

  private void insertOne(Document document, String collection){
    db.getCollection(collection).insertOne(document);
  }

  private void replaceOne(Document document, String collection){
    UpdateResult result = db.getCollection(collection).replaceOne(Filters.eq("id", document.get("id")), document);
    long resultsMatched = result.getMatchedCount();
    if(resultsMatched < 1L){
      insertOne(document, collection);
    }
  }

  private void checkIfSourced(String variableName){
    if(System.getenv(variableName) == null){
      System.err.printf("Could not find environment variable %s. Did you source the environment?", variableName);
      System.err.println("source <filename>.env");
      System.exit(1);
    }
  }
}
