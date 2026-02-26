package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CreateUserView {
    public SignupBean createUser() throws IOException {
        System.out.println("*********************************");
        System.out.println("*        INPUT USER             *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SignupBean signupBean = new SignupBean();

        System.out.print(" It will be the username for the user created: ");
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

        System.out.print("Type of user: ");
        signupBean.setCreditCard_Role(reader.readLine());

        return signupBean;
    }
}
