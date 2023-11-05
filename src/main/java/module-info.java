module com.example.blackjack {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires javafx.web;
    requires batik.transcoder;


    opens com.example.blackjack to javafx.fxml;
    exports com.example.blackjack;
    exports com.example.blackjack.Controllers;
}