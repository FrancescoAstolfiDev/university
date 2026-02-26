
package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import progettoispw.letmeknow.bean.FormToTakeStatusBean;
import progettoispw.letmeknow.bean.FormTouchedBean;
import progettoispw.letmeknow.bean.HomepageBean;
import progettoispw.letmeknow.controller.CollectionFormController;

public class FormCollectionResultsInterf1 {
    PageMenu pageSwitch;
    public static final String INTERF="interf1.fxml";
    @FXML
    protected Text idUser;
    public FormCollectionResultsInterf1(){
        pageSwitch =new PageMenu();
    }
    public void initialize(){
        HomepageBean bean=new HomepageBean(false);
        idUser.setText("User"+ bean.getUserID());
    }
    @FXML
    protected void goToSettings(ActionEvent event) {
        pageSwitch.switchToSettings(event);
    }
    protected  void which(int i,ActionEvent event){
        FormTouchedBean bean=new FormTouchedBean();
        bean.setFormTouched(i);
        CollectionFormController controller=new CollectionFormController();
        controller.setTouched(bean);
        String name;
        String title;
        FormToTakeStatusBean formBean=new FormToTakeStatusBean();
        if(formBean.getValComplete()==6){
            name="formResult/form"+i+INTERF;
        }
        else{
            name="formToTake/form"+i+INTERF;
        } title="form"+i;
        pageSwitch.switchTo(name,event,title);
    }
    @FXML
    protected void urResult(ActionEvent event) {
        Button button=(Button) event.getTarget();
        switch(button.getText()){
            case "FORM 3":{
                which(3,event);
                break;
            }
            case "FORM 1":{
                which(1,event);
                break;
            }
            case "FORM 2":{
                which(2,event);
                break;
            }
            default:{
                event.consume();
            }
        }

    }
    @FXML
    public void takeForm(ActionEvent event){
        CollectionFormController controller=new CollectionFormController();
        controller.takeForm();
        FormToTakeStatusBean innerBean=new FormToTakeStatusBean();
        int val = innerBean.getFormId();
        if(innerBean.getValComplete()<=6){
            pageSwitch.switchTo("formToTake/form"+val+INTERF,event,"form"+val);
        }
        else{
            pageSwitch.switchTo("formResult/form"+val+INTERF,event,"form"+val);
        }
    }
    @FXML
    protected  void goToISC(ActionEvent event){
        pageSwitch.switchToISC(event);
    }
    @FXML
    protected  void goToPersonalForm(ActionEvent event){
        pageSwitch.switchToHome(event);
    }
    @FXML
    protected  void goToHome(ActionEvent event){
        pageSwitch.switchToHome(event);}
}
