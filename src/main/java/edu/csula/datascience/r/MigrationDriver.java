package edu.csula.datascience.r;

import edu.csula.datascience.r.es.MongoToElasticSearch;

/**
 * Created by samskim on 5/22/16.
 */
public class MigrationDriver {

    public static void main(String[] args){

        MongoToElasticSearch migrator = new MongoToElasticSearch(null);

        if (args.length > 0 && args[0] != null) {
            migrator = new MongoToElasticSearch(args[0]);
        }

        migrator.migrateToEs();
        System.out.println("Migration finished");
    }
}
