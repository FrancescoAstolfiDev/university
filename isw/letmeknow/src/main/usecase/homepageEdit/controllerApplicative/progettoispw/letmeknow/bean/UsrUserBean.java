package progettoispw.letmeknow.bean;
import progettoispw.letmeknow.controller.Factory;
import progettoispw.letmeknow.controller.User;

public class UsrUserBean {
   User user;
   public UsrUserBean(){
       Factory factory=new Factory();
       user=factory.getUser();
   }
   public boolean getInfo(){
       return user.isErrorOccurred();
   }
}
