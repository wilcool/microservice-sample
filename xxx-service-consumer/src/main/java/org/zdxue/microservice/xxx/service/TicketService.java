/**
 * 
 */
package org.zdxue.microservice.xxx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zdxue.microservice.xxx.model.Ticket;
import org.zdxue.microservice.xxx.repo.TicketRepo;

/**
 * @author zdxue
 *
 */
@Service
public class TicketService {

    @Autowired
    private TicketRepo ticketRepo;

    public Ticket getTicket(int id) {
        return ticketRepo.getTicket(id);
    }

}
