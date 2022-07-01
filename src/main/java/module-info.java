module ru.bvkuchin.networkchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.bvkuchin.networkchat to javafx.fxml;
    exports ru.bvkuchin.networkchat;
}