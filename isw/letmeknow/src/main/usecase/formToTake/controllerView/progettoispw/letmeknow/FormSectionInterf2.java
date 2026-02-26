package progettoispw.letmeknow;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import progettoispw.letmeknow.bean.FormResultBean;
import progettoispw.letmeknow.bean.FormToTakeStatusBean;

public class FormSectionInterf2 {
    @FXML
    AnchorPane section;
    @FXML
    ProgressBar progressBar;
    @FXML
    Slider empathySlider;
    @FXML
    Slider humorSlider;
    @FXML
    Slider optimismSlider;
    @FXML
    Text  by;
    public void initialize() {
        if(progressBar==null)setSliders();
        else setProgress();
    }
    public void setSliders(){
        FormResultBean bean=new FormResultBean();
        Slider[] sliders=new Slider[]{empathySlider,humorSlider,optimismSlider};
        int [] val=bean.getParam();
        for(int i=0;i<3;i++){
            int finalI = i;
            sliders[i].setValue(val[i]);
            sliders[i].valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                   sliders[finalI].setValue(val[finalI]);
                }
            });
        }
        by.setText(bean.getDate());
    }
    public void setProgress(){
        FormToTakeStatusBean bean= new FormToTakeStatusBean();
        progressBar.setProgress(bean.getValComplete()*0.17);
    }
}
