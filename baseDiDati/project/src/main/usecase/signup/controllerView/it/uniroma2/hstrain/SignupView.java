package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

public class SignupView {
    public SignupBean signupFun() throws IOException {
        System.out.println("*********************************");
        System.out.println("*       SIGNUP DASHBOARD        *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SignupBean signupBean = new SignupBean();

        System.out.print("Fiscal Code hstrain, will be your username: ");
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

        System.out.print("Date of Birth (yyyy-mm-dd): ");
        String dateOfBirth = reader.readLine();
        signupBean.setDateBorn(Date.valueOf(dateOfBirth));

        System.out.print("Credit Card Number: ");
        signupBean.setCreditCard_Role(reader.readLine());

        // You can add more fields here as needed

        // Print the filled it.uniroma2.hstrain.SignupBean for verification
        System.out.println("it.uniroma2.hstrain.SignupBean filled with data:");
        System.out.println(signupBean.toString());
        return signupBean;
        // Return an appropriate value according to your logic
    }
}