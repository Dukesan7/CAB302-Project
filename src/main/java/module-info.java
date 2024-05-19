module org.example.cab302project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.json;

    opens org.example.cab302project to javafx.fxml;
    exports org.example.cab302project;
}