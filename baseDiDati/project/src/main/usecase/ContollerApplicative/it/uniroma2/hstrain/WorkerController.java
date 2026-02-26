package it.uniroma2.hstrain;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class WorkerController implements ControllerApplicative {
    WorkerDao dao;
    AbsRide_WorkBean bean;
    @Override
    public void start() {
        try {
            ConnectionFactoryFromFile.changeRole(Role.LAVORATORE);
            this.dao = new WorkerDao();
            while (true) {
                int choice;
                choice =InitialWorkerView.showMenu();
                switch (choice) {
                    case 1 -> {
                        AddReportWorkView view = new AddReportWorkView();
                        bean = new AbsRide_WorkBean();
                        bean=view.fillBean();
                        dao.reportWorkTime(bean);
                        start();
                    }
                    case 2 -> {
                        ReadWeeklyWorkTimeView view = new ReadWeeklyWorkTimeView();
                        String worker =view.inputWorkTime();
                        List<AbsRide_WorkBean> beans = dao.readWorkTime(worker);
                        view.SeeWorkTime(beans);
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

