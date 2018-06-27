/**
 * 
 */
package org.zdxue.microservice.xxx.repo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zdxue.microservice.xxx.cache.redis.JedisTemplate;
import org.zdxue.microservice.xxx.model.Ticket;
import org.zdxue.microservice.xxx.mybatis.mapper.TicketMapper;

import com.alibaba.fastjson.JSON;

/**
 * @author zdxue
 *
 */
@Component
public class TicketRepo {
    private static final Logger logger = LoggerFactory.getLogger(TicketRepo.class);

    @Autowired
    private JedisTemplate jedisTemplate;

    @Autowired
    private TicketMapper ticketMapper;

    public Ticket getTicket(int id) {
        String key = "t:" + id;
        String json = jedisTemplate.get(key);
        if (StringUtils.isEmpty(json)) {
            logger.debug("load from database, id: {}", id);
            Ticket ticket = ticketMapper.findById(id);
            if (ticket != null) {
                jedisTemplate.set(key, JSON.toJSONString(ticket), 600);
            }

            return ticket;
        }

        logger.debug("load from Cache");
        return JSON.parseObject(json, Ticket.class);
    }
}
