package progettoispw.letmeknow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import progettoispw.letmeknow.bean.BeanResultSearch;
import progettoispw.letmeknow.controller.SearchController;

public class ButtonBarInterf2 {
    PageMenu pageSwitch;
    @FXML
    Button home;
    @FXML
    Button chat;
    @FXML
    Button form;
    @FXML
    Button search;
    @FXML
    Button settings;
    public ButtonBarInterf2(){
        pageSwitch=new PageMenu();
    }
    public void initialize(){
        Button [] buttons=new Button[] {home,chat,form,search,settings};
        for(Button button:buttons)button.setStyle("-fx-opacity: 0.5");
    }
    public void goToChat(ActionEvent actionEvent) {
        pageSwitch.switchToChat(actionEvent);
    }
    public void goToHome(ActionEvent actionEvent) {
        SearchController controller=new SearchController();
       controller.reset();
        pageSwitch.switchToHome(actionEvent);
    }
    public void goToForm(ActionEvent actionEvent) {
        pageSwitch.switchToPersonalForm(actionEvent);
    }
    public void goToSettings(ActionEvent actionEvent) {
        pageSwitch.switchToSettings(actionEvent);
    }
    public void goToSearch(ActionEvent actionEvent) {
        pageSwitch.switchToSearch(actionEvent);
    }
    public void setOpacity(Button button ){button.setStyle("-fx-opacity: 1.0");}
    public void setHome() {
        setOpacity(home);
    }
    public void setSearch() {
        setOpacity(search);
    }
    public void setSettings() {setOpacity(settings);}
    public void setPersonalForm() {setOpacity(form);}
}
