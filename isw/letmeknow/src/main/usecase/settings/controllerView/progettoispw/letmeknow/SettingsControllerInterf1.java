package progettoispw.letmeknow;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import progettoispw.letmeknow.bean.EmailCheck;
import progettoispw.letmeknow.bean.SettingsBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.bean.TwoStringsBean;
import progettoispw.letmeknow.controller.SettingsController;


import java.net.URL;
import java.util.ResourceBundle;

public class SettingsControllerInterf1 implements Initializable {
    private PageMenu pageSwitch;
    private SettingsBean bean;
    private SettingsController controller;
    private static final String DEFAULTBORDER="-fx-border-color: rgba(55, 125, 255,0.7)";
    private static final String REDBORDER="-fx-border-color:red";
    @FXML
    PasswordField pswd;
    @FXML
    PasswordField confirmpswd;
    @FXML
    TextField email;
    @FXML
    TextArea feedback;
    @FXML
    protected ComboBox<String> comb;
    private boolean bool;
    public SettingsControllerInterf1(){
        bean=new SettingsBean();
        controller=new SettingsController();
        pageSwitch =new PageMenu();
        bool=false;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        comb.setItems(FXCollections.observableArrayList("Italiano", "Inglese"));
    }
    @FXML
    protected void goBack() {
        pageSwitch.backTo();
    }
    @FXML
    protected void goEditProf(ActionEvent event){
        pageSwitch.switchTo("homepageEdit/interf1.fxml", event,"Edit");
    }
    @FXML
    protected void goToHome(ActionEvent event)  {
        pageSwitch.switchToHome(event);
    }
    @FXML
    protected void goToChat(ActionEvent event) {
        pageSwitch.switchToISC(event);
    }
    @FXML
    protected void setPswd()  {
        pswd.setStyle(DEFAULTBORDER);
        confirmpswd.setStyle(DEFAULTBORDER);
        TwoStringsBean stringBean=new TwoStringsBean();
        bool=stringBean.setStrings(pswd.getText(),confirmpswd.getText());
        if(bool){
            controller.setPassword(stringBean);
            bool= !bean.getInfo();
        }
        if(!bool){
            pswd.setStyle(REDBORDER);
            confirmpswd.setStyle(REDBORDER);
        }else{
            pswd.setText("");
            confirmpswd.setText("");
        }
    }
    @FXML
    protected void signOut(ActionEvent event){
        controller.closeConnection();
        pageSwitch.switchTo("login/interf1.fxml",event,"login");
    }
    @FXML
    protected void setMail (){
        email.setStyle(DEFAULTBORDER);
        EmailCheck emailBean=new EmailCheck();
        bool=emailBean.setEmail(email.getText());
        if(bool){
            controller.setEmail(emailBean);
            bool=bean.getInfo();
        }
        if(bool){
            email.setStyle(REDBORDER);
        }else{
            email.setText("");
        }
    }
    @FXML
    protected void setFeed(){
        feedback.setStyle("-fx-border-color: white");
        StringBean stringBean=new StringBean();
        stringBean.setPass(feedback.getText());
        controller.feed(stringBean);
        bool= bean.getInfo();
        if(bool){
            feedback.setStyle(REDBORDER);
        }else{
            feedback.setText("");
        }
    }
    @FXML
    protected void goToPersonalForm(ActionEvent event) {
        pageSwitch.switchToPersonalForm(event);
    }
}

