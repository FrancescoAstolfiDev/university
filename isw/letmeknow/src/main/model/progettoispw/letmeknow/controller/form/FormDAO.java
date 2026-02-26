package progettoispw.letmeknow.controller.form;

import progettoispw.letmeknow.controller.ConnectionDBMS;
import progettoispw.letmeknow.controller.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormDAO implements Dao {
    ConnectionDBMS connDB;
    Query query;
    private static final String FORM1="112233";
    private static final String FORM2="123123";
    private static final String FORM3="123321";
    private static final int START=3;//dove iniziano le risposte
    private static final  String [] FORM={FORM1,FORM2,FORM3};
    private static final Integer [] FORMSID={1,2,3};
    public FormDAO() {
        getConn();
        getQuery();
    }
    @Override
    public void getConn() {
        connDB=new ConnectionDBMS();
    }
    @Override
    public void getQuery() {
        query=new Query();
    }
    public int [] queryResult (String userid, int formid){
        Statement stmt=null;
        ResultSet rst=null;
        int [] array=new int[6];
        boolean bool=true;
        try {
            stmt=connDB.getSTMT(stmt);
            rst=query.queryResults(stmt, userid, formid);
            if(!rst.next()){
                bool= query.insertForm(stmt,userid,formid,FORM[formid-1]);
            }
            rst=query.queryResults(stmt, userid, formid);
            if(bool){
                while (rst.next()) {
                    for(int i=0;i<6;i++){
                        array[i]=Integer.parseInt(rst.getString(i+START));
                    }
                }
                return array;
            }
            array=new int[]{1,1,1,1,1,1};
            return array;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return array;
        }finally{
            connDB.closeRSTSTMT(rst,stmt);
        }
    }
    public Boolean setAnswer(String userid, int formid, int[] answer, int complete){
        Statement stmt=null;
        try {
            stmt=connDB.getSTMT(stmt);
            return query.setResults(stmt,userid,formid,answer,complete);
        }finally{
            connDB.closeSTMT(stmt);
        }
    }
    public int [] queryParamForm (String userid,int formid) {
        Statement stmt = null;
        ResultSet rst = null;
        int[] param = new int[3];
        int indice = 0;
        try {
            stmt=connDB.getSTMT(stmt);
            rst = query.takeParamForm(stmt, userid, formid);
            while (rst.next() && rst.getString(indice+1)!=null) {
                param[indice++] = Integer.parseInt(rst.getString(indice));
                param[indice++] = Integer.parseInt(rst.getString(indice));
                param[indice++] = Integer.parseInt(rst.getString(indice));
            }
            return param;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new int[3];
        } finally {
            connDB.closeRSTSTMT(rst, stmt);
        }
    }
    private int [] queryParam (String userid){
        Statement stmt=null;
        ResultSet rst = null;
        stmt=connDB.getSTMT(stmt);
        try{
            int [] param=new int[3];
            int indice=0;
            rst=query.takeParam(stmt,userid);
            while(rst.next()){
                param[indice++]=Integer.parseInt(rst.getString(indice));
                param[indice++]=Integer.parseInt(rst.getString(indice));
                param[indice++]=Integer.parseInt(rst.getString(indice));
            }
            return param;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new int[]{1,1,1};
        }finally{
            connDB.closeSTMT(stmt);
        }
    }

    public Boolean close(String userid,int formid){
        Statement stmt=null;
        boolean bool;
        int [] param=queryParam(userid);
        stmt=connDB.getSTMT(stmt);
        bool=query.close(stmt,userid,formid,param);
        connDB.closeSTMT(stmt);
        return bool;
    }
    public String queryData(String userid,int formid){
        Statement stmt=null;
        ResultSet rst=null;
        int [] data=new int [3];
        String out=null;
        try {
            stmt=connDB.getSTMT(stmt);
            rst=query.takeDate(stmt,userid,formid);
            if(rst.next() && rst.getString(1)!=null){
                //conversione data da formato americano a italiano
                out=rst.getString(1);
                int end=out.indexOf("-");
                data[2]=(Integer.parseInt(out.substring(0,end)));
                int beg=end;
                end=out.indexOf("-",end+1);
                data[1]=(Integer.parseInt(out.substring(beg+1,end)));
                beg=end;
                end=out.length();
                data[0]=(Integer.parseInt(out.substring(beg+1,end)));
                out=String.format("%d-%d-%d",data[0],data[1],data[2]);
            }
            return out;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally{
            connDB.closeSTMT(stmt);
        }
    }
    public Boolean setCalculated(String userid,int formid){
        Statement stmt=null;
        try {
            stmt=connDB.getSTMT(stmt);
            return query.setCalculated(stmt,userid,formid);
        }finally{
            connDB.closeSTMT(stmt);
        }
    }
    public int takeAForm(String userid){
        Statement stmt=null;
        ResultSet rst=null;
        Integer val;
        List<Integer> formid =new ArrayList<>(Arrays.asList(FORMSID));
        try {
            stmt=connDB.getSTMT(stmt);
            rst=query.newForm(stmt,userid);
            while(rst.next()){
                val=Integer.parseInt(rst.getString(1));
                if(!rst.getString(2).equals("6"))return val;
                formid.remove(val);
            }
            if(!formid.isEmpty())return formid.get(0);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }finally{
            connDB.closeRSTSTMT(rst,stmt);
        }
    }
}