package edu.csula.datascience.r;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dean.jraw.models.Submission;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.csula.datascience.r.acquisition.SubmissionCollector;
import edu.csula.datascience.r.models.Post;

import static org.junit.Assert.*;

/**
 * Created by samskim on 4/24/16.
 */
public class SubmissionCollectorTest {

    List<Submission> list;

    @Before
    public void setup(){

        list = new ArrayList<>();

        String submissionResponseString = "{\n" +
                "\"domain\": \"self.redditdev\",\n" +
                "\"banned_by\": null,\n" +
                "\"media_embed\": {},\n" +
                "\"subreddit\": \"redditdev\",\n" +
                "\"selftext_html\": \"&lt;!-- SC_OFF --&gt;&lt;div class=\\\"md\\\"&gt;&lt;p&gt;Hello, I am a reddit noob and I&amp;#39;m trying to build an app that scrapes comments on reddit posts. I want to play by the rules (&lt;a href=\\\"https://github.com/reddit/reddit/wiki/API\\\"&gt;https://github.com/reddit/reddit/wiki/API&lt;/a&gt;) , which says &lt;/p&gt;\\n\\n&lt;ul&gt;\\n&lt;li&gt;Clients must authenticate with OAuth2&lt;/li&gt;\\n&lt;li&gt;Clients connecting via OAuth2 may make up to 60 requests per minute. &lt;/li&gt;\\n&lt;/ul&gt;\\n\\n&lt;p&gt;I have successfully got authentication token through plain http request in java.\\nHowever, when I type a url on my browser, appending .json after a post name,  it responds with a json blob of the page, without having to go through OAuth2.&lt;/p&gt;\\n\\n&lt;p&gt;Here is my question:&lt;/p&gt;\\n\\n&lt;ol&gt;\\n&lt;li&gt;&lt;p&gt;Am I supposed to embed my token in the request header, even though it seems like it doesn&amp;#39;t require an authentication to scrape the webpage?  ( for example, &lt;a href=\\\"https://www.reddit.com/r/redditdev/4fx4so.json\\\"&gt;https://www.reddit.com/r/redditdev/4fx4so.json&lt;/a&gt;)&lt;/p&gt;&lt;/li&gt;\\n&lt;li&gt;&lt;p&gt;For each request for getting comments of a post, does it count as 1 request in the rate limit, or something else?&lt;/p&gt;&lt;/li&gt;\\n&lt;li&gt;&lt;p&gt;What could be the best way to scrape comments of a post?&lt;/p&gt;&lt;/li&gt;\\n&lt;/ol&gt;\\n\\n&lt;p&gt;If there is any rules/info that I missed, kindly let me know. Thanks&lt;/p&gt;\\n&lt;/div&gt;&lt;!-- SC_ON --&gt;\",\n" +
                "\"selftext\": \"Hello, I am a reddit noob and I'm trying to build an app that scrapes comments on reddit posts. I want to play by the rules (https://github.com/reddit/reddit/wiki/API) , which says \\n\\n- Clients must authenticate with OAuth2\\n- Clients connecting via OAuth2 may make up to 60 requests per minute. \\n\\nI have successfully got authentication token through plain http request in java.\\nHowever, when I type a url on my browser, appending .json after a post name,  it responds with a json blob of the page, without having to go through OAuth2.\\n\\nHere is my question:\\n\\n1. Am I supposed to embed my token in the request header, even though it seems like it doesn't require an authentication to scrape the webpage?  ( for example, https://www.reddit.com/r/redditdev/4fx4so.json)\\n\\n2. For each request for getting comments of a post, does it count as 1 request in the rate limit, or something else?\\n\\n3. What could be the best way to scrape comments of a post?\\n\\nIf there is any rules/info that I missed, kindly let me know. Thanks\",\n" +
                "\"likes\": true,\n" +
                "\"suggested_sort\": null,\n" +
                "\"user_reports\": [],\n" +
                "\"secure_media\": null,\n" +
                "\"link_flair_text\": null,\n" +
                "\"id\": \"4fx4so\",\n" +
                "\"from_kind\": null,\n" +
                "\"gilded\": 0,\n" +
                "\"archived\": false,\n" +
                "\"clicked\": false,\n" +
                "\"report_reasons\": null,\n" +
                "\"author\": \"seungkim11\",\n" +
                "\"media\": null,\n" +
                "\"name\": \"t3_4fx4so\",\n" +
                "\"score\": 9,\n" +
                "\"approved_by\": null,\n" +
                "\"over_18\": false,\n" +
                "\"hidden\": false,\n" +
                "\"thumbnail\": \"\",\n" +
                "\"subreddit_id\": \"t5_2qizd\",\n" +
                "\"edited\": 1461305413,\n" +
                "\"link_flair_css_class\": null,\n" +
                "\"author_flair_css_class\": null,\n" +
                "\"downs\": 0,\n" +
                "\"mod_reports\": [],\n" +
                "\"secure_media_embed\": {},\n" +
                "\"saved\": false,\n" +
                "\"removal_reason\": null,\n" +
                "\"stickied\": false,\n" +
                "\"from\": null,\n" +
                "\"is_self\": true,\n" +
                "\"from_id\": null,\n" +
                "\"permalink\": \"/r/redditdev/comments/4fx4so/question_about_reddit_scraping_rules/\",\n" +
                "\"locked\": false,\n" +
                "\"hide_score\": false,\n" +
                "\"created\": 1461332441,\n" +
                "\"url\": \"https://www.reddit.com/r/redditdev/comments/4fx4so/question_about_reddit_scraping_rules/\",\n" +
                "\"author_flair_text\": null,\n" +
                "\"quarantine\": false,\n" +
                "\"title\": \"question about reddit scraping rules\",\n" +
                "\"created_utc\": 1461303641,\n" +
                "\"ups\": 9,\n" +
                "\"upvote_ratio\": 1,\n" +
                "\"num_comments\": 6,\n" +
                "\"visited\": false,\n" +
                "\"num_reports\": null,\n" +
                "\"distinguished\": null\n" +
                "}";


        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode node = null;
        try {
            node = objectMapper.readTree(submissionResponseString);
            Submission submission = new Submission(node);

            list.add(submission);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSubmissionMungee(){

        SubmissionCollector collector = new SubmissionCollector();
        List<Post> cleanedPosts = (List<Post>) collector.mungee(list);
        Assert.assertEquals("seungkim11", cleanedPosts.get(0).getAuthor());

    }

}