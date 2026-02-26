package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CreateWorkerView {
    public SignupBean createWorker() throws IOException {
        System.out.println("*********************************");
        System.out.println("*        INPUT WORKER           *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SignupBean signupBean = new SignupBean();

        System.out.print("Matriculation number , it will be the username of the worker: ");
        signupBean.setIdUsr(reader.readLine());

        while (signupBean.getPassword()==null || !signupBean.getPassword().equals(signupBean.getConfirmPassword())) {
            System.out.print("Password: ");
            signupBean.setPassword(reader.readLine());

            System.out.print("Confirm Password: ");
            signupBean.setConfirmPassword(reader.readLine());

            if (!signupBean.getPassword().equals(signupBean.getConfirmPassword())) {
                System.out.println("Passwords do not match. Please try again.");
            }
        }

        System.out.print("Name: ");
        signupBean.setName(reader.readLine());

        System.out.print("Surname: ");
        signupBean.setSurname(reader.readLine());

        System.out.print("Type of worker: ");
        signupBean.setCreditCard_Role(reader.readLine());

        return signupBean;
    }
}
