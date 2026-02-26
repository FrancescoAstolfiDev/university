package progettoispw.letmeknow;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import progettoispw.letmeknow.bean.FormToTakeStatusBean;

import java.io.IOException;

public class TakeFormControllerInterf2 extends TakeFormControllerInterf1{
    @FXML
    private AnchorPane status;
    @FXML
    ScrollPane scrollAnswers;
    FormToTakeStatusBean bean;
    public TakeFormControllerInterf2(){
        bean=new FormToTakeStatusBean();
    }
    public void initialize(){
        setValues();
        setSection();
    }
    public void save(ActionEvent event){
        save();
    }
    public void setSection(){
        try {
            status.getChildren().removeAll(status.getChildren());
            if(bean.getValComplete()==6){
            status.getChildren().add((Node) FXMLLoader.load(getClass().getResource("formCollectionResults/resultFormSectionInterf2.fxml")));
            }
            else{
                Button save;
                status.getChildren().add((Node) FXMLLoader.load(getClass().getResource("formCollectionResults/takeFormSectionInterf2.fxml")));
                ObservableList<Node> externList= status.getChildren();
                AnchorPane pane=(AnchorPane) externList.get(0);
                externList= pane.getChildren();
                save=(Button)externList.get(3);
                save.setOnAction(this::save);
            }
        }
        catch (IOException e) {
                status.getChildren().removeAll(status.getChildren());
            }
        }
}


