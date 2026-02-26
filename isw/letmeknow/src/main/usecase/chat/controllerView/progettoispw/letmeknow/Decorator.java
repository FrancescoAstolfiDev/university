package progettoispw.letmeknow;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import progettoispw.letmeknow.WordCheck;

public class Decorator {
    private  String text;
    private Label label;
    private WordCheck check;
    private Integer hlist;//high of the vertical layout starts
    private Label lastmessage;
    private boolean bool;
    private double hmsg;
    private double ystart;
    private double hspacing;
    private double aumenta;
    public Decorator(boolean inputbool){
        if(inputbool){
            check=new WordCheck(10,30);
        }else{
            check=new WordCheck(10,56);
        }
        hmsg=0;
        hspacing=30;
        lastmessage=null;
        ystart=0;
        bool=inputbool;
        hlist=0;
    }
    public void setText(String input){
        text=input;
    }
    public double getAumenta() {
        return aumenta;
    }
    public void setHList(){
        hlist=0;
    }
    public void general(){
        label =new Label(check.check(text));// prende il testo con gli invio
        label.setFont(Font.font("Gill Sans MT", FontWeight.BOLD,18));
        label.setPrefWidth(350);
        if(!bool)label.setPrefWidth(600);
        label.setMinHeight(60);
        if(lastmessage == null){
            ystart =hspacing;
        }else{
            ystart=lastmessage.getTranslateY()+hmsg+hspacing;
        }
        label.setTranslateY(ystart);
        hmsg= check.highText(label.getText());
        aumenta=ystart+hmsg+hspacing;
    }
    public Label getMessageRecived(){
        general();
        label.setTextFill(Color.rgb(43,43,43,0.7));
        label.setTranslateX(-5);
        label.setStyle("-fx-padding: 0 0 0 30");
        label.setBackground(new Background(new BackgroundFill(Color.rgb(214,214,214),new CornerRadii(18.0),new Insets(-5.0))));
        lastmessage=label;
        return label;
    }
    public Label getMessageSended(){
        general();
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-padding: 0 60 0 0");
        label.setTranslateX(50);
        label.setTranslateX(100);
        if(!bool)label.setTranslateX(300);
        label.setBackground(new Background(new BackgroundFill(Color.rgb(55, 125, 255, 0.69),new CornerRadii(18.0),new Insets(-5.0))));
        lastmessage=label;
        return label;
    }
    public Button getButton(String usrid) {
        Button uid=new Button(usrid);
        uid.setPrefWidth(125);
        uid.setPrefHeight(75);
        uid.setTranslateY(hlist);
        uid.setStyle("-fx-background-color: rgb(55, 125, 255, 0.69);-fx-text-fill: white; ");
        return uid;
    }
    public Label getLabel(String msg){
        Label lmsg;
        lmsg=new Label(msg);
        lmsg.setTranslateX(125);
        lmsg.setPrefWidth(305);
        lmsg.setPrefHeight(75);
        lmsg.setTranslateY(hlist);
        lmsg.setStyle("-fx-opacity:  0.7; ");
        lmsg.setStyle(
                "-fx-border-width: 1px;"+
                        "-fx-font-family: \"Work Sans\"; "+
                        "-fx-border-color: rgba(177, 177, 187,1);"+
                        "-fx-font-size:16px;"+
                        "-fx-text-fill:rgb(109,109,109);"+
                        "-fx-font-style:normal;"+
                        "-fx-font-weight: 200;"+
                        "-fx-font-size:18px;");
        hlist+=100;
        return lmsg;
    }
    public Integer getHlist() {
        return hlist;
    }
}
