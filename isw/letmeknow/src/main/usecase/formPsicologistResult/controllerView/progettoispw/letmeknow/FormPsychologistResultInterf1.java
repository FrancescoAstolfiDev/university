//spelling checkActive + same line variables
package progettoispw.letmeknow;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import progettoispw.letmeknow.bean.FormPsychologistResultBean;

public class FormPsychologistResultInterf1 {
    @FXML
    Text by;
    @FXML
    Slider sl1;
    @FXML
    Slider sl2;
    @FXML
    Slider sl3;
    @FXML
    Slider sl4;
    @FXML
    Slider sl5;
    @FXML
    Slider sl6;
    @FXML
    Label lb1;
    @FXML
    Label lb2;
    @FXML
    Label lb3;
    @FXML
    Label lb4;
    @FXML
    Label lb5;
    @FXML
    Label lb6;

    FormPsychologistResultBean bean;
    Page controller;
    public FormPsychologistResultInterf1(){
        bean=new FormPsychologistResultBean();
        controller=new Page();
    }
    public void initialize(){
        Slider [] sliders;
        Label [] labels;
        sliders=new Slider[]{sl1,sl2,sl3,sl4,sl5,sl6};
        labels=new Label[]{lb1,lb2,lb3,lb4,lb5,lb6};
        float [] values= bean.getSelected();
        for(int i=0;i<6;i++){
            int index = i;
            sliders[i].setValue(values[i+1]);
            labels[i].setText(String.valueOf(values[i+1]));
            sliders[i].valueProperty().addListener(
                    (ObservableValue<? extends Number> observableValue, Number number, Number t1) ->{
                    sliders[index].setValue(values[index+1]);
                    labels[index].setText(String.valueOf(values[index+1]));
            });
        }
        by.setText((int)values[0]+"users");
    }
    @FXML
    public void goBack(){
        controller.backTo();
    }
}
