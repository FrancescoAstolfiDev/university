package it.uniroma2.hstrain;

import java.util.Scanner;

public class InitialAcquirenteView {
    public static int showMenu() {
        System.out.println("*********************************");
        System.out.println("*      COSTUMER DASHBOARD       *");
        System.out.println("*********************************\n");
        System.out.println("*** What should I do for you? ***\n");
        System.out.println("1) Book a ticket");
        System.out.println("2) Your tickets in detail");
        System.out.println("3) Your tickets bought ");
        System.out.println("4) Quit ");
        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.print("Please enter your choice: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= 4) {
                break;
            }
            System.out.println("Invalid option");
        }

        return choice;
    }
}
