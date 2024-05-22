module org.example.cab302project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.json;

    opens org.example.cab302project to javafx.fxml;
    exports org.example.cab302project;
    exports org.example.cab302project.Controller;
    opens org.example.cab302project.Controller to javafx.fxml;
    exports org.example.cab302project.focusSess;
    opens org.example.cab302project.focusSess to javafx.fxml;
    exports org.example.cab302project.Tasks;
    opens org.example.cab302project.Tasks to javafx.fxml;
}