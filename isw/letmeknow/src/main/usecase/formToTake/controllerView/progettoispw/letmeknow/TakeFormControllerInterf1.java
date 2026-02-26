package progettoispw.letmeknow;


import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

import progettoispw.letmeknow.bean.FormToTakeStatusBean;
import progettoispw.letmeknow.bean.ParamBean;
import progettoispw.letmeknow.controller.FormToTakeStatusController;

public class TakeFormControllerInterf1 {
    @FXML
    protected Text idForm;
    @FXML
    protected Slider sl1;
    @FXML
    protected Slider sl2;
    @FXML
    protected Slider sl3;
    @FXML
    protected Slider sl4;
    @FXML
    protected Slider sl5;
    @FXML
    protected Slider sl6;
    @FXML
    protected Label lb1;
    @FXML
    protected Label lb2;
    @FXML
    protected Label lb3;
    @FXML
    protected Label lb4;
    @FXML
    protected Label lb5;
    @FXML
    protected Label lb6;
    @FXML
    protected ProgressBar progressBar;
    protected PageMenu pageSwitch;
    protected Slider[] sl;
    protected Label[] labels;
    protected boolean [] values;
    protected  boolean [] locked;
    protected int [] response;
    protected double progress;
    public TakeFormControllerInterf1() {
        progress=0;
        pageSwitch =new PageMenu();
    }

    private boolean[] not(boolean []bool){
        boolean [] currbool=new boolean[6];
        for(int i=0;i< bool.length;i++){
            if(bool[i]) currbool[i]=false;
            else currbool[i]=true;
        }
        return currbool;
    }
    public void initialize(){
       FormToTakeStatusBean bean=new FormToTakeStatusBean();
       int completed=bean.getValComplete();
       if(completed==6){
            goBack();
        }
        progress= completed*0.17;
        progressBar.setProgress(progress);
        setValues();
        progressBar.setProgress(progress);
    }
    public void setValues(){
        FormToTakeStatusBean bean=new FormToTakeStatusBean();
        locked=bean.getStatus();
        response= bean.getValResponse();
        sl=new Slider[] {sl1,sl2,sl3,sl4,sl5,sl6};
        labels= new Label[]{lb1, lb2, lb3, lb4, lb5, lb6};
        values=not(locked);
        for(int i=0;i<6;i++){
            if(response[i]!=-1){
                sl[i].setValue(response[i]);
            }
            int finalI = i;
            sl[i].valueProperty().addListener(
                    (ObservableValue<? extends Number> observableValue, Number number, Number t1) ->{
                    if(values[finalI]){
                        progress+=0.17;

                        values[finalI]=false;
                    }
                    if(locked[finalI]){
                        sl[finalI].setValue(response[finalI]);
                    }
                    labels[finalI].setText(""+(int)sl[finalI].getValue());
                });
        }
    }
    @FXML
    protected void save(){
        for (int i=0;i<6;i++) {
            if (!values[i]) {
                locked[i]=true;
                response[i]=(int)sl[i].getValue();
            }
        }
        ParamBean bean=new ParamBean();
        bean.setParam(response);
        FormToTakeStatusController controller=new FormToTakeStatusController();
        controller.setValResponse(bean);
        initialize();
    }
    @FXML
    protected void goBack() {
        pageSwitch.backTo();
    }
    @FXML
    protected  void goToISC(ActionEvent event){
        pageSwitch.switchToISC(event);
    }
    @FXML
    protected  void goToHome(ActionEvent event){
        pageSwitch.switchToHome(event);}

}
