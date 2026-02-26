package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.LogBean;

public class LoginController {
    Factory factory;
    public LoginController(){
        factory=new Factory();
    }
    public boolean getLog(LogBean bean){
        return factory.tryLog(bean.getUserid(),bean.getPassword());
    }
}
