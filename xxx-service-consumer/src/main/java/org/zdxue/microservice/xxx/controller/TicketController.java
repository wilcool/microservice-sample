package org.zdxue.microservice.xxx.controller;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zdxue.microservice.xxx.model.Ticket;
import org.zdxue.microservice.xxx.model.User;
import org.zdxue.microservice.xxx.service.UserService;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * @author xuezd
 */
@RestController
public class TicketController {

    private static final Random RND = new Random();

    @Reference(url = "dubbo://127.0.0.1:20880")
    private UserService userService;

    @GetMapping(path = "/ticket/{id}")
    public Ticket ticket(@PathVariable int id) {
        User user = userService.getUser(RND.nextInt(10));

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setCode("T-" + id + "-" + user.getId());
        ticket.setOwner(user);

        return ticket;
    }

}
