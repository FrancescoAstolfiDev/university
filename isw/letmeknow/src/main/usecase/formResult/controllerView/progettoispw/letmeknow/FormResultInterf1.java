//var on same line
package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import progettoispw.letmeknow.bean.FormResultBean;


public class FormResultInterf1 {
    @FXML
    ImageView empathySlider;
    @FXML
    ImageView humorSlider;
    @FXML
    ImageView optimismSlider;
    ImageView [] param;
    @FXML
    ImageView ans1;
    @FXML
    ImageView ans2;
    @FXML
    ImageView ans3;
    @FXML
    ImageView ans4;
    @FXML
    ImageView ans5;
    @FXML
    ImageView ans6;
    ImageView [] images;
    @FXML
    Text by;
    FormResultBean bean ;
    PageMenu controller ;
    int [] values;
    public FormResultInterf1(){
        bean=new FormResultBean();
        controller=new PageMenu();
    }
    public void initialize(){
        HomepagecontrollerInterf1 homepage=new HomepagecontrollerInterf1();
        controller=new PageMenu();
        values=bean.getValResponse();
        images= new ImageView[]{ans1,ans2,ans3,ans4,ans5,ans6};
        param=new ImageView[]{empathySlider,humorSlider, optimismSlider};
        int indice=0;
        for(ImageView image:images ){
            homepage.setSlider(image,values[indice++]);
        }
        values= bean.getParam();
        indice=0;
        for(ImageView image:param){
            homepage.setSlider(image,values[indice++]);
        }
        by.setText(bean.getDate());
    }
    @FXML
    protected  void goToISC(ActionEvent event){
        controller.switchToISC(event);
    }
    @FXML
    protected  void goToHome(ActionEvent event){controller.switchToHome(event);}
    @FXML
    public void goBack(ActionEvent actionEvent) {
        controller.switchToPersonalForm(actionEvent);
    }
}
