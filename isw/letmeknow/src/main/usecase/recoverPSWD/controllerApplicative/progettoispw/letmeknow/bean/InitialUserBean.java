package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.Factory;
import progettoispw.letmeknow.controller.User;

public class InitialUserBean {
    User user;
    public InitialUserBean(){
        Factory factory=new Factory();
        factory.concreteInitialUser(null);
        user= factory.getInitialUser();
    }
    public boolean getInfo(){
        return user.isErrorOccurred();
    }
}
