package progettoispw.letmeknow;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import progettoispw.letmeknow.bean.IntBean;
import progettoispw.letmeknow.bean.ParamBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.SearchController;

public class SearchControllerInterf1 {
   PageMenu pageSwitch;
    SearchController controller;
    @FXML
    protected Slider posSL;
    @FXML
    protected Slider empSL;
    @FXML
    protected Slider humSL;
    @FXML
    protected Slider carefulness;
    @FXML
    protected Label lb1;
    @FXML
    protected Label lb2;
    @FXML
    protected Label lb3;
    @FXML
    protected Label lb4;
    @FXML
    protected TextField inputTraits;
    @FXML
    protected TextField inputGoal;
    protected boolean [] clicked;
    @FXML
    protected ProgressBar progressBar;
    @FXML
    protected Button   buttonSearch;
    protected Slider [] sl ;
    protected Label[] labels;
    protected double progress;
    public SearchControllerInterf1(){
        progress=0;
        clicked= new boolean[]{false, false, false, false, false, false, false};
        pageSwitch=new PageMenu();
        controller=new SearchController();
    }
    protected void incremProgress(int index){
        if(!clicked[index]){
            progress=progress+0.17;
            clicked[index]=true;
            progressBar.setProgress(progress);
            buttonSearch.setOpacity(progress);
        }
    }
    public void initialize(){
        labels= new Label[]{lb1, lb2, lb3,lb4};
        sl=new Slider[] {empSL,humSL,posSL, carefulness};
        for(int i=0;i<4;i++){
            int finalI = i;
            sl[i].valueProperty().addListener(
                    (ObservableValue<? extends Number> observableValue, Number number, Number t1) ->{
                    incremProgress(finalI);
                    progressBar.setProgress(progress);
                    if(finalI< 4)labels[finalI].setText(""+(int)sl[finalI].getValue());
                });
            }
        inputTraits.textProperty().addListener((observableValue, s, t1) ->incremProgress(4));
        inputGoal.textProperty().addListener((observableValue, s, t1) -> incremProgress(5));
    }
    protected void researchByParameter(){
        IntBean beanAff=new IntBean();
        ParamBean beanParam=new ParamBean();
        int [] paramS=new int[labels.length-1];
        for(int i=0;i<4;i++){
                if (i==3){
                    if (clicked[3]) {
                        beanAff.setVal((int)sl[i].getValue());
                    }
                    else{
                        beanAff.setVal(0);
                    }
                }
                else{
                    if(clicked[i])paramS[i]=((int)sl[i].getValue());
                    else paramS[i]=0;
                }
            }
       beanParam.setParam(paramS);
       controller.enterParamSearch(beanParam);
       controller.enterAffinity(beanAff);
    }
    protected void researchByGoal(){
        if (clicked[5]){
            StringBean bean=new StringBean();
            bean.setPass(inputGoal.getText());
            controller.enterGoal(bean);
    }}
    protected void researchByDescripription(){
        if (clicked[4]){
            StringBean bean=new StringBean();
            bean.setPass(inputTraits.getText());
            controller.enterDes(bean);
        }
    }
    @FXML
    protected  void affinity(ActionEvent event ){
        controller.toMe();
        goResult(event);
    }
    @FXML
    public void search(ActionEvent actionEvent) {
        controller.reset();
        researchByParameter();
        researchByDescripription();
        researchByGoal();
        goResult(actionEvent);
    }
    @FXML
    protected void goToHome(ActionEvent event) {
        pageSwitch.switchToHome(event);
    }
    @FXML
    protected void goToPersonalForm(ActionEvent event)  {
        pageSwitch.switchToPersonalForm(event);
    }
    @FXML
    protected void goResult(ActionEvent event) {
        pageSwitch.switchTo("resultSearch/interf1.fxml",event,"your research result");
    }
    @FXML
    protected void goBack()  {
        pageSwitch.backTo();
    }

}
