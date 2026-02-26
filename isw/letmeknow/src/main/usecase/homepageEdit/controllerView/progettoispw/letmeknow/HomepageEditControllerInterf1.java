package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import progettoispw.letmeknow.bean.DateBean;
import progettoispw.letmeknow.bean.UsrUserBean;
import progettoispw.letmeknow.bean.HomepageBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.HomepageEditController;


public class HomepageEditControllerInterf1 {
    protected  PageMenu pageSwitch;
    @FXML
    protected ImageView empathySlider;
    @FXML
    private ImageView humorSlider;
    @FXML
    private TextField personalDes;
    @FXML
    private ImageView positivitySlider;
    @FXML
    private TextField tag;
    @FXML
    private TextField goal;
    @FXML
    private TextField date;
    @FXML
    private Text userName;
    public HomepageEditControllerInterf1(){
        pageSwitch =new PageMenu();
    }
    public void initialize() {
        HomepageBean bean=new HomepageBean(false);
        HomepagecontrollerInterf1 home=new HomepagecontrollerInterf1();
        userName.setText("User : "+bean.getUserID());
        int[] arrayValue=bean.getParam();
        home.setSlider(empathySlider,arrayValue[0]);
        home.setSlider(humorSlider,arrayValue[1]);
        home.setSlider(positivitySlider,arrayValue[2]);
        personalDes.setPromptText(bean.getDescription());
        goal.setPromptText(bean.getGoal());
        tag.setPromptText(bean.getTag());
        arrayValue=home.bean.getDate();
        date.setPromptText(" "+arrayValue[0]+"-"+arrayValue[1]+"-"+arrayValue[2]);
    }

    @FXML
    public void saveChanges(ActionEvent event)  {
        boolean bool;
        StringBean bean;
        HomepageEditController controller=new HomepageEditController();
        UsrUserBean beanModel=new UsrUserBean();
        controller.reset();
        bean=new StringBean();
        bean.setPass(goal.getText());
        controller.setGoal(bean);
        bool=!beanModel.getInfo();
        if(bool){
            bean=new StringBean();
            bean.setPass(personalDes.getText());
            controller.setDes(bean);
            bool= !beanModel.getInfo();
        }
        if(bool){
            bean=new StringBean();
            bean.setPass(tag.getText());
            controller.setTag(bean);
            bool=!beanModel.getInfo();
        }
        if(bool){
            DateBean dateBean=new DateBean();
            dateBean.setDate(date.getText());
            controller.setDate(dateBean);
            bool= !beanModel.getInfo();
        }
        if(bool){
            goal.setText("");
            tag.setText("");
            date.setText("");
            personalDes.setText("");
            initialize();
        }
        else{
            Exceptions.exceptionEditProfileOccurred(event);
        }
    }
    @FXML
    protected void goToHome(ActionEvent event) {
        pageSwitch.switchToHome(event);
    }
    @FXML
    protected void goToISC(ActionEvent event) {
        pageSwitch.switchToISC(event);
    }
    @FXML
    protected void goToPersonalForm(ActionEvent event){
        pageSwitch.switchToPersonalForm(event);
    }
    @FXML
    protected void takeForm(ActionEvent event){
       FormCollectionResultsInterf1 formController=new FormCollectionResultsInterf1();
       formController.takeForm(event);
    }
}
