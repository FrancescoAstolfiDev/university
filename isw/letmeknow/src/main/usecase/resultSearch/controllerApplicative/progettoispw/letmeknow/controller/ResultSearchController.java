package progettoispw.letmeknow.controller;


import progettoispw.letmeknow.bean.StringBean;
import progettoispw.letmeknow.controller.search.Search;
import progettoispw.letmeknow.controller.usruser.UsrUser;

import java.util.ArrayList;
import java.util.List;

public class ResultSearchController {
    private int count;
    private int nVal;
    private Search search;
    public ResultSearchController(Integer n){
        nVal=n;
        count=0;
        search= ConcreteUsrUser.getSearch();
    }
    public ResultSearchController(){
        search= ConcreteUsrUser.getSearch();
    }
    public void attach(InnerUsers elem,List <InnerUsers>formatted ){
        formatted.add(elem);
    }
    InnerUsers actual;
    public List<InnerUsers> queryUsers(){
        int indice;
        actual=null;
        List<String> founded= search.getArrayList();
        ArrayList<InnerUsers>formatted=new ArrayList<>();
        count = check(count, (ArrayList<String>) founded);
        UsrUser user;
        for (String userid : founded) {
            if(userid==null)return formatted;
            user=new UsrUser(userid);
            indice = founded.indexOf(userid);
            if (indice >= count && indice < count + nVal) {
                actual = new InnerUsers(user);
                attach(actual,formatted);
            }
        }
        count+=nVal;
        return formatted;
    }

    private Integer check(Integer count,ArrayList<String> founded) {
        if(count>=founded.size()){
            return 0;
        }
        return count;
    }
    public void touched(StringBean bean){
        search.setTouched(bean.getPass());
    }
    public int[] nVisit(){
        return search.getnVisit();
    }
    public void reset(){
        search.reset();
    }
}