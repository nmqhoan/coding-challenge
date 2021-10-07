package com.example.demo.web.rest;

import com.example.demo.domain.Ticket;
import com.example.demo.service.TicketService;
import com.example.demo.web.rest.errors.BadRequestException;
import com.example.demo.web.rest.errors.TicketCheckedException;
import com.example.demo.web.rest.errors.TicketNotFoundException;
import com.example.demo.web.rest.response.CheckedTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TicketResource {

	private final Logger log = LoggerFactory.getLogger(TicketResource.class);

	private static final String ENTITY_NAME = "ticket";

	@Autowired
	TicketService ticketService;

	/**
	 * {@code POST  /tickets} : Create a new ticket.
	 *
	 * @param numOfLine number of line of ticket to create.
	 * @return the Ticket entity
	 */
	@PostMapping("/ticket")
	public Ticket createTicket(@RequestParam(defaultValue = "3") int numOfLine) {
		log.debug("REST request to generate a Ticket with {} lines", numOfLine);
		if (numOfLine <= 0) {
			throw new BadRequestException("Invalid request");
		}
		Ticket ticket = ticketService.generateTicket(numOfLine);
		Ticket result = ticketService.save(ticket);
		return result;
	}

	/**
	 * {@code GET  /ticket/} : return all tickets.
	 *
	 * @return list of tickets
	 */
	@GetMapping(value = "/ticket")
	public List<Ticket> getAllTickets() {
		log.debug("REST request to get all tickets");
		return ticketService.findAll();
	}

	/**
	 * {@code GET  /ticket/:id} : get the "id" ticket.
	 *
	 * @param id the id of the ticket to retrieve.
	 * @return the ticket, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping(value = "/ticket/{id}")
	public Ticket getTicket(@PathVariable Long id) {
		log.debug("REST request to get Ticket : {}", id);
		Optional<Ticket> ticket = ticketService.findOne(id);
		if(ticket.isPresent())
			return ticket.get();
		else {
			log.debug("Ticket with id {} not found", id);
			throw new TicketNotFoundException("Ticket not found");
		}
	}

	/**
	 *
	 * @param id the id of the ticket to update
	 * @param numOfLine the new number of line to be added to the ticket
	 * @return a new ticket with new lines udpated
	 */
	@PutMapping(value = "/ticket/{id}")
	public Ticket updateTicket(@PathVariable Long id, @RequestParam int numOfLine) {
		log.debug("REST request to update Ticket with id : {}", id);
		Optional<Ticket> ticket = ticketService.findOne(id);
		if(ticket.isPresent()) {
			if (!ticket.get().isChecked()) {
				for(int i = 0; i<numOfLine; i++)
					ticket.get().getLines().add(ticketService.generateLine());
				Ticket newTicket = ticketService.save(ticket.get());
				return newTicket;
			} else{
				log.debug("Ticket with id {} was already checked", id);
				throw new TicketCheckedException("Ticket was already checked. It can not be amended");
			}
		}
		else {
			log.debug("Ticket with id {} not found", id);
			throw new TicketNotFoundException("Ticket not found");
		}
	}

	/**
	 * {@code PUT  /status/:id} : check the "id" ticket.
	 * @param id
	 * @return the checked ticket, or with status {@code 404 (Not Found)}.
	 */
	@PutMapping(value = "/status/{id}")
	public CheckedTicket updateTicketStatus(@PathVariable Long id) {
		log.debug("REST request to update Ticket with id : {}", id);
		Optional<Ticket> ticket = ticketService.findOne(id);
		if(ticket.isPresent()) {
			Ticket dbTicket = ticket.get();
			dbTicket.setChecked();
			dbTicket = ticketService.save(dbTicket);
			CheckedTicket checkedTicket = new CheckedTicket(dbTicket);
			return checkedTicket;
		}
		else {
			log.debug("Ticket with id {} not found", id);
			throw new TicketNotFoundException("Ticket not found");
		}
	}
}
