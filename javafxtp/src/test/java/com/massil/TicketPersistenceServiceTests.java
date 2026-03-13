package com.massil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TicketPersistenceServiceTests {
    private TicketPersistenceService service;

    @BeforeEach
    void setup() {
        DatabaseManager.initializeDatabase();
        service = new TicketPersistenceService();
        service.deleteAllTickets();
    }

    @Test
    void creationRechercheEtSuppression() {
        assertTrue(service.getTickets().isEmpty());
        SupportTicket t1 = new SupportTicket("Test 1", "Client A", "HIGH", LocalDate.now(), "Desc 1", true, "OPEN");
        SupportTicket t2 = new SupportTicket("Autre", "Client B", "LOW", LocalDate.now(), "Desc 2", false, "OPEN");
        SupportTicket c1 = service.createTicket(t1);
        SupportTicket c2 = service.createTicket(t2);
        assertNotNull(c1);
        assertNotNull(c2);
        assertEquals(2, service.getTickets().size());
        assertFalse(service.search("Test").isEmpty());
        service.deleteAllTickets();
        assertTrue(service.getTickets().isEmpty());
    }
}

