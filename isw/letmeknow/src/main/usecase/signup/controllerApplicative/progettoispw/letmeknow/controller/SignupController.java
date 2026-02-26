package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.*;
import progettoispw.letmeknow.controller.user.InitialUser;

public class SignupController {
    private InitialUser user;
    public SignupController(){
        Factory factory=new Factory();
        user=factory.getInitialUser();
    }
    public void checkMail(EmailCheck bean){
       SettingsController controller=new SettingsController();
       boolean bool=controller.checkMail(bean.getEmail(),user);
       if(bool)user.isErrorOccurred();
    }
    public void checkDescription(StringBean bean){
        String description=bean.getPass();
        user.setErrorOccurred(false);
        if(description==null || (description.toCharArray()[0] != '#')){
            user.setErrorOccurred(true);
        }
    }
    public void checkGoal(StringBean bean){
        String goal=bean.getPass();
        user.setErrorOccurred(false);
        if(goal==null){
            user.setErrorOccurred(true);
        }
    }
    public void checkParam(ParamBean bean){
        user.setErrorOccurred(false);
        int [] param=bean.getParam();
        if(param!=null){
            for(int elem:param)if(elem<1 || elem>5)user.setErrorOccurred(true);
        }
        else{
            user.setErrorOccurred(true);
        }
    }
    public void signup(SignupBean bean,ParamBean beanParam){
        String [] all=bean.getAll();
        for(String str:all)if(str==null){
            user.isErrorOccurred();
            return;
        }
       user.registrationUSR(bean.getPassword(),bean.getEmail(),"usr",beanParam.getParam(),bean.getGoal(),bean.getDescription());
    }
    public void signup(SignupBean bean){
        if(bean.getEmail()==null || bean.getPassword()==null)user.isErrorOccurred();
        else user.registrationPSY(bean.getPassword(),bean.getEmail(),"psy");
    }
}
