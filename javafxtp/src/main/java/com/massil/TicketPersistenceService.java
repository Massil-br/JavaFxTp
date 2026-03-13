package com.massil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class TicketPersistenceService {
    private final TicketDAO dao = new SQLiteTicketDao();
    private final ObservableList<SupportTicket> tickets = FXCollections.observableArrayList();

    public TicketPersistenceService() {
        refresh();
    }

    public ObservableList<SupportTicket> getTickets() {
        return tickets;
    }

    public void refresh() {
        tickets.clear();
        List<SupportTicket> all = dao.findAll();
        if (all == null || all.isEmpty()) return;
        tickets.addAll(all);
    }

    public SupportTicket createTicket(SupportTicket ticket) {
        if (ticket == null) return null;
        SupportTicket created = dao.insert(ticket);
        if (created == null) return null;
        tickets.add(created);
        return created;
    }

    public void updateTicket(SupportTicket ticket) {
        if (ticket == null) return;
        dao.update(ticket);
        refresh();
    }

    public void deleteTicket(long id) {
        dao.deleteById(id);
        refresh();
    }

    public void deleteAllTickets() {
        dao.deleteAll();
        tickets.clear();
    }

    public List<SupportTicket> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return dao.findAll();
        return dao.searchByTitleOrCustomer(keyword);
    }
}
