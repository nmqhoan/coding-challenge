package com.example.demo.service;


import com.example.demo.domain.Line;
import com.example.demo.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket save(Ticket ticket);

    Ticket update(Ticket ticket);

    Optional<Ticket> findOne(Long id);

    Ticket generateTicket(int numOfLine);

    public Line generateLine();

    List<Ticket> findAll();
}
