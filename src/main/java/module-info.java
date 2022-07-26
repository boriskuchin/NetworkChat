module ru.bvkuchin.networkchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.apache.commons.io;


    opens ru.bvkuchin.networkchat to javafx.fxml;
    exports ru.bvkuchin.networkchat;
    exports ru.bvkuchin.networkchat.controllers;
    opens ru.bvkuchin.networkchat.controllers to javafx.fxml;
}