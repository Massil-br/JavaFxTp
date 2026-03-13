package com.massil;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class TicketEditorController {

    @FXML private TextField titleField;
    @FXML private TextField customerField;
    @FXML private ComboBox<String> priorityBox;
    @FXML private DatePicker datePicker;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox urgentCheck;
    @FXML private ComboBox<String> statusBox;
    @FXML private Label statusLabel;

    private final TicketPersistenceService service = new TicketPersistenceService();

    @FXML
    public void initialize() {
        DatabaseManager.initializeDatabase();
        priorityBox.getItems().addAll("Faible", "Moyenne", "Haute", "Critique");
        statusBox.getItems().addAll("Nouveau", "En cours", "Résolu");
        datePicker.setValue(LocalDate.now());
        statusBox.setValue("Nouveau");
    }

    @FXML
    public void handleSave() {
        try {
            SupportTicket ticket = new SupportTicket(
                    titleField.getText().trim(),
                    customerField.getText().trim(),
                    priorityBox.getValue(),
                    datePicker.getValue(),
                    descriptionArea.getText().trim(),
                    urgentCheck.isSelected(),
                    statusBox.getValue()
            );
            service.createTicket(ticket);
            statusLabel.setText("Ticket enregistré.");
            handleReset();
        } catch (Exception e) {
            statusLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    public void handleReset() {
        titleField.clear();
        customerField.clear();
        priorityBox.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.now());
        descriptionArea.clear();
        urgentCheck.setSelected(false);
        statusBox.setValue("Nouveau");
    }
}