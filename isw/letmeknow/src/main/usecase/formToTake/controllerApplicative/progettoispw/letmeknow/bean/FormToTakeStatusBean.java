package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.ConcreteUsrUser;
import progettoispw.letmeknow.controller.form.ResultForm;

public class FormToTakeStatusBean {
    ResultForm form;
    public FormToTakeStatusBean(){
        form= ConcreteUsrUser.getResultForm();
    }
    public int getFormId(){
        return form.getFormid();
    }
    public int[] getValResponse(){
        return form.getAnswers();
    }
    public String getUserid(){
        return form.getUserid();
    }
    public int getValComplete() {
        return form.getComplete();
    }
    public boolean[] getStatus() {
        return form.getStatus();
    }
}
