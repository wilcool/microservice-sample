/**
 * 
 */
package org.zdxue.microservice.xxx.schedule.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author zdxue
 */
public class MongoRepo {

    @Autowired
    private MongoTemplate mongoTemplate;
    
}
