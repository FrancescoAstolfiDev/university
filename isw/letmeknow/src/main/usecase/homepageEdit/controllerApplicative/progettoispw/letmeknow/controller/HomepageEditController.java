package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.DateBean;
import progettoispw.letmeknow.bean.HomepageBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.usruser.UsrUser;

public class HomepageEditController {
    UsrUser user;
    HomepageBean compare;

    public HomepageEditController() {
        user = ConcreteUsrUser.getUsrUser();
        compare = new HomepageBean(false);
    }
    public void setDes(StringBean bean) {
        String pass = bean.getPass();
        if (pass != null) {
            if (!pass.contains("#")) {
                user.setErrorOccurred(true);
            } else if (!user.getDescript().equals(pass)) {
                reset();
                user.setPersonalDes(pass);
            }
        }
    }
    public void setGoal(StringBean bean) {
        String pass = bean.getPass();
            if (pass!=null &&user.getGoal() == null || !user.getGoal().equals(pass)) {
                reset();
                user.setPersonalGoal(pass);
            }
    }
    public void setDate(DateBean bean) {
        String pass = bean.getDate();
        if(pass!=null){
            reset();
            user.setPersonalData(pass);
         }
    }
    public void setTag(StringBean bean) {
        String pass = bean.getPass();
        if (pass != null) {
            if (!pass.contains("#")) {
                user.setErrorOccurred(true);
                return;
            }
            if (user.getTag() == null) user.setPersonalTag(pass);
            else if (!user.getTag().equals(pass)) {
                user.setErrorOccurred(false);
                user.setPersonalTag(pass);
            }
        }
    }
    public void reset(){
        user.setErrorOccurred(false);
    }
}


