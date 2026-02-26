package progettoispw.letmeknow.controller.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Query {
    public ResultSet queryResults(Statement stmt, String userid ,int formid) {
        try {
            String sql = String.format(" SELECT *  FROM forms where userid = '%s' and formid='%d'", userid, formid);
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            return null;
        }
    }
    public boolean insertForm(Statement stmt,String userid,int formid,String about){
        try {
            String sql=String.format("INSERT INTO `forms` (`formid`, `userid`,`about`) VALUES ('%d', '%s','%s');",formid,userid,about);
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean setResults(Statement stmt, String userid, int formid, int[] answer, int complete){
        try {
            String sql = String.format(" UPDATE `forms` " +
                            "SET `q1` = '%d', `q2` = '%d', `q3` = '%d', `q4` = '%d', `q5` = '%d', `q6` = '%d',`completed`='%d' WHERE (`formid` = '%d') and (`userid` = '%s');",
                    answer[0], answer[1], answer[2], answer[3], answer[4], answer[5],complete, formid, userid);
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }
    public ResultSet takeParam(Statement stmt,String userid){
        try {
            String sql=String.format("SELECT empathy,humor,positivity FROM utenti where userid='%s';",userid);
            return stmt.executeQuery(sql);
        } catch (SQLException throwables) {
            return null;
        }
    }
    public ResultSet takeParamForm(Statement stmt,String userid,int formid){
        try {
            String sql=String.format("SELECT emp,hum,pos FROM forms where userid='%s' AND formid='%d';",userid,formid);
            return stmt.executeQuery(sql);
        } catch (SQLException throwables) {
            return null;
        }
    }
    public ResultSet takeDate(Statement stmt,String userid,int formid){
        try {
            String sql=String.format("SELECT `by` FROM forms where userid='%s'AND formid=%d;",userid,formid);
            return stmt.executeQuery(sql);
        } catch (SQLException throwables) {
            return null;
        }
    }
    public Boolean setCalculated(Statement stmt,String userid,int formid){
        try {
            String sql=String.format(" UPDATE  forms  set `calculated`=1  WHERE (`formid` = %d) and (`userid` = '%s');", formid,userid);
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }
    public Boolean close(Statement stmt,String userid,int formid,int[] param){
        try {
            String sql=String.format(" UPDATE  forms  set `emp` = %d, `hum` = %d, `pos` = %d,`by`=CURRENT_TIMESTAMP  WHERE (`formid` = %d) and (`userid` = '%s');",
                    param[0],param[1],param[2],formid,userid);
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }
    public ResultSet newForm(Statement stmt,String userid){
        try {
            String sql=String.format("SELECT `formid`,`completed` FROM forms where userid='%s';",userid);
            return stmt.executeQuery(sql);
        } catch (SQLException throwables) {
            return null;
        }
    }

}
