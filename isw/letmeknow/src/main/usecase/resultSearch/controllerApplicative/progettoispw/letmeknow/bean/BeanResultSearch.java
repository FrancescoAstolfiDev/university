package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.InnerUsers;
import progettoispw.letmeknow.controller.ResultSearchController;

import java.util.List;

public class BeanResultSearch {
    Integer nval;
    ResultSearchController controller;
    public BeanResultSearch(int val){
        nval=val;
        controller=new ResultSearchController(nval);
    }
    public BeanResultSearch(){
        controller=new ResultSearchController();
    }
    public String[][] getUsers(){
        int index;
        String [][] arrStr=new String[3][nval];
        List<InnerUsers> actual=controller.queryUsers();
        for(InnerUsers usr : actual){
            index=actual.indexOf(usr);

            arrStr[0][index]=usr.getUserid();
            arrStr[1][index]=usr.getGoal();
            arrStr[2][index]=usr.getDescription();
        }
        return arrStr;
    }
    public int[] getnval(){
        return controller.nVisit();
    }
}
