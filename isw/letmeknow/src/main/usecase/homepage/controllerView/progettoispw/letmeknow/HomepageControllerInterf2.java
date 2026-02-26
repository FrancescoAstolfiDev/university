package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomepageControllerInterf2 extends HomepagecontrollerInterf1{
    @FXML
    AnchorPane homepageEdit;
    @FXML
    protected void goToEditProfile(ActionEvent event){
        try {
            homepageEdit.getChildren().removeAll(homepageEdit.getChildren());
            homepageEdit.getChildren().add((Node) FXMLLoader.load(getClass().getResource("homepageEdit/homepageEdit.fxml")));
        } catch (IOException e) {
            homepageEdit.getChildren().removeAll(homepageEdit.getChildren());
        }
    }
}
