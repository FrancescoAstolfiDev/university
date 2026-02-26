package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import progettoispw.letmeknow.bean.HomepagePsychologistBean;

import java.io.IOException;

public class HomepagePsychologistInterf2 extends HomepagePsychologistInterf1{
    @FXML
    AnchorPane anchorSelected;

    @Override
    @FXML
    public void select(ActionEvent event){
        try{
            int val=getSelected(event);
            anchorSelected.getChildren().removeAll(anchorSelected.getChildren());
            anchorSelected.getChildren().add((Node) FXMLLoader.load(getClass().getResource("homepagePsychologist/formSelected"+val+".fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    @FXML
    public void goToSettings(ActionEvent event ){
        try {
            anchorSelected.getChildren().removeAll(anchorSelected.getChildren());
            anchorSelected.getChildren().add((Node) FXMLLoader.load(getClass().getResource("settingsPsychologist/interf2.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    @FXML
    public void increm() {
        anchorSelected.getChildren().removeAll(anchorSelected.getChildren());
        controller.increm();
        initialize();
    }
    @Override
    @FXML
    public void decrem() {
        anchorSelected.getChildren().removeAll(anchorSelected.getChildren());
        controller.decrem();
        initialize();
    }
}
