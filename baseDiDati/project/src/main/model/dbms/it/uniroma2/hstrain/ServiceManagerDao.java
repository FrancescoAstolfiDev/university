package it.uniroma2.hstrain;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceManagerDao {
    public void addAbsRide(AbsRide_WorkBean bean) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call aggiungi_tratta(?,?,?,?)}");
        cs.setString(1, bean.getsPart());
        cs.setString(2, bean.getsArr());
        cs.setString(3, bean.getStartRide_Work());
        cs.setString(4, bean.getEndRide_Work());
        cs.executeQuery();
    }
    public void workerAssignment (AbsRide_WorkBean bean) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call assegna_lavoratore(?,?,?,?,?)}");
        cs.setString(1, bean.getWorker());
        cs.setString(2, bean.getTrain());
        cs.setString(3, bean.getDay());
        cs.setString(4, bean.getStartRide_Work());
        cs.setString(5,bean.getEndRide_Work());
        cs.executeQuery();
    }
    public void assignRide(AbsRide_WorkBean bean) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call assegna_tratta(?,?,?,?,?)}");
        cs.setString(1, bean.getDay());
        cs.setString(2, bean.getTrain());
        cs.setString(3, bean.getsPart());
        cs.setString(4, bean.getsArr());
        cs.setString(5,bean.getStartRide_Work());
        cs.executeQuery();
    }
   public TrainBean countSeats(TrainBean bean) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call conta_posti(?)}");
        cs.setString(1, bean.getMatricola());
        boolean status = cs.execute();
        if(status){
           ResultSet rs = cs.getResultSet();
           while(rs.next()){
               PassengersCarBean beanP = new PassengersCarBean();
               beanP.setIdRef(rs.getString("Vagone"));
               beanP.setClassPassengersCar(rs.getString("ClasseApp"));
               beanP.setSeats(rs.getInt("Posti"));
               bean.addPassengersCars(beanP);
           }
       }
        return bean;
    }
    public List<TrainBean> countPassengersCars() throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        List<TrainBean> list=new ArrayList<>();
        CallableStatement cs = conn.prepareCall("{call conta_vagoni()}");
        boolean status = cs.execute();
        if(status){
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                TrainBean bean=new TrainBean();
                bean.setMatricola(rs.getString("Treno"));
                bean.setPassengersCars_1C(rs.getInt("Vagoni_1C"));
                bean.setPassengersCars_2C(rs.getInt("Vagoni_2C"));
                list.add(bean);
            }
        }
        return list ;
    }
    public void createWorkerUser(SignupBean bean) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call crea_lavoratore(?,?,?,?,?)}");
        cs.setString(1, bean.getPassword());
        cs.setString(2, bean.getIdUsr());
        cs.setString(3, bean.getNome());
        cs.setString(4, bean.getCognome());
        cs.setString(5, bean.getCreditCard_Role());
        cs.executeQuery();
    }
    public void createUser(SignupBean bean ) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call crea_utente(?,?,?)}");
        cs.setString(2, bean.getPassword());
        cs.setString(1, bean.getIdUsr());
        cs.setString(3, bean.getCreditCard_Role());
        cs.executeQuery();
    }

}
