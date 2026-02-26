package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.Factory;

public class FactoryBean {
    Factory factory;
    public FactoryBean(){
        factory=new Factory();
    }
    public String getType(){
        try {
            return factory.getUser().getType();
        }catch(NullPointerException e ){
            return null;
        }
    }
}
