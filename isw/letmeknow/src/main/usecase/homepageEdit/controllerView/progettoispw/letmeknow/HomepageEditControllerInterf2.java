package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomepageEditControllerInterf2 extends HomepageEditControllerInterf1 {
    @FXML
    AnchorPane homepageEdit;
    @Override
    @FXML
    protected void goToHome(ActionEvent event) {
        try {
            homepageEdit.getChildren().removeAll(homepageEdit.getChildren());
            homepageEdit.getChildren().add((Node) FXMLLoader.load(getClass().getResource("homepage/homepage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    @FXML
    protected void takeForm(ActionEvent event){
        pageSwitch.switchToPersonalForm(event);
    }
}
