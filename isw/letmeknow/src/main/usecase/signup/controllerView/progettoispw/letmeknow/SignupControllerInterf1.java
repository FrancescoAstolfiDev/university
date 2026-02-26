package progettoispw.letmeknow;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import progettoispw.letmeknow.bean.*;
import progettoispw.letmeknow.controller.SignupController;


public class SignupControllerInterf1 {
    @FXML
    TextField email;
    @FXML
    TextField description;
    @FXML
    TextField goal;
    @FXML
    PasswordField pswd;
    @FXML
    PasswordField confirmpswd;
    @FXML
    Slider sl1;
    @FXML
    Slider sl2;
    @FXML
    Slider sl3;
    Slider [] sliders;
    @FXML
    Label lab1;
    @FXML
    Label lab2;
    @FXML
    Label lab3;
    @FXML
    Label emailCheck;
    @FXML
    Label pswdCheck;
    @FXML
    Label desCheck;
    @FXML
    Label slCheck;
    @FXML
    Label goalCheck;
    InitialUserBean bean;
    public SignupControllerInterf1(){
        bean=new InitialUserBean();
    }
    public SignupControllerInterf1(TextField  email,PasswordField [] passwords,Label []labels){
        bean=new InitialUserBean();
        this.email=email;
        this.pswd=passwords[0];
        this.confirmpswd=passwords[1];
        this.emailCheck=labels[0];
        this.pswdCheck=labels[1];

    }
    public void initialize() {
        Label [] labels = new Label[]{lab1, lab2, lab3};
        sliders=new Slider[]{sl1,sl2,sl3};
        reset();
        for (int i=0;i<3;i++) {
            int finalI = i;
            sliders[i].valueProperty().addListener((observableValue, number, t1) -> {
                labels[finalI].setText("" + (int) sliders[finalI].getValue());
                labels[finalI].setOpacity(1);
            });
        }
    }
    @FXML
    protected void goToLogin() {
        Page pageSwitch=new Page();
        pageSwitch.backTo();
    }
    private boolean check(boolean bool,Label lab){
        if(bool)lab.setOpacity(1);
        return bool;
    }
    public  void reset(){
        emailCheck.setOpacity(0);
        pswdCheck.setOpacity(0);
        if(desCheck!=null)desCheck.setOpacity(0);
        if(slCheck!=null)slCheck.setOpacity(0);
        if(goalCheck!=null)goalCheck.setOpacity(0);
    }

    public boolean checkMailPswd(ActionEvent event,boolean psyAcces ){
        SignupController controller=new SignupController();
        boolean bool;
        reset();
        EmailCheck emailBean=new EmailCheck();
        bool=emailBean.setEmail(email.getText());
        if(bool){
            controller.checkMail(emailBean);
            bool= bean.getInfo();
        }else{
            bool=true;
        }
        if (check(bool, emailCheck)) return false;
        TwoStringsBean twoStringsBean=new TwoStringsBean();
        bool=twoStringsBean.setStrings(pswd.getText(),confirmpswd.getText());
        if(check(!bool,pswdCheck))return false;
        if(psyAcces){
            SignupBean signupBean=new SignupBean();
            signupBean.setEmail(email.getText());
            signupBean.setPassword(pswd.getText());
            controller.signup(signupBean);
            bool=bean.getInfo();
            if(!bool)goToLogin();
             else {
                 Exceptions.exceptionSignupOccurred(event);
            }
        }
        return bool;
    }
    @FXML
    protected void save(ActionEvent event) {
        boolean bool;
        String[] arr;
        reset();
        SignupBean signupBean=new SignupBean();
        SignupController controller=new SignupController();
        bool=checkMailPswd(event,false);//da tornare false
        if(!bool)return;
        StringBean stringBean=new StringBean();
        stringBean.setPass(description.getText());
        controller.checkDescription(stringBean);
        bool=bean.getInfo();
        if(check(bool,desCheck))return;
        arr= new String[]{lab1.getText(), lab2.getText(), lab3.getText()};
        ParamBean paramBean =new ParamBean();
        paramBean.setParam(arr);
        controller.checkParam(paramBean);
        bool=bean.getInfo();
        if(check(bool,slCheck))return;
        stringBean=new StringBean();
        stringBean.setPass(goal.getText());
        controller.checkGoal(stringBean);
        bool=bean.getInfo();
        if(check(bool,goalCheck))return;
        signupBean.setEmail(email.getText());
        signupBean.setDescription(description.getText());
        signupBean.setGoal(goal.getText());
        signupBean.setPassword(pswd.getText());
        controller.signup(signupBean,paramBean);
        bool= bean.getInfo();
        if(bool){
            Exceptions.exceptionSignupOccurred(event);
        }
        else{
            goToLogin();
        }
    }
}
