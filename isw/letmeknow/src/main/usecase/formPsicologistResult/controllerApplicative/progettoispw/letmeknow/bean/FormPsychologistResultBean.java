package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.ConcretePsyUser;
import progettoispw.letmeknow.controller.psyuser.PsyUser;

public class FormPsychologistResultBean {
    PsyUser user;
    public FormPsychologistResultBean(){
        user= ConcretePsyUser.getPsyUser();
    }
    public float [] getSelected(){
        return user.getSelected();
    }
}
