package it.uniroma2.hstrain;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController implements ControllerApplicative {
    CredentialsBean cred=null;
    @Override
    public void start() {
        try {
            int choice = LoginView.sigupLogin();
            if(choice==2) cred = LoginView.authenticate();
            else {
                SignupController contr=new SignupController();
                contr.start();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        try {
            cred = new LoginDao().execute(cred.getUsername(), cred.getPassword());
        } catch(DAOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CredentialsBean getCred() {
        return cred;
    }
}
