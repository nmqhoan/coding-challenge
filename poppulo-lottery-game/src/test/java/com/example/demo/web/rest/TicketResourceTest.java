package com.example.demo.web.rest;

import com.example.demo.configuration.ApplicationProperties;
import com.example.demo.domain.Line;
import com.example.demo.domain.Ticket;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.TicketService;
import com.example.demo.web.rest.errors.BadRequestException;
import com.example.demo.web.rest.errors.TicketCheckedException;
import com.example.demo.web.rest.errors.TicketNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties({ApplicationProperties.class})
@Transactional
class TicketResourceTest {
    private static final int DEFAULT_NUM_OF_LINE = 3;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    /**
     * Create an ticket entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity() {
        List<int[]> tests = new ArrayList<int[]>(){{
            add(new int[]{0,0,0});
            add(new int[]{2,0,2});
            add(new int[]{1,1,0});
        }};
        List<Line> lines = new ArrayList<>();
        for(int[] arr: tests) {
            Line line = new Line();
            line.setNumbers(arr);
            lines.add(line);
        }
        Ticket ticket = new Ticket(lines);
        return ticket;
    }

    @BeforeEach
    public void initTest() {ticket = createEntity();}

    @Test
    void createTicket() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();
        // Create the ticket
        restTicketMockMvc.perform(post("/api/ticket").param("numOfLine",String.valueOf(DEFAULT_NUM_OF_LINE))
                         .contentType(MediaType.APPLICATION_JSON))
                         .andExpect(status().isOk());
        // Validate the ticket in the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeCreate + 1);
        Ticket testTicket = tickets.get(tickets.size() - 1);
        assertThat(testTicket.isChecked()).isEqualTo(false);
        assertThat(testTicket.getLines().size()).isEqualTo(DEFAULT_NUM_OF_LINE);
    }

    @Test
    void createTicketWithInvalidNumberOfLines() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();
        // Create the ticket
        restTicketMockMvc.perform(post("/api/ticket").param("numOfLine",String.valueOf(-1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals("Invalid request",result.getResolvedException().getMessage()));
        // Validate the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void updateTicket() throws Exception {
        // Initialize the database
        ticketRepository.save(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        int lineToAdd = 2;

        // Update the ticket
        restTicketMockMvc.perform(put("/api/ticket/{id}",ticket.getId()).param("numOfLine", String.valueOf(lineToAdd)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        // Validate the ticket in the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = tickets.get(tickets.size() - 1);
        assertThat(testTicket.isChecked()).isEqualTo(false);
        assertThat(testTicket.getLines().size()).isEqualTo(DEFAULT_NUM_OF_LINE+lineToAdd);
    }

    @Test
    void updateNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        int lineToAdd = 2;

        // Update the ticket
        restTicketMockMvc.perform(put("/api/ticket/{id}",-1L).param("numOfLine", String.valueOf(lineToAdd))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TicketNotFoundException))
                .andExpect(result -> assertEquals("Ticket not found",result.getResolvedException().getMessage()));

        // Validate the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void updateCheckedTicket() throws Exception {
        // Initialize the database
        ticket.setChecked();
        ticketRepository.save(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        int lineToAdd = 2;

        // Update the ticket
        restTicketMockMvc.perform(put("/api/ticket/{id}",ticket.getId()).param("numOfLine", String.valueOf(lineToAdd))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TicketCheckedException))
                .andExpect(result -> assertEquals("Ticket was already checked. It can not be amended",result.getResolvedException().getMessage()));

        // Validate the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void getAllTickets() throws Exception {
        // Initialize the database
        ticketRepository.save(ticket);

        // Get all tickets
        restTicketMockMvc.perform(get("/api/ticket/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(ticket.getId().intValue()))
                .andExpect(jsonPath("$.[*].checked").value(false));
    }

    @Test
    void getTicket() throws Exception {
        // Initialize the database
        ticketRepository.save(ticket);
        // Get ticket
        restTicketMockMvc.perform(get("/api/ticket/{id}", ticket.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(ticket.getId().intValue()))
                .andExpect(jsonPath("$.checked").value(false));
    }

    @Test
    void getNonExistingTicket() throws Exception {
        // Get ticket
        restTicketMockMvc.perform(get("/api/ticket/{id}", -1L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TicketNotFoundException))
                .andExpect(result -> assertEquals("Ticket not found",result.getResolvedException().getMessage()));
    }

    @Test
    void updateTicketStatus() throws Exception {
        // Initialize the database
        ticketRepository.save(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket
        restTicketMockMvc.perform(put("/api/status/{id}",ticket.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Validate the ticket in the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = tickets.get(tickets.size() - 1);
        assertThat(testTicket.isChecked()).isEqualTo(true);
    }

    @Test
    void updateNonExistingTicketStatus() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket
        restTicketMockMvc.perform(put("/api/status/{id}",-1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TicketNotFoundException))
                .andExpect(result -> assertEquals("Ticket not found",result.getResolvedException().getMessage()));

        // Validate the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeUpdate);
    }
}