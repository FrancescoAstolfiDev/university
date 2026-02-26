package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import progettoispw.letmeknow.bean.BeanResultSearch;
import java.io.IOException;
public class ResultSearchControllerInterf2{
    @FXML
    private Group group1;
    @FXML
    private Group group2;
    @FXML
    private Group group3;
    @FXML
    private Group group4;
    @FXML
    Group group5;
    @FXML
    Group group6;
    @FXML
    AnchorPane resultVisit;
    String [] uids;
    BeanResultSearch beanResultSearch;
    public ResultSearchControllerInterf2(){
        uids=new String[6];
        beanResultSearch=new BeanResultSearch(6);
    }
    public ResultSearchControllerInterf2(String [] input){
        uids=input;
    }
    public void initialize(){
        outputVal();
    }
    public void visit(ActionEvent event) {
        ResultSearchControllerInterf1 rscInterf1=new ResultSearchControllerInterf1(uids);
        rscInterf1.touchVisit(event);
        if(resultVisit !=null)switchPane();
    }
    public void switchPane(){
        try {
            resultVisit.getChildren().removeAll(resultVisit.getChildren());
            resultVisit.getChildren().add((Node) FXMLLoader.load(getClass().getResource("homepageOthers/interf2.fxml")));
        } catch (IOException e) {
            resultVisit.getChildren().removeAll(resultVisit.getChildren());
        }

    }
    public void touchChat(ActionEvent actionEvent) {
        InitialSearchAndChatControllerInterf1 iscController=new InitialSearchAndChatControllerInterf1(uids);
        iscController.touchChat(actionEvent);
    }
    public void outputVal() {
        ResultSearchControllerInterf1 rscInterf1=new ResultSearchControllerInterf1();
        uids=rscInterf1.prevOutputVal(new Group[]{group1,group2,group3,group4,group5,group6},6,beanResultSearch);
    }
}
