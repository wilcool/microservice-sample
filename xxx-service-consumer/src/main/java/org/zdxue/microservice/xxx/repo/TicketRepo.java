/**
 * 
 */
package org.zdxue.microservice.xxx.repo;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.zdxue.microservice.xxx.dao.TicketMapper;
import org.zdxue.microservice.xxx.model.Ticket;

import com.alibaba.fastjson.JSON;

/**
 * @author zdxue
 *
 */
@Component
public class TicketRepo {
    private static final Logger logger = LoggerFactory.getLogger(TicketRepo.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TicketMapper ticketMapper;

    public Ticket getTicket(int id) {
        String key = "t:" + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(json)) {
            logger.debug("load from database, id: {}", id);
            Ticket ticket = ticketMapper.findById(id);
            if (ticket != null) {
                stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(ticket), 600, TimeUnit.SECONDS);
            }

            return ticket;
        }

        logger.debug("load from Cache");
        return JSON.parseObject(json, Ticket.class);
    }
}
