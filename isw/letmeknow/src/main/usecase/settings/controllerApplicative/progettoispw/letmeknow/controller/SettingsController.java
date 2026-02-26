package progettoispw.letmeknow.controller;


import progettoispw.letmeknow.bean.EmailCheck;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.bean.TwoStringsBean;
import progettoispw.letmeknow.controller.user.InitialUser;

public class SettingsController {
    InitialUser user;
    public SettingsController(){
        Factory factory = new Factory();
        try {
            user = new InitialUser(factory.getUser().getUserid());
        }catch(NullPointerException e ){
            user= factory.getInitialUser();
        }
    }
    public void closeConnection(){
        ConnectionDBMS.closeConn();
    }
    public void setPassword(TwoStringsBean bean){
       String pass= bean.getString1();
       reset();
       if(pass!=null){
           user.setPassword(pass);
       }
    }
    public boolean checkMail(String email,InitialUser user){
       return user.checkEmail(email);
    }
    public void setEmail(EmailCheck bean){
        String email=bean.getEmail();
        reset();
        if(!user.checkEmail(email)){
            user.setEmail(email);
        }
        else{
            user.isErrorOccurred();
        }
    }
    public void feed(StringBean bean){
        String pass=bean.getPass();
        reset();
        if(pass!=null){
            user.setFeed(bean.getPass());
        }
    }
    public void reset(){
        user.setErrorOccurred(false);
    }
}
