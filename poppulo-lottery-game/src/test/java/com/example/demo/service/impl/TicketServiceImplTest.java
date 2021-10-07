package com.example.demo.service.impl;

import com.example.demo.domain.Line;
import com.example.demo.domain.Ticket;
import com.example.demo.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class TicketServiceImplTest {
    @InjectMocks
    private TicketServiceImpl ticketService;
    @Mock
    private TicketRepository ticketRepository;

    private Ticket ticket;
    final int DEFAULT_NUM_OF_LINES = 3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticket = ticketService.generateTicket(DEFAULT_NUM_OF_LINES);
    }

    @Test
    void generateTicket() {
        Ticket gotTicket = ticketService.generateTicket(DEFAULT_NUM_OF_LINES);
        assertNotNull(gotTicket);
        assertEquals(gotTicket.getLines().size(),DEFAULT_NUM_OF_LINES);
        assertFalse(gotTicket.isChecked());
    }

    @Test
    void generateLine() {
        Line line = ticketService.generateLine();
        assertNotNull(line);
        assertEquals(line.getNumbers().length,3);
    }

    @Test
    void save() {
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        Ticket createdTicket = ticketService.save(ticket);
        assertNotNull(createdTicket);
        assertEquals(createdTicket.isChecked(),ticket.isChecked());
        assertEquals(createdTicket.getLines(),ticket.getLines());
    }

    @Test
    void update() {
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        Ticket savedTicket = ticketService.update(ticket);
        assertNotNull(savedTicket);
        assertEquals(savedTicket.isChecked(),ticket.isChecked());
        assertEquals(savedTicket.getLines(),ticket.getLines());
    }

    @Test
    void findOne() {
        final Long id = 1L;
        ticket.setId(id);
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));
        final Optional<Ticket> wantTicket = ticketService.findOne(id);
        assertNotNull(wantTicket);
    }

    @Test
    void findAll() {
        List<Ticket> tickets = new ArrayList<Ticket>();
        tickets.add(ticket);
        when(ticketRepository.findAll()).thenReturn(tickets);
        List<Ticket> wantTickets = ticketService.findAll();
        assertEquals(tickets,wantTickets);
    }
}