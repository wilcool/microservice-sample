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
import org.zdxue.microservice.xxx.model.User;
import org.zdxue.microservice.xxx.mybatis.mapper.UserMapper;

import com.alibaba.fastjson.JSON;

/**
 * @author zdxue
 *
 */
@Component
public class UserRepo {
    private static final Logger logger = LoggerFactory.getLogger(UserRepo.class);

    @Autowired
    private JedisTemplate jedisTemplate;

    @Autowired
    private UserMapper userMapper;

    public User getUser(int id) {
        String key = "u:" + id;
        String json = jedisTemplate.get(key);
        if (StringUtils.isEmpty(json)) {
            logger.debug("load from database, id: {}", id);
            User user = userMapper.findById(id);
            if (user != null) {
                jedisTemplate.set(key, JSON.toJSONString(user), 600);
            }

            return user;
        }

        logger.debug("load from Cache");
        return JSON.parseObject(json, User.class);
    }

}