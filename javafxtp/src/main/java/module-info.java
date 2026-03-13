module com.massil {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;

    opens com.massil to javafx.fxml;
    exports com.massil;
}
