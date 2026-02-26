package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TicketBoughtView {
    public void SeeTicketDetails(List<TicketBean> listBean ) throws IOException {
        System.out.println("*********************************");
        System.out.println("*         YOUR TICKETS          *");
        System.out.println("*********************************\n");
        for (TicketBean ticket : listBean) {
            System.out.println(ticket.toString());
        }
       return;
    }

    public TicketBean GetFiscalCodeFun() throws IOException {
        System.out.println("*********************************");
        System.out.println("*        INPUT FISCALCODE       *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TicketBean ticketBean = new TicketBean(); // Create a new TicketBean instance

        System.out.print("Enter Fiscal Code: ");
        ticketBean.setFiscalCode(reader.readLine()); // Set the fiscal code in the TicketBean

        return ticketBean; // Return the created TicketBean instance
    }
}
