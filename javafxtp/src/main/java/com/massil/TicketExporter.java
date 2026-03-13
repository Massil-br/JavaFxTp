package com.massil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public final class TicketExporter {
    private TicketExporter() {}

    public static void exportToCsv(Collection<SupportTicket> tickets, String filePath) {
        if (tickets == null || tickets.isEmpty()) return;
        if (filePath == null || filePath.isBlank()) return;
        Path path = Path.of(filePath);
        try {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) Files.createDirectories(parent);
            try (var w = Files.newBufferedWriter(path)) {
                w.write("id;title;customer;priority;created_at;description;urgent;status");
                w.newLine();
                for (SupportTicket t : tickets) {
                    String line =
                            t.getId() + ";" +
                            escape(t.getTitle()) + ";" +
                            escape(t.getCustomerName()) + ";" +
                            escape(t.getPriority()) + ";" +
                            t.getCreatedAt() + ";" +
                            escape(t.getDescription()) + ";" +
                            (t.isUrgent() ? "1" : "0") + ";" +
                            escape(t.getStatus());
                    w.write(line);
                    w.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String escape(String v) {
        if (v == null) return "";
        String s = v.replace("\"", "\"\"");
        if (s.contains(";") || s.contains("\"") || s.contains("\n") || s.contains("\r")) return "\"" + s + "\"";
        return s;
    }
}

