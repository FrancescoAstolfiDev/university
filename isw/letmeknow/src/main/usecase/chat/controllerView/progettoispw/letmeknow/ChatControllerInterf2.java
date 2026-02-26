package progettoispw.letmeknow;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import progettoispw.letmeknow.bean.ISCBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.ISCController;

import java.io.IOException;

public class ChatControllerInterf2 extends ChatControllerInterf1{
    @FXML
    AnchorPane listUtenti;
    @FXML
    TextField searchBar;
    @FXML
    AnchorPane anchorChat;
    @FXML
    AnchorPane buttonBar;
    ISCBean iscBean;
    String userid;
    ISCController controller;
    public ChatControllerInterf2(){
        super();
        iscBean=new ISCBean();
        graphic=new Decorator(false);
        userid=bean.getUid();
        timeline=new Timeline(new KeyFrame(Duration.millis(5000),this::recivemsgArr));
        timeline.setCycleCount(Animation.INDEFINITE);//never stop
        controller= iscBean.getController();
    }
    private void recivemsgArr(ActionEvent event){
        if(initializated) {
            addUser();
            recivemsgArr();
        }
    }
    @FXML@Override
    protected void sendMsg() {
        super.sendMsg();
        addUser();
    }
    @FXML@Override
    public void  initialize(){
        addUser();
        try {
            if(bean.getWith()==null){
                anchorChat.getChildren().removeAll(anchorChat.getChildren());
                anchorChat.getChildren().add((Node) FXMLLoader.load(getClass().getResource("chat/chatVoid.fxml")));
                return ;
            }
        } catch (IOException e) {
            buttonBar.getChildren().removeAll(buttonBar.getChildren());
            anchorChat.getChildren().removeAll(anchorChat.getChildren());
        }
        super.initialize();
    }
    public String checkUsrId(String input){
        if(input.length()<=8){
            return input;
        }
        int indice;
        indice=input.indexOf("||");
        String sub=input.substring(0,indice);
        if(!sub.equals(userid)) {
            return sub;
        }
        return input.substring(indice+2);
    }
    private void goChat(ActionEvent event){
        Button button=(Button)event.getTarget();
        String who=button.getText();
        who=checkUsrId(who);
        StringBean stringBean=new StringBean();
        stringBean.setPass(who);
        controller.setTouched(stringBean);
        pageSwitch.switchToChat(event);
    }
    public  void addUser(){
        listUtenti.getChildren().removeAll(listUtenti.getChildren());
        graphic.setHList();
        Button userButton;
        Label msgLabel;
        String [][] innerusers =iscBean.exitUid();
        String [] uid=innerusers[0];
        String [] msg=innerusers[1];
        for(int i=0;i< uid.length;i++){
            userButton=graphic.getButton(uid[i]);
            msgLabel=graphic.getLabel(msg[i]);
            userButton.setOnAction(this::goChat);
            listUtenti.getChildren().addAll(userButton,msgLabel);
            listUtenti.setPrefHeight(graphic.getHlist());
        }
        searchBar.textProperty().addListener((observableValue, s, t1) -> {
            if(searchBar.getText().equals("")){
                controller.reset();
                timeline.play();
            }
        });
    }
    @FXML
    public void searchMessage(){
        StringBean stringBean=new StringBean();
        stringBean.setPass(searchBar.getText());
        controller.searchMessage(stringBean);
        timeline.stop();
        addUser();
    }
    public void goToSettings(ActionEvent actionEvent) {
        timeline.stop();
        pageSwitch.switchToSettings(actionEvent);
    }
    public void goToSearch(ActionEvent actionEvent) {
        timeline.stop();
        pageSwitch.switchToSearch(actionEvent);
    }
    public void goToChat(ActionEvent actionEvent) {
        timeline.stop();
        pageSwitch.switchToChat(actionEvent);
    }
    @FXML@Override
    protected void touchedHome(ActionEvent event){
        ResultSearchControllerInterf1 resultSearchControllerInterf1=new ResultSearchControllerInterf1();
        resultSearchControllerInterf1.setTouchedHome(bean.getWith());
        timeline.stop();
        goToSearch(event);
    }
}
