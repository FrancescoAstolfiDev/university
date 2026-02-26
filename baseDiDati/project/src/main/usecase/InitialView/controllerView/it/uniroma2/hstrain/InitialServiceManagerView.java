package it.uniroma2.hstrain;

import java.util.Scanner;

public class InitialServiceManagerView {
    public static int showMenu() {
        System.out.println("*********************************");
        System.out.println("*   SERVICE MANAGER DASHBOARD   *");
        System.out.println("*********************************\n");
        System.out.println("*** What should I do for you? ***\n");
        System.out.println("1) Add a ride");
        System.out.println("2) Assign a worker to a train");
        System.out.println("3) Assign a ride to a train ");
        System.out.println("4) Count Seats in to a Passengers Car of a train");
        System.out.println("5) Count passenger Car of all trains");
        System.out.println("6) Create Worker");
        System.out.println("7) Create User");
        System.out.println("8) Quit ");

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.print("Please enter your choice: ");
            choice = input.nextInt();
            if (choice >= 1 && choice <= 8) {
                break;
            }
            System.out.println("Invalid option");
        }

        return choice;
    }
}
