package progettoispw.letmeknow;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import progettoispw.letmeknow.bean.ISCBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.ISCController;

public class InitialSearchAndChatControllerInterf1 {
    private PageMenu pageSwitch;
    private String userid;
    private ISCController controller;
    private static final String UID="userid";
    @FXML
    Group group1;
    @FXML
    Group group2;
    @FXML
    Group group3;
    @FXML
    Group group4;
    @FXML
    Group extGroup1;
    @FXML
    Group extGroup2;
    @FXML
    Group extGroup3;
    @FXML
    Group extGroup4;
    @FXML
    TextField searchBar;
    int nval;
    ISCBean bean;
    String [] uids;
    Group [] chatExtGroup;
    Group []chatGroup;
    public InitialSearchAndChatControllerInterf1(){
        nval=4;
        bean=new ISCBean(nval);
        uids=new String[4];
        pageSwitch =new PageMenu();
        controller= bean.getController();
    }
    public InitialSearchAndChatControllerInterf1(String[] input) {
        uids=input;
        bean=new ISCBean(nval);
        pageSwitch =new PageMenu();
    }
    public void initialize(){
        userid=bean.getUserid();
        chatExtGroup=new Group[]{extGroup1,extGroup2,extGroup3,extGroup4};
        chatGroup=new Group []{group1,group2,group3,group4};
        outputValChat();
        searchBar.textProperty().addListener((observableValue, s, t1) -> {
            if(searchBar.getText().equals("")){
                controller.reset();
            }
        });
    }
    public String [] prevOutputValChat(Group [] group1, Group [] group2, int input ){
        chatExtGroup=group1;
        chatGroup=group2;
        nval=input;
        bean=new ISCBean(nval);
        return outputValChat();
    }
    @FXML
    public  String [] outputValChat(){
        for(int i=0;i<nval;i++){
            chatExtGroup[i].setOpacity(1);
        }
        String [][] innerusers =bean.exitUid();
        String [] strUid=innerusers[0];
        String [] strLst=innerusers[1];
        for(int i=0;i<nval;i++){
            if(strUid[i]==null || strLst[i]==null){
                chatExtGroup[i].setOpacity(0);
            }
            ObservableList<Node> inner= chatGroup[i].getChildren();
            for(Node elem:inner){
                Text text=(Text)elem;
                if(elem.getId()!=null && elem.getId().equals(UID)){
                    uids[i]=strUid[i];
                    text.setText("#"+strUid[i]);
                }
                else text.setText(strLst[i]);
            }
        }
        return uids;
    }
    private String getUid(String string){
        int indice;
        String sub;
        if(string==null ||string.length()<8 )return string ;
        else{
            indice=string.indexOf("||");
            sub=string.substring(0,indice);
            if(!sub.equals(userid)) {
                return sub;
            }
            return string.substring(indice+2);
            }
        }
    private String[] getUids(String[] uids) {
        for(int i=0;i<3;i++){
            uids[i]=getUid(uids[i]);
        }
        return uids;
    }
    public void touchChat(ActionEvent event){
        Button button=(Button) event.getTarget();
        if(button.getOpacity()<1)return ;
        uids=getUids(uids);
        for(int i=1;i<7;i++){
            String compare="chat"+i;
            if(compare.equals(button.getId())){
                touchChat(uids[i-1],event);
            }
        }

    }
    public void touchChat(String userid,ActionEvent event){
        StringBean stringBean =new StringBean();
        stringBean.setPass(userid);
        controller.setTouched(stringBean);
        pageSwitch.switchTo("chat/interf1.fxml",event,"chat");
    }
    public void visit(ActionEvent event) {
        uids=getUids(uids);
       ResultSearchControllerInterf1 resultSearch =new ResultSearchControllerInterf1(uids);
       resultSearch.setUids(uids);
       resultSearch.visit(event);
    }
    @FXML
    protected void searchMessage(){
        StringBean stringBeanbean=new StringBean();
        stringBeanbean.setPass(searchBar.getText());
        controller.searchMessage(stringBeanbean);
        outputValChat();
    }
    @FXML
    protected void goToHome(ActionEvent event)  {
        pageSwitch.switchToHome(event);
    }
    @FXML
    protected void goToPersonalForm(ActionEvent event){
        pageSwitch.switchToPersonalForm(event);
    }
    @FXML
    protected void goToSettings(ActionEvent event) {
        pageSwitch.switchToSettings(event);
    }
    @FXML
    protected void goToSearch(ActionEvent event) {
        pageSwitch.switchToSearch(event);
    }
    public void goToISC(ActionEvent event) {
        pageSwitch.switchToISC(event);
    }
}
