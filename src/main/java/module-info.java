module ru.bvkuchin.networkchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.bvkuchin.networkchat to javafx.fxml;
    exports ru.bvkuchin.networkchat;
    exports ru.bvkuchin.networkchat.controllers;
    opens ru.bvkuchin.networkchat.controllers to javafx.fxml;
}