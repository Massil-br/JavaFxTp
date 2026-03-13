package com.massil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketDaoTests {
    @BeforeEach
    void setup() {
        DatabaseManager.initializeDatabase();
        SQLiteTicketDao dao = new SQLiteTicketDao();
        dao.deleteAll();
    }

    @Test
    void crudCompletsurTicket() {
        SQLiteTicketDao dao = new SQLiteTicketDao();
        SupportTicket created = dao.insert(new SupportTicket("Test", "Client", "HIGH", LocalDate.now(), "Description", true, "OPEN"));
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        List<SupportTicket> all = dao.findAll();
        assertFalse(all.isEmpty());
        SupportTicket updated = new SupportTicket(created.getId(), "Test modifié", "Client 2", "LOW", created.getCreatedAt(), "Desc modifiée", false, "CLOSED");
        dao.update(updated);
        SupportTicket reloaded = dao.findById(created.getId()).orElse(null);
        assertNotNull(reloaded);
        assertEquals("Test modifié", reloaded.getTitle());
        dao.deleteById(created.getId());
        assertTrue(dao.findAll().isEmpty());
    }
}

