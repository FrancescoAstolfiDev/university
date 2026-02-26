package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import progettoispw.letmeknow.bean.SignupBean;

import java.util.Optional;

public class SignupPsychologistControllerInterf1 {
    @FXML
    protected TextField email;
    @FXML
    PasswordField pswd;
    @FXML
    PasswordField confirmpswd;
    @FXML
    Label emailCheck;
    @FXML
    Label pswdCheck;
    SignupControllerInterf1 signupController;

    public void initialize(){
        signupController=new SignupControllerInterf1(email,new PasswordField[]{pswd,confirmpswd},new Label[]{emailCheck,pswdCheck});
        signupController.reset();
    }

    @FXML
    protected void goToLogin() {
            Page controller=new Page();
            controller.backTo();
        }
    @FXML
    protected  void save (ActionEvent event) {
        signupController.reset();
        signupController.checkMailPswd(event,true);
    }
}
