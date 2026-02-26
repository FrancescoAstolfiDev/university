package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.EmailCheck;
import progettoispw.letmeknow.controller.user.InitialUser;

public class RecoverPswdController {
        InitialUser user;
        public RecoverPswdController(){
            Factory factory=new Factory();
            user=factory.getInitialUser();
        }
        public void sendMail(EmailCheck bean){
            user.setErrorOccurred(false);
            user.composeMail(bean.getEmail());
        }
}
