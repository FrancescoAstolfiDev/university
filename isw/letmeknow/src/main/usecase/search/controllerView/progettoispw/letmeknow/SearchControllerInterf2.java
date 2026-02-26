package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import progettoispw.letmeknow.bean.HomepageBean;

import java.io.IOException;

public class SearchControllerInterf2 extends SearchControllerInterf1 {
    @FXML
    AnchorPane resultVisit;
    @FXML
    AnchorPane buttonBar;
    String [] uids;
    public SearchControllerInterf2(){
        super();
        uids=new String[6];
    }
    @Override
    public void initialize(){
        super.initialize();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("buttonBarInterf2.fxml"));
        try {
            buttonBar.getChildren().add((Node)loader.load());
            ButtonBarInterf2 barController=loader.getController();
            barController.setSearch();
            HomepageBean beanVisit =new HomepageBean(true);
            if(beanVisit.getUserID()!=null)visit();
        } catch (IOException e) {
            buttonBar.getChildren().removeAll(buttonBar.getChildren());
        }
    }
    @Override
    public void goResult(ActionEvent event){
        try {
            resultVisit.getChildren().removeAll(resultVisit.getChildren());
            resultVisit.getChildren().add((Node) FXMLLoader.load(getClass().getResource("ResultSearch/interf2.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void visit (){
        try {
            resultVisit.getChildren().removeAll(resultVisit.getChildren());
            resultVisit.getChildren().add((Node) FXMLLoader.load(getClass().getResource("homepageOthers/interf2.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
