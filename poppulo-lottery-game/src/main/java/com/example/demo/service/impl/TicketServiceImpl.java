package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.demo.domain.Line;
import com.example.demo.domain.Ticket;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class TicketServiceImpl implements com.example.demo.service.TicketService {

	private final TicketRepository ticketRepository;

	@Autowired
	public TicketServiceImpl(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@Override
    public Ticket save(Ticket ticket) {
		ticket = ticketRepository.save(ticket);
		return ticket;
	}

	@Override
	public Ticket update(Ticket ticket) {
		ticket = ticketRepository.save(ticket);
		return ticket;
	}


	@Override
    public Optional<Ticket> findOne(Long id) {
		return ticketRepository.findById(id);
	}

	@Override
	public Ticket generateTicket(int numOfLine) {
		List<Line> lines = new ArrayList<Line>();
		for (int i=0;i<numOfLine;i++){
			lines.add(generateLine());
		}
		Ticket newTicket = new Ticket(lines);
		return newTicket;
	}

	@Override
	public List<Ticket> findAll() {
		return ticketRepository.findAll();
	}

	@Override
	public Line generateLine(){
		Line line = new Line();
		Random rd = new Random();
		int[] arr = new int[3];
		for (int i=0;i<arr.length;i++){
			arr[i] = rd.nextInt(3);
		}
		line.setNumbers(arr);
		return line;
	}

}
