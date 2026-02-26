package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.FormTouchedBean;

public class CollectionFormController {
   public void setTouched(FormTouchedBean bean){
        int formid=bean.getFormTouched();
        if(formid>=1&& formid<=3){
           ConcreteUsrUser.setResultForm(formid);
       }
       else ConcreteUsrUser.setResultForm(1);
   }
   public void  takeForm(){
       ConcreteUsrUser.setResultForm();
    }
}
