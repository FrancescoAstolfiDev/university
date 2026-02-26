package progettoispw.letmeknow.bean;
import progettoispw.letmeknow.controller.ConcretePsyUser;
import progettoispw.letmeknow.controller.psyuser.PsyUser;

public class HomepagePsychologistBean {
    PsyUser user;
    public HomepagePsychologistBean(){
        user=ConcretePsyUser.getPsyUser();
        user.getLists();
    }
    public String getMonth(){
        return user.getMonthName();
    }
    public int[] getForms(){
        int[] ret=new int[3];
        float[] current;
        for(int i=0;i<3;i++){
            current=user.getList();
            if(current.length!=0)ret[i]=(int)current[0];
        }
        return ret;
    }
}
