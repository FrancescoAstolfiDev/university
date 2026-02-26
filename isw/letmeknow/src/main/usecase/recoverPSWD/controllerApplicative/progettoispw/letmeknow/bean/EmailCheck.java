package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.user.InitialUser;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailCheck {
    String email;
    public boolean setEmail(String email ){
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            this.email=email;
            return true;
        } catch (AddressException e) {
            this.email=null;
            return false;
        }
    }
    public String getEmail(){
        return this.email;
    }
}
