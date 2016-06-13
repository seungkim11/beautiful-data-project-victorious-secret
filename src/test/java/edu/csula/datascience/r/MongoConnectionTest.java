package edu.csula.datascience.r;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import edu.csula.datascience.r.acquisition.MyMongoDriver;

/**
 * Created by samskim on 4/24/16.
 */

@Ignore
public class MongoConnectionTest {
    MongoDatabase db;
    MyMongoDriver md;
    MongoCollection collection;

    @Before
    public void setup(){
        md = new MyMongoDriver();
        db = md.getDb();
        collection = db.getCollection("posts_2016_04_23");
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
        Map<String, String> map = new HashMap<>();
        Assert.assertTrue(collection.count() > 6000000);
    }

    @Test
    public void mongoCollectionTest(){

        Document doc = (Document) collection.find().first();

        Assert.assertEquals(doc.get("id").toString().length(), 6);
    }


}
