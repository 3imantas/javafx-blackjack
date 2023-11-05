module com.example.blackjack {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires javafx.web;


    opens com.example.blackjack to javafx.fxml;
    exports com.example.blackjack;
    exports com.example.blackjack.Controllers;
}