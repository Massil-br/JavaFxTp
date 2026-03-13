package com.massil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteTicketDao implements TicketDAO {
    private SupportTicket mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        String customerName = rs.getString("customer_name");
        String priority = rs.getString("priority");
        LocalDate createdAt = LocalDate.parse(rs.getString("created_at"));
        String description = rs.getString("description");
        boolean urgent = rs.getInt("urgent") != 0;
        String status = rs.getString("status");
        return new SupportTicket(id, title, customerName, priority, createdAt, description, urgent, status);
    }

    @Override
    public SupportTicket insert(SupportTicket ticket) {
        String sql = "INSERT INTO support_tickets (title, customer_name, priority, created_at, description, urgent, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ticket.getTitle());
            ps.setString(2, ticket.getCustomerName());
            ps.setString(3, ticket.getPriority());
            ps.setString(4, ticket.getCreatedAt().toString());
            ps.setString(5, ticket.getDescription());
            ps.setInt(6, ticket.isUrgent() ? 1 : 0);
            ps.setString(7, ticket.getStatus());
            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) return null;
                long id = keys.getLong(1);
                return ticket.withId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SupportTicket> findAll() {
        String sql = "SELECT * FROM support_tickets ORDER BY id DESC";
        List<SupportTicket> result = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(mapRow(rs));
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SupportTicket> findById(long id) {
        String sql = "SELECT * FROM support_tickets WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SupportTicket> searchByTitleOrCustomer(String keyword) {
        String sql = "SELECT * FROM support_tickets WHERE title LIKE ? OR customer_name LIKE ? ORDER BY id DESC";
        List<SupportTicket> result = new ArrayList<>();
        String pattern = "%" + keyword + "%";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(SupportTicket ticket) {
        String sql = "UPDATE support_tickets SET title = ?, customer_name = ?, priority = ?, created_at = ?, description = ?, urgent = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticket.getTitle());
            ps.setString(2, ticket.getCustomerName());
            ps.setString(3, ticket.getPriority());
            ps.setString(4, ticket.getCreatedAt().toString());
            ps.setString(5, ticket.getDescription());
            ps.setInt(6, ticket.isUrgent() ? 1 : 0);
            ps.setString(7, ticket.getStatus());
            ps.setLong(8, ticket.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM support_tickets WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM support_tickets";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
