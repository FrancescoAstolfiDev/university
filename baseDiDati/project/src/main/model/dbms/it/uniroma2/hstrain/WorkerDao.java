package it.uniroma2.hstrain;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkerDao {
    public void reportWorkTime(AbsRide_WorkBean bean ) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call reportLavoro(?,?,?,?,?)}");
        cs.setString(1, bean.getWorker());
        cs.setString(2, bean.getDay());
        cs.setString(3, bean.getTrain());
        cs.setString(4, bean.getStartRide_Work());
        cs.setString(5, bean.getReport());
        cs.executeQuery();
    }
    public List<AbsRide_WorkBean> readWorkTime(String worker) throws SQLException {
        List<AbsRide_WorkBean> list=new ArrayList<>();
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call visualizza_OrarioSett(?)}");
        cs.setString(1, worker);
        boolean status= cs.execute();
        if(status){
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                AbsRide_WorkBean beanInn=new AbsRide_WorkBean();
                beanInn.setWorker(rs.getString("Lavoratore"));
                beanInn.setTrain(rs.getString("Treno"));
                beanInn.setStartRide_Work(rs.getTime("OrarioInizio"));
                beanInn.setEndRide_Work(rs.getTime("OrarioFine"));
                beanInn.setDay(rs.getDate("OrarioInizio"));
                System.out.println(beanInn.toString());
                list.add(beanInn);
            }
        }
        return list;
    }
}
