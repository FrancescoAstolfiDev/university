package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import progettoispw.letmeknow.bean.UsrUserBean;
import progettoispw.letmeknow.bean.FormTouchedBean;
import progettoispw.letmeknow.bean.HomepagePsychologistBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.HomepagePsychologistController;

public class HomepagePsychologistInterf1 {
    Page pageSwitch;
    @FXML
    Label count1;
    @FXML
    Label count2;
    @FXML
    Label count3;
    @FXML
    Label month;
    @FXML
    TextArea feedback;
    public static final String  USERS="users";
    protected HomepagePsychologistController controller;
    public HomepagePsychologistInterf1(){
        pageSwitch =new Page();
        controller=new HomepagePsychologistController();
    }
    public void initialize(){
        HomepagePsychologistBean bean=new HomepagePsychologistBean();
        int[] current=bean.getForms();
        count1.setText(current[0]+USERS);
        count2.setText(current[1]+USERS);
        count3.setText(current[2]+USERS);
        month.setText(bean.getMonth());
    }
    public void increm() {
        controller.increm();
        initialize();
    }
    public void decrem() {
        controller.decrem();
        initialize();
    }
    @FXML
    public int  getSelected(ActionEvent event){
        Button button=(Button) event.getTarget();
        int val=0;
        FormTouchedBean bean=new FormTouchedBean();
        switch(button.getId()){
            case "form1" :{
                val=1;
                break;
            }
            case "form2" :{
                val=2;
                break;
            }
            case "form3" :{
                val=3;
                break;
            }
            default :{
                event.consume();
            }
        }
        bean.setFormTouched(val);
        controller.setSelected(bean);
        return val;
     }
     @FXML
     public void select(ActionEvent event){
        int formid=getSelected(event);
        pageSwitch.switchTo("formResultPsychologist/form"+formid+"interf1.fxml",event,"users results");
     }
    public void suggestForm(){
        feedback.setStyle("-fx-border-color: white;");
        StringBean bean=new StringBean();
        UsrUserBean beanError=new UsrUserBean();
        bean.setPass(feedback.getText());
        controller.setFeed(bean);
        if(beanError.getInfo()){
            feedback.setText("");
        }
        else{
            feedback.setStyle("-fx-border-color: red;");
        }
    }
    public void backTo(){
        pageSwitch.backTo();
    }
    public void goToSettings(ActionEvent event ){
        pageSwitch.switchTo("settingsPsychologist/interf1.fxml",event,"Settings");
    }
}
