package progettoispw.letmeknow;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import javafx.scene.text.Text;
import javafx.util.Duration;
import progettoispw.letmeknow.bean.BeanResultSearch;


import javafx.animation.Timeline;
import progettoispw.letmeknow.bean.ChatBean;
import progettoispw.letmeknow.bean.MessageBean;
import progettoispw.letmeknow.bean.MessageTextModel;
import progettoispw.letmeknow.controller.ChatController;


public class ChatControllerInterf1 {
    @FXML
    private AnchorPane messaggi;
    @FXML
    private TextArea inputmsg;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    protected Text withName;
    protected  boolean initializated;
    protected  PageMenu pageSwitch;
    protected ChatBean bean;
    protected  String [] message;
    protected Decorator graphic;
    protected Timeline timeline;
    public ChatControllerInterf1() {
        bean=new ChatBean();
        graphic=new Decorator(true);
        initializated=false;
        pageSwitch =new PageMenu();
        timeline=new Timeline(new KeyFrame(Duration.millis(5000),this::action));
        timeline.setCycleCount(Animation.INDEFINITE);//never stop
    }
    @FXML
    protected void sendMsg() {
        MessageTextModel beanMsg=new MessageTextModel();
        MessageBean messageBean=new MessageBean();
        messageBean.setText(inputmsg.getText());
        messageBean.setReciver(bean.getWith());
        ChatController controller=new ChatController();
        controller.newMSG(messageBean);
        inputmsg.setText(beanMsg.getTextMsg());
        recivemsgArr();
    }
    private  void action(ActionEvent event ){
        if(initializated) {
            recivemsgArr();
        }
    }
    public void  recivemsgArr() {
        message = bean.getMSG();
        Label textmsg;
        for (int i = 0; i < message.length; i += 2) {
            graphic.setText(message[i]);
            if (message[i + 1].equals("i am the sender")) {
                textmsg = graphic.getMessageSended();
            } else {
                textmsg = graphic.getMessageRecived();
            }
            messaggi.getChildren().add(textmsg);
            messaggi.setPrefHeight(graphic.getAumenta());
            scrollpane.setVvalue(1.0);
        }
        initializated=true;
    }
    public void  initialize(){
        withName.setText("User #"+bean.getWith());
        recivemsgArr();
        timeline.play();
    }
    @FXML
    protected void goToHome(ActionEvent event){
        timeline.stop();
        pageSwitch.switchToHome(event);
    }
    @FXML
    protected void goBack(ActionEvent event) {
        timeline.stop();
        pageSwitch.switchToISC(event);
    }
    @FXML
    protected void goToPersonalForm(ActionEvent event)  {
        timeline.stop();
        pageSwitch.switchToPersonalForm(event);
    }
    @FXML
    protected void touchedHome(ActionEvent event){
        ResultSearchControllerInterf1 rscView=new ResultSearchControllerInterf1();
        rscView.setTouchedHome(bean.getWith());
        timeline.stop();
        pageSwitch.switchTo("homepageOthers/interf1.fxml",event,"Visit");
    }
}
