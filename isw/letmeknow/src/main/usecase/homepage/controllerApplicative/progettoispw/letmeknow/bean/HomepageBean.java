//translated variables/classes

package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.WordCheck;
import progettoispw.letmeknow.controller.ConcreteUsrUser;
import progettoispw.letmeknow.controller.usruser.UsrUser;

public class HomepageBean {
    UsrUser user;
    public HomepageBean(boolean visit){
        if(!visit)user= ConcreteUsrUser.getUsrUser();
        else user= new UsrUser(ConcreteUsrUser.getSearch().getTouched());
    }
    public String getUserID(){
        return user.getUserid();
    }
    public int[] getParam(){
        int [] ret=new int[3];
        ret[0]=user.getEmp();
        ret[1]=user.getHum();
        ret[2]=user.getOpt();
        for(int i=0;i<3;i++)if(ret[i]>5 || ret[i]<1)ret[i]=1;
        return ret;
    }
    public String getDescription(){
        return user.getDescript();
    }
    public String getTag(){
        WordCheck checkStr=new WordCheck(3,20);
        String tag=user.getTag();
        tag=checkStr.replace(tag,'#',"\n");
        tag=checkStr.check(tag);
        return tag;
    }
    public String getGoal() {
        WordCheck checkStr = new WordCheck(3, 20);
        String goal = user.getGoal();
        return checkStr.check(goal);
    }
    public int[] getDate(){
        return user.getDate();
   }
   public boolean getExpired(){
        return user.getExpired();
   }
}
