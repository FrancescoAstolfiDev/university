package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class LoginView {

    public static int sigupLogin ()throws IOException{
        System.out.println("1)Signup for Free ");
        System.out.println("2)Login");

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.print("Please enter your choice: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= 2) {
                break;
            }
            System.out.println("Invalid option");
        }

        return choice;
    }

    public static CredentialsBean authenticate() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("username: ");
        String username = reader.readLine();
        System.out.print("password: ");
        String password = reader.readLine();

        return new CredentialsBean(username, password, null);
    }
}
