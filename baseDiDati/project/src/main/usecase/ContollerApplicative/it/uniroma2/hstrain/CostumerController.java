package it.uniroma2.hstrain;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CostumerController implements ControllerApplicative{
    CostumerDao dao;
    TicketBean bean;
    List<TicketBean> beans;

    @Override
    public void start() {
        try {
            ConnectionFactoryFromFile.changeRole(Role.ACQUIRENTE);
            this.dao = new CostumerDao();
            while (true) {
                int choice;
                choice = InitialAcquirenteView.showMenu();
                switch (choice) {
                    case 1 -> {
                        BookTicketView view = new BookTicketView();
                        bean = view.BookTicketFun();
                        dao.bookTicket(bean);
                        beans = dao.ticketBought(bean.getFiscalCode());
                        TicketBoughtView viewTickets = new TicketBoughtView();
                        viewTickets.SeeTicketDetails(beans);
                        this.start();
                    }
                    case 2 -> {
                        TicketDetailsView view = new TicketDetailsView();
                        bean = view.BookTicketFun();
                        beans = dao.ticketDetails(bean);
                        view.SeeTicketDetails(beans);
                        this.start();
                    }
                    case 3 -> {
                        TicketBoughtView view = new TicketBoughtView();
                        bean = view.GetFiscalCodeFun();
                        beans = dao.ticketBought(bean.getFiscalCode());
                        TicketBoughtView viewTickets = new TicketBoughtView();
                        viewTickets.SeeTicketDetails(beans);
                        this.start();
                    }
                    default -> {
                        ConnectionFactoryFromFile.changeRole(Role.LOGIN);
                        ApplicationController controller = new ApplicationController();
                        controller.start();
                        return;
                    }
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
