package edu.csula.datascience.r;

import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import edu.csula.datascience.r.acquisition.MongoDriver;

/**
 * Created by samskim on 4/24/16.
 */
public class MongoConnectionTest {
    MongoDatabase db;
    MongoDriver md;

    @Before
    public void setup(){
        md = new MongoDriver();
        db = md.getDb();
    }

    @Test
    public void mongoEnvTest(){
        Assert.assertEquals("redditUser", md.readDatabaseCredentials().get("MONGO_USERNAME"));
    }

    @Test
    public void mongoDBConnectionTest(){
        Assert.assertEquals("reddit", db.getName());
    }

    @Test
    public void mongoConnectionTest(){
        MongoDriver mongoDriver = new MongoDriver();
        Map<String, String> map = new HashMap<>();
        Assert.assertTrue(db.getCollection("posts_2016_04_23").count() > 6000000);
    }

    @Test
    public void mongoCollectionTest(){
        Document doc = db.getCollection("posts_2016_4_23").find().first();
        System.out.println(doc.get("id").toString());
        Assert.assertTrue(doc.get("id").toString().length() < 7);
    }


}
