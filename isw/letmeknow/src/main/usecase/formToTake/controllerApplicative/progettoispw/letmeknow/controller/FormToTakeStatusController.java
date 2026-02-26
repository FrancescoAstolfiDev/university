package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.ParamBean;
import progettoispw.letmeknow.controller.form.ResultForm;

public class FormToTakeStatusController {
    public void setValResponse(ParamBean bean){
        ResultForm form=ConcreteUsrUser.getResultForm();
        int [] answers= bean.getParam();
        for(int i=0;i< answers.length;i++){
            if(answers[i]<1)answers[i]=-1;
            else if (answers[i]>5)answers[i]=5;
        }
        form.setAnswers(answers);
    }
}
