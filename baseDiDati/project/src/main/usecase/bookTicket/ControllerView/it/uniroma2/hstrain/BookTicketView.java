package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

public class BookTicketView {
    public TicketBean BookTicketFun() throws IOException {
        System.out.println("*********************************");
        System.out.println("*    BOOK TICKET DASHBOARD    *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TicketBean bean = new TicketBean();

        System.out.print("Input the train ride: ");
        bean.setTrainRide(reader.readLine());

        System.out.print("Class of the car passengers Seconda or Prima:  ");
        bean.setClassCarPassengers(reader.readLine());

        System.out.print("Input the day (yyyy-MM-dd): ");
        bean.setDay(Date.valueOf(reader.readLine()));

        System.out.print("Input the fiscal code: ");
        bean.setFiscalCode(reader.readLine());

        System.out.print("Input the station start: ");
        bean.setStationStart(reader.readLine());

        System.out.print("Input the station destination: ");
        bean.setStationDest(reader.readLine());

        return bean;
    }
}
