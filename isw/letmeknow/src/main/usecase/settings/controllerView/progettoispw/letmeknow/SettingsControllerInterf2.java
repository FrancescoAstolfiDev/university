package progettoispw.letmeknow;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsControllerInterf2 extends SettingsControllerInterf1{
    @FXML
    AnchorPane homepageEdit;
    @FXML
    AnchorPane buttonBar;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        comb.setItems(FXCollections.observableArrayList("Italiano", "Inglese"));
        load();
    }
    public void load () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("buttonBarInterf2.fxml"));
            buttonBar.getChildren().add((Node)loader.load());
            ButtonBarInterf2 barController=loader.getController();
            barController.setSettings();
            homepageEdit.getChildren().removeAll(homepageEdit.getChildren());
            homepageEdit.getChildren().add((Node) FXMLLoader.load(getClass().getResource("homepageEdit/homepageEdit.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
