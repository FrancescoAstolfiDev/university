package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.FormTouchedBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.psyuser.PsyUser;

public class HomepagePsychologistController {
    private PsyUser user;
    public HomepagePsychologistController(){
        user= ConcretePsyUser.getPsyUser();
    }
    public void increm(){
        user.incremMonth();
    }
    public void decrem(){
        user.decremMonth();
    }
    public void setSelected(FormTouchedBean bean){
        user.setIndex(bean.getFormTouched()-1);
    }
    public void  setFeed(StringBean bean){
        user.suggestQuestion(bean.getPass());
    }
}
