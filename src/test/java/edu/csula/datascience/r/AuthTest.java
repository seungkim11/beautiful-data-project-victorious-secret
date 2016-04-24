package edu.csula.datascience.r;

import edu.csula.datascience.r.auth.RedditOAuthHttp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by samskim on 4/24/16.
 */
public class AuthTest {
    private final String ENV_TEST = "TESTING_ENV";
    RedditOAuthHttp oAuthHttp;

    @Test
    public void getEnv(){
        String env = System.getenv(ENV_TEST);
        Assert.assertEquals("victorious-secret", env);
    }

    @Before
    public void setup(){
        oAuthHttp = new RedditOAuthHttp();
        oAuthHttp.getEnvKeys();
    }

    @Test
    public void RedditHttpAuth(){
        String token = oAuthHttp.getToken();
        //Assert.assertTrue(token.matches("53844815[\\s\\S]+"));
        Assert.assertTrue(!token.isEmpty());
    }
}
