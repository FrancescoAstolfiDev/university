module progettoispw.letmeknow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires java.mail;
    opens progettoispw.letmeknow to javafx.fxml;
    exports progettoispw.letmeknow;
    opens progettoispw.letmeknow.photo to javafx.fxml;
    exports progettoispw.letmeknow.bean;
    opens progettoispw.letmeknow.bean to javafx.fxml;
    exports progettoispw.letmeknow.controller;
    opens progettoispw.letmeknow.controller to javafx.fxml;
    //exports progettoipsw.letmeknow;
    //opens progettoipsw.letmeknow to javafx.fxml;
}