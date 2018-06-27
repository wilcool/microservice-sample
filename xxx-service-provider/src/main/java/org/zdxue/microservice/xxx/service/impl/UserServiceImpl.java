package org.zdxue.microservice.xxx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zdxue.microservice.xxx.model.User;
import org.zdxue.microservice.xxx.repo.UserRepo;
import org.zdxue.microservice.xxx.service.UserService;

import com.alibaba.dubbo.config.annotation.Service;

@Service(interfaceClass = UserService.class)
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User getUser(int id) {
        return userRepo.getUser(id);
    }

}
