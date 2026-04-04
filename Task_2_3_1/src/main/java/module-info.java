module ru.nsu.dizmestev {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.nsu.dizmestev to javafx.fxml;
    exports ru.nsu.dizmestev;
}