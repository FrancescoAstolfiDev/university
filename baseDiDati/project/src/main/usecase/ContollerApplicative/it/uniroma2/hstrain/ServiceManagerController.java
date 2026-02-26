package it.uniroma2.hstrain;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ServiceManagerController implements ControllerApplicative {
    ServiceManagerDao dao;
    AbsRide_WorkBean bean;


    @Override
    public void start() {
        try {
            ConnectionFactoryFromFile.changeRole(Role.GESTORESERVIZIO);
            this.dao = new ServiceManagerDao();
            while (true) {
                int choice;
                choice = InitialServiceManagerView.showMenu();
                switch (choice) {
                    case 1 -> {
                        AddRideView view = new AddRideView();
                        bean = view.addRide();
                        dao.addAbsRide(bean);
                        start();
                    }
                    case 2 -> {
                        WorkerAsignmentView view=new WorkerAsignmentView();
                        bean=view.assignWork();
                        dao.workerAssignment(bean);
                        start();
                    }
                    case 3 -> {
                        AssignRideView view = new AssignRideView();
                        bean = view.assignRide();
                        dao.assignRide(bean);
                        start();
                    }
                    case 4 -> {
                        TrainBean tBean;
                        CountSeatsView view = new CountSeatsView();
                        tBean = view.CountSeats();
                        tBean= dao.countSeats(tBean);
                        view.SeeSeatsTrain(tBean);
                        start();
                    }
                    case 5 -> {
                        List<TrainBean> tBeans;
                        CountPassengersCarsView view = new CountPassengersCarsView();
                        tBeans=dao.countPassengersCars();
                        view.SeePassengersCars(tBeans);
                        start();
                    }
                    case 6 -> {
                        CreateWorkerView view = new CreateWorkerView();
                        SignupBean sBean;
                        sBean=view.createWorker();
                        dao.createWorkerUser(sBean);
                        this.start();
                    }
                    case 7 -> {
                        CreateUserView view = new CreateUserView();
                        SignupBean sBean;
                        sBean= view.createUser();
                        dao.createUser(sBean);
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