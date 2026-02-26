//utenteusr never used. should I cancel it with also library?

package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.ConcreteUsrUser;
import progettoispw.letmeknow.controller.form.ResultForm;

public class FormResultBean {
    ResultForm form;
    public FormResultBean(){
        form= ConcreteUsrUser.getResultForm();
    }
    public int[] getValResponse(){
        return form.getAnswers();
    }
    public int [] getParam () {
        return form.getParam();
    }
    public String getDate () {
        return form.getDate();
    }
}