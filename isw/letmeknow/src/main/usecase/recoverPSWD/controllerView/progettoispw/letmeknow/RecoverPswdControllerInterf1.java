package progettoispw.letmeknow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import progettoispw.letmeknow.bean.EmailCheck;
import progettoispw.letmeknow.bean.InitialUserBean;
import progettoispw.letmeknow.controller.RecoverPswdController;


public class RecoverPswdControllerInterf1
{
    @FXML
    TextField email;
    @FXML
    Label show;
    public void initialize(){
        show.setOpacity(0);
    }
    @FXML
    public void sendMail( ){
        InitialUserBean initialUserBean=new InitialUserBean();
        show.setOpacity(0);
        String address=email.getText();
        RecoverPswdController controller=new RecoverPswdController();
        EmailCheck bean=new EmailCheck();
        boolean bool= bean.setEmail(address);
        if(bool)controller.sendMail(bean);
        else {
            show.setOpacity(1);
            return ;
        }
        bool=initialUserBean.getInfo();
        if(bool){
            show.setOpacity(1);
        }
        else {
           backToLogin();
        }
    }
    @FXML
    protected void backToLogin()  {
        Page pageSwitch=new Page();
        pageSwitch.backTo();
    }

}
