package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.IntBean;
import progettoispw.letmeknow.bean.ParamBean;
import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.search.Search;
import progettoispw.letmeknow.controller.usruser.UsrUser;

public class SearchController {
    private Search find;
    public SearchController(){
        find= ConcreteUsrUser.getSearch();
    }
    public void enterAffinity(IntBean bean){
        find.setAffinity(bean.getVal());
    }
    public void enterAffinity(int val){
        find.setAffinity(val);
    }
    public void enterGoal(StringBean bean){
        String pass= bean.getPass();
        if(pass!=null)enterGoal(pass);
    }
    public void enterGoal(String pass){
       find.goalSearch(pass);
    }
    public void enterDes(StringBean bean){
        String pass=bean.getPass();
        if(pass!=null){
            enterDes(pass);
        }
    }
    public void enterDes(String pass){
        find.descrSearch(pass);
    }
    public void enterParamSearch(ParamBean bean ){
        boolean bool=false;
        int []val=bean.getParam();
        for(int i =0;i<3;i++)if(val[i]>1)bool=true;
        if(bool)enterParamSearch(bean.getParam());
    }
    public void enterParamSearch(int [] param){
        if(param!=null)find.parametricSetSearch(param);
    }
    public void toMe(){
        UsrUser me = ConcreteUsrUser.getUsrUser();
        enterAffinity(0);
        enterGoal(me.getTag());
        enterDes(me.getDescript());
        enterParamSearch(new  int[] {me.getEmp(),me.getHum(),me.getOpt()} );
    }
    public void reset(){
        find.reset();
    }
}
