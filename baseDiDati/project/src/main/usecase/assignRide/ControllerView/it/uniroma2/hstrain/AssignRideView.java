package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;

public class AssignRideView {
    public AbsRide_WorkBean assignRide() throws IOException {
        System.out.println("*********************************");
        System.out.println("*     INPUT RIDE AND TRAIN      *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        AbsRide_WorkBean bean = new AbsRide_WorkBean();

        System.out.print("Enter day (yyyy-MM-dd): ");
        String dayInput = reader.readLine();
        bean.setDay(Date.valueOf(dayInput)); // Set the day

        System.out.print("Enter train: ");
        bean.setTrain(reader.readLine()); // Set the train

        System.out.print("Enter station start: ");
        bean.setsPart(reader.readLine()); // Set the station start

        System.out.print("Enter station destination: ");
        bean.setsArr(reader.readLine()); // Set the station end

        System.out.print("Enter start ride time (HH:mm:ss): ");
        String startRideInput = reader.readLine();
        bean.setStartRide_Work(Time.valueOf(startRideInput)); // Set the start ride time

        return bean; // Return the created AbsRide_WorkBean instance

    }
}
