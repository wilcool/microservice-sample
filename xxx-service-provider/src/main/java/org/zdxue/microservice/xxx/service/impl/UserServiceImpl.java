package org.zdxue.microservice.xxx.service.impl;

import org.springframework.stereotype.Component;
import org.zdxue.microservice.xxx.model.User;
import org.zdxue.microservice.xxx.service.UserService;

import com.alibaba.dubbo.config.annotation.Service;

@Service(interfaceClass = UserService.class)
@Component
public class UserServiceImpl implements UserService {

	@Override
	public User getUser(int id) {
		// TODO mock User
		User user = new User();
		user.setId(id);
		user.setName("user-" + id);
		return user;
	}

}
