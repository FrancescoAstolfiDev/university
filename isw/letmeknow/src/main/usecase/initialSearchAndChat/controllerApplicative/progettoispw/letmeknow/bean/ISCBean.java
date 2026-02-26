package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.ISCController;
import progettoispw.letmeknow.controller.LastMessage;

import java.util.ArrayList;

public class ISCBean {
    private int nval;
    private ISCController controller;
    public ISCBean (int val){
        nval=val;
        controller=new ISCController(nval);
    }
    public ISCBean (){
        nval=0;
        controller=new ISCController();
    }
    public String getUserid(){
        return controller.getUid();
    }
    public String[][] exitUid(){
        int index;
        ArrayList<LastMessage>  actual= (ArrayList<LastMessage>) controller.queryUsers();
        String [][] arrStr;
        if(nval!=0) arrStr=new String[2][nval];
        else arrStr=new String[2][actual.size()];
        for(LastMessage usr : actual){
            index =actual.indexOf(usr);
            arrStr[0][index]=usr.getUserid();
            arrStr[1][index]= usr.getText();
        }
        return arrStr;
    }
    public ISCController getController(){
        return controller;
    }
}
