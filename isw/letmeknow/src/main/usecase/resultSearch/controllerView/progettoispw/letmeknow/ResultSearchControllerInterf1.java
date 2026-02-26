package progettoispw.letmeknow;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import progettoispw.letmeknow.bean.BeanResultSearch;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.ResultSearchController;

public class ResultSearchControllerInterf1 {
    @FXML
    protected Group group1;
    @FXML
    protected Group group2;
    @FXML
    protected Group group3;
    @FXML
    protected Group group4;
    Group [] visitGroup;
    String [] uids;
    static final String UID_CONTENT ="Userid  : #";
    static final String MSG_WORKON ="Working On :";
    static final String DESCRIPTION ="About me :";
    private PageMenu pageSwitch;
    BeanResultSearch beanVisit;
    int nval;
    public ResultSearchControllerInterf1(){
        nval=4;
        beanVisit=new BeanResultSearch(nval);
        pageSwitch=new PageMenu();
        uids=new String[nval];
    }

    public ResultSearchControllerInterf1(String[] input) {
        uids=input;
        beanVisit=new BeanResultSearch();
    }

    public void initialize(){
        visitGroup=new Group[]{group1,group2,group3,group4};
        outputVal();
    }

    public String[] prevOutputVal(Group [] input, int inputnval,BeanResultSearch bean){
        visitGroup=input;
        nval=inputnval;
        uids=new String[nval];
        this.beanVisit=bean;
        return outputVal();
    }
    @FXML
    public String [] outputVal(){
        for(int i=0;i<nval;i++){
            visitGroup[i].setOpacity(1);
        }
        String [] [] users= beanVisit.getUsers();
        String[] strDes= users[2];
        String[] strGoal=users[1];
        String[] strUid=users[0];
        for(int i=0;i<nval;i++){
            ObservableList<Node> externList= visitGroup[i].getChildren();
            Group group =(Group)externList.get(2);
            ObservableList<Node>inner=group.getChildren();
            if(strUid[i]!=null && !strUid[i].equals("null")){
                for(Node elem:inner){
                    Text text=(Text)elem;
                    if(text.getText().contains(UID_CONTENT)){
                        text.setText(UID_CONTENT +strUid[i]);
                        uids[i]=strUid[i];
                    }
                    else if(text.getText().contains(MSG_WORKON)){
                        text.setText(MSG_WORKON+strGoal[i]);
                    }
                    else if(text.getText().contains(DESCRIPTION))text.setText(DESCRIPTION+strDes[i]);
                }
            }
            else {
                visitGroup[i].setOpacity(0);
            }}
        return uids;
    }
    @FXML
    public void touchChat(ActionEvent event){
        InitialSearchAndChatControllerInterf1 iscController=new InitialSearchAndChatControllerInterf1(uids);
        iscController.touchChat(event);
    }
    public void setUids(String [] input){
        uids=input;
    }
    public void touchVisit(ActionEvent event){
        Button button = (Button) event.getTarget();
        if (button.getOpacity() < 1) return;
        for(int i=1;i<7;i++){
            String compare="home"+i;
            if(compare.equals(button.getId())){
                setTouchedHome(uids[i-1]);
            }
        }
    }
    @FXML
    public void visit(ActionEvent event) {
        touchVisit(event);
        pageSwitch.switchTo("homepageOthers/interf1.fxml",event,"visit");
    }
    @FXML
    protected void goBack()  {
        pageSwitch.backTo();
    }
    @FXML
    protected void goToHome(ActionEvent event) {
        pageSwitch.switchToHome(event);
    }
    @FXML
    protected void goToPersonalForm(ActionEvent event){
        pageSwitch.switchToPersonalForm(event);
    }
    public void setTouchedHome(String user){
        ResultSearchController controller=new ResultSearchController();
        StringBean stringBean=new StringBean();
        stringBean.setPass(user);
        controller.touched(stringBean);
    }

}
