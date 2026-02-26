package it.uniroma2.hstrain;

import java.util.Scanner;

public class InitialWorkerView {
    public static int showMenu() {
        System.out.println("*********************************");
        System.out.println("*       WORKER DASHBOARD        *");
        System.out.println("*********************************\n");
        System.out.println("*** What should I do for you? ***\n");
        System.out.println("1) Report work Time");
        System.out.println("2) See weekly work Time");
        System.out.println("3) Quit ");

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.print("Please enter your choice: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= 3) {
                break;
            }
            System.out.println("Invalid option");
        }

        return choice;
    }
}
