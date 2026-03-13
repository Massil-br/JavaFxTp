package com.massil;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class TicketPersistenceApp extends Application {
    private TicketPersistenceService service;
    private TextField titleField;
    private TextField customerField;
    private TextField searchField;
    private ComboBox<String> priorityBox;
    private ComboBox<String> statusBox;
    private DatePicker datePicker;
    private TextArea descriptionArea;
    private CheckBox urgentCheck;
    private Label statusLabel;
    private TableView<SupportTicket> table;

    @Override
    public void start(Stage stage) {
        DatabaseManager.initializeDatabase();
        service = new TicketPersistenceService();
        BorderPane root = buildRoot();
        Scene scene = new Scene(root, 1000, 600);
        var css = getClass().getResource("styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
        stage.setTitle("Ticket Persistence");
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane buildRoot() {
        buildControls();
        table = buildTable();
        table.setItems(service.getTickets());
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> fillFormFromSelection(newSel));
        HBox searchBox = new HBox(8, new Label("Recherche"), searchField);
        searchBox.setPadding(new Insets(8));
        HBox buttons = buildButtons();
        VBox left = new VBox(10, buildFormGrid(), buttons, statusLabel);
        left.setPadding(new Insets(10));
        BorderPane root = new BorderPane();
        root.setTop(searchBox);
        root.setLeft(left);
        root.setCenter(table);
        return root;
    }

    private void buildControls() {
        titleField = new TextField();
        customerField = new TextField();
        searchField = new TextField();
        priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("LOW", "MEDIUM", "HIGH");
        statusBox = new ComboBox<>();
        statusBox.getItems().addAll("OPEN", "IN_PROGRESS", "CLOSED");
        datePicker = new DatePicker(LocalDate.now());
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(4);
        urgentCheck = new CheckBox("Urgent");
        statusLabel = new Label();
        searchField.textProperty().addListener((obs, old, val) -> applySearch(val));
    }

    private GridPane buildFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.add(new Label("Title"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Customer"), 0, 1);
        grid.add(customerField, 1, 1);
        grid.add(new Label("Priority"), 0, 2);
        grid.add(priorityBox, 1, 2);
        grid.add(new Label("Status"), 0, 3);
        grid.add(statusBox, 1, 3);
        grid.add(new Label("Date"), 0, 4);
        grid.add(datePicker, 1, 4);
        grid.add(new Label("Description"), 0, 5);
        grid.add(descriptionArea, 1, 5);
        grid.add(urgentCheck, 1, 6);
        return grid;
    }

    private TableView<SupportTicket> buildTable() {
        TableView<SupportTicket> tv = new TableView<>();
        TableColumn<SupportTicket, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<SupportTicket, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<SupportTicket, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        TableColumn<SupportTicket, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        TableColumn<SupportTicket, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        TableColumn<SupportTicket, Boolean> urgentCol = new TableColumn<>("Urgent");
        urgentCol.setCellValueFactory(new PropertyValueFactory<>("urgent"));
        TableColumn<SupportTicket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        tv.getColumns().addAll(List.of(idCol, titleCol, customerCol, priorityCol, dateCol, urgentCol, statusCol));
        return tv;
    }

    private HBox buildButtons() {
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button resetButton = new Button("Reset");
        Button reloadButton = new Button("Reload");
        Button exportButton = new Button("Export CSV");
        addButton.setOnAction(e -> onAdd());
        updateButton.setOnAction(e -> onUpdate());
        deleteButton.setOnAction(e -> onDelete());
        resetButton.setOnAction(e -> onReset());
        reloadButton.setOnAction(e -> onReload());
        exportButton.setOnAction(e -> onExport());
        HBox box = new HBox(8, addButton, updateButton, deleteButton, resetButton, reloadButton, exportButton);
        box.setPadding(new Insets(10, 0, 0, 0));
        return box;
    }

    private void onAdd() {
        SupportTicket ticket = buildTicketFromForm(false);
        if (ticket == null) return;
        SupportTicket created = service.createTicket(ticket);
        if (created == null) {
            statusLabel.setText("Erreur à la création");
            return;
        }
        clearForm();
        table.getSelectionModel().clearSelection();
        statusLabel.setText("Ticket créé id=" + created.getId());
    }

    private void onUpdate() {
        SupportTicket selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Aucun ticket sélectionné");
            return;
        }
        SupportTicket ticket = buildTicketFromForm(true);
        if (ticket == null) return;
        service.updateTicket(ticket);
        applySearch(searchField.getText());
        statusLabel.setText("Ticket mis à jour");
    }

    private void onDelete() {
        SupportTicket selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Aucun ticket sélectionné");
            return;
        }
        service.deleteTicket(selected.getId());
        applySearch(searchField.getText());
        clearForm();
        statusLabel.setText("Ticket supprimé");
    }

    private void onReset() {
        clearForm();
        table.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    private void onReload() {
        service.refresh();
        applySearch(searchField.getText());
        statusLabel.setText("Données rechargées");
    }

    private void onExport() {
        TicketExporter.exportToCsv(service.getTickets(), "tickets.csv");
        statusLabel.setText("Export CSV effectué");
    }

    private void clearForm() {
        titleField.clear();
        customerField.clear();
        priorityBox.getSelectionModel().clearSelection();
        statusBox.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.now());
        descriptionArea.clear();
        urgentCheck.setSelected(false);
    }

    private void fillFormFromSelection(SupportTicket ticket) {
        if (ticket == null) return;
        titleField.setText(ticket.getTitle());
        customerField.setText(ticket.getCustomerName());
        priorityBox.setValue(ticket.getPriority());
        statusBox.setValue(ticket.getStatus());
        datePicker.setValue(ticket.getCreatedAt());
        descriptionArea.setText(ticket.getDescription());
        urgentCheck.setSelected(ticket.isUrgent());
    }

    private SupportTicket buildTicketFromForm(boolean withId) {
        String title = titleField.getText();
        String customer = customerField.getText();
        String priority = priorityBox.getValue();
        String status = statusBox.getValue();
        LocalDate date = datePicker.getValue();
        String description = descriptionArea.getText();
        boolean urgent = urgentCheck.isSelected();
        if (title == null || title.isBlank()) {
            statusLabel.setText("Titre obligatoire");
            return null;
        }
        if (customer == null || customer.isBlank()) {
            statusLabel.setText("Client obligatoire");
            return null;
        }
        if (priority == null || status == null || date == null) {
            statusLabel.setText("Champs requis manquants");
            return null;
        }
        if (!withId) return new SupportTicket(title, customer, priority, date, description, urgent, status);
        SupportTicket selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return null;
        return new SupportTicket(selected.getId(), title, customer, priority, date, description, urgent, status);
    }

    private void applySearch(String keyword) {
        service.getTickets().setAll(service.search(keyword));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
