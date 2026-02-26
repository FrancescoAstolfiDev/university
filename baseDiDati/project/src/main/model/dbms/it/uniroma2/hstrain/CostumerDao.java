package it.uniroma2.hstrain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CostumerDao {
    public void bookTicket(TicketBean bean) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call prenotaBiglietto(?,?,?,?,?,?)}");
        cs.setString(1, bean.getTrainRide());//CORSA
        cs.setString(2, bean.getDay());//GIORNO
        cs.setString(3, bean.getFiscalCode());//ACQUIRENTE
        cs.setString(5, bean.getStationStart());//STAZIONE PARTENZA
        cs.setString(6, bean.getStationDest());//STAZIONE ARRIVO
        cs.setString(4,bean.getClassCarPassengers());
        cs.executeQuery();
    }
    public List<TicketBean> ticketDetails(TicketBean bean) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        List<TicketBean> list = new ArrayList<>();
        CallableStatement cs = conn.prepareCall("{call dettagli_biglietto(?,?,?)}");
        cs.setString(1, bean.getIdTicket());//BIGLIETTO
        cs.setString(2, bean.getTrainRide());//CORSAd
        cs.setString(3, bean.getDay());//GIORNO
        boolean status = cs.execute();
        if(status){
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                bean=new TicketBean(rs.getString("Treno"),rs.getString("Vagone"),rs.getString("Posto"),rs.getString("Parte"),rs.getTime("Arrivo"),rs.getString("Arriva"),rs.getTime("Partenza"),rs.getString("Stazione"));
                list.add(bean);
            }

        }
        return list;

    }
    public List<TicketBean>  ticketBought(String fiscalCode) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        List<TicketBean> list = new ArrayList<>();
        TicketBean bean;
        CallableStatement cs = conn.prepareCall("{call visualizza_BigliettiAcq(?)}");
        cs.setString(1, fiscalCode);//CODICE FISCALE
        boolean status = cs.execute();
        if(status){
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                bean=new TicketBean();
                bean.setIdTicket(rs.getString("CodicePrenotazioneUnivoco"));
                bean.setTrainRide(rs.getString("Corsa"));
                bean.setDay(rs.getDate("Giorno"));
                bean.setStationStart(rs.getString("Partenza"));
                bean.setStationDest(rs.getString("Arrivo"));
                list.add(bean);
            }

        }
        return list;
    }
}
