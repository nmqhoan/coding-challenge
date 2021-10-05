package com.example.demo.web.rest.response;

import com.example.demo.domain.Line;
import com.example.demo.domain.Ticket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CheckedTicket {
    private final Long ticketId;
    private List<CheckedLine> checkedLines;

    public CheckedTicket(Ticket ticket){
        checkedLines = new ArrayList<CheckedLine>();
        ticketId = ticket.getId();
        for(Line line:ticket.getLines()){
            checkedLines.add(new CheckedLine(line));
        }
    }

    public Long getTicketId() {
        return ticketId;
    }

    public List<CheckedLine> getCheckedLines() {
        // sort lines by result
        checkedLines.sort(new Comparator<CheckedLine>() {
            @Override
            public int compare(CheckedLine o1, CheckedLine o2) {
                return o1.getResult() - o2.getResult();
            }
        });
        return checkedLines;
    }

    public void setCheckedLines(List<CheckedLine> checkedLines) {
        this.checkedLines = checkedLines;
    }

    @Override
    public String toString() {
        return "CheckedTicket{" +
                "ticketId=" + ticketId +
                ", checkedLines=" + checkedLines +
                '}';
    }
}
