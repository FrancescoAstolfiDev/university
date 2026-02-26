package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.Factory;
import progettoispw.letmeknow.controller.user.InitialUser;

public class SettingsBean {
    InitialUser user;
    public SettingsBean(){
        Factory factory=new Factory();
        factory.getInitialUser();
        user= factory.getInitialUser();
    }
    public boolean getInfo(){
        return user.isErrorOccurred();
    }
}
