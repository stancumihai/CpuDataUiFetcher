module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.dragos.controller to javafx.fxml;
    exports org.dragos;
}