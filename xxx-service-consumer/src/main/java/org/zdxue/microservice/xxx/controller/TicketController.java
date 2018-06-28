package org.zdxue.microservice.xxx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zdxue.microservice.xxx.model.Ticket;
import org.zdxue.microservice.xxx.model.User;
import org.zdxue.microservice.xxx.service.TicketService;
import org.zdxue.microservice.xxx.service.UserService;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * @author xuezd
 */
@RestController
public class TicketController {

    @Reference(url = "dubbo://127.0.0.1:20881")
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @GetMapping(path = "/ticket/{id}")
    public Ticket ticket(@PathVariable int id) {
        Ticket ticket = ticketService.getTicket(id);
        if (ticket != null) {
            User user = userService.getUser(ticket.getUserId()); // call remote
            ticket.setOwner(user);
        }

        return ticket;
    }

}
