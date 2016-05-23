package edu.csula.datascience.r;

import edu.csula.datascience.r.es.MongoToElasticSearch;

/**
 * Created by samskim on 5/22/16.
 */
public class ElasticSearchMigrator {

    public static void main(String[] args){
        MongoToElasticSearch mes = new MongoToElasticSearch();

        mes.migrateToEs();
	System.out.println("Migration Finished");
    }
}
