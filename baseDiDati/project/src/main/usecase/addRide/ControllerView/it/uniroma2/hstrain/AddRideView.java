package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class AddRideView {
    public AbsRide_WorkBean addRide() throws IOException {
        System.out.println("*********************************");
        System.out.println("*         INPUT RIDE            *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        AbsRide_WorkBean bean = new AbsRide_WorkBean();

        System.out.print("Enter station start: ");
        bean.setsPart(reader.readLine()); // Set the station start

        System.out.print("Enter station destination: ");
        bean.setsArr(reader.readLine()); // Set the station end

        System.out.print("Enter start ride time (HH:mm:ss): ");
        String startRideInput = reader.readLine();
        bean.setStartRide_Work(Time.valueOf(startRideInput)); // Set the start ride time

        System.out.print("Enter end ride time (HH:mm:ss): ");
        String endRideInput = reader.readLine();
        bean.setEndRide_Work(Time.valueOf(endRideInput)); // Set the end ride time

        return bean; // Return the created AbsRide_WorkBean instance
    }

}
