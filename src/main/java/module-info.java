module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.stancu.controller to javafx.fxml;
    exports org.stancu;
}