package progettoispw.letmeknow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class HomepageHomepageEditInterf2 {
    @FXML
    AnchorPane homepageEditt;
    @FXML
    AnchorPane noticeSettings;
    @FXML
    AnchorPane buttonBar;

    public void initialize(){
        load();
    }
    public void load () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("buttonBarInterf2.fxml"));
            buttonBar.getChildren().add((Node)loader.load());
            ButtonBarInterf2 barController=loader.getController();
            barController.setHome();
            homepageEditt.getChildren().removeAll(homepageEditt.getChildren());
            homepageEditt.getChildren().add((Node) FXMLLoader.load(getClass().getResource("homepage/homepage.fxml")));
            noticeSettings.getChildren().removeAll(noticeSettings.getChildren());
            noticeSettings.getChildren().add((Node) FXMLLoader.load(getClass().getResource("homepage/noticeBoard.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
