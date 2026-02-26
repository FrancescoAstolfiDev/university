package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.List;

public class TicketDetailsView {
    public TicketBean BookTicketFun() throws IOException {
        System.out.println("*********************************");
        System.out.println("*         SEARCH TICKET           *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TicketBean bean = new TicketBean();

        System.out.print("Input the id ticket: ");
        bean.setIdTicket(reader.readLine());

        System.out.print("Input the train ride: ");
        bean.setTrainRide(reader.readLine());

        System.out.print("Input the day (yyyy-MM-dd): ");
        bean.setDay(Date.valueOf(reader.readLine()));
        return bean;
    }
    public void SeeTicketDetails(List<TicketBean> listBean ) throws IOException {
        System.out.println("*********************************");
        System.out.println("*         YOUR TICKET            *");
        System.out.println("*********************************\n");
        for (TicketBean ticket : listBean) {
            System.out.println(ticket.toString());
        }
    }
}
