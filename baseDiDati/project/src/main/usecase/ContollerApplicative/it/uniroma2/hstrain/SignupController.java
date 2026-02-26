package it.uniroma2.hstrain;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController implements ControllerApplicative{
    @Override
    public void start() {
        SignupView signupView =new SignupView();
        try {
            SignupBean bean ;
            bean=signupView.signupFun();
            LoginDao loginDao=new LoginDao();
            loginDao.signup(bean);
            LoginController contr=new LoginController();
            contr.start();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
