package it.uniroma2.hstrain;

public class ApplicationController implements ControllerApplicative {
    CredentialsBean cred;

    @Override
    public void start() {
        LoginController loginController = new LoginController();
        loginController.start();
        cred = loginController.getCred();

        if (cred.getRole() == null) {
            throw new RuntimeException("Invalid credentials");
        }

        switch (cred.getRole()) {
            case ACQUIRENTE -> new CostumerController().start();
            case LAVORATORE -> new WorkerController().start();
            case GESTORESERVIZIO -> new ServiceManagerController().start();
            default -> throw new RuntimeException("Invalid credentials");
        }
    }
}