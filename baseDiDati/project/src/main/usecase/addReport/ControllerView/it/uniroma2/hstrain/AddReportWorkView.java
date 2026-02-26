package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;

public class AddReportWorkView {


    public AbsRide_WorkBean fillBean() throws IOException {
        System.out.println("*********************************");
        System.out.println("*           ADD REPORT          *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        AbsRide_WorkBean bean = new AbsRide_WorkBean();

        System.out.print("Enter worker: ");
        bean.setWorker(reader.readLine()); // Set the worker

        System.out.print("Enter day (yyyy-MM-dd): ");
        String dayInput = reader.readLine();
        bean.setDay(Date.valueOf(dayInput)); // Set the day

        System.out.print("Enter train: ");
        bean.setTrain(reader.readLine()); // Set the train

        System.out.print("Enter start work time (HH:mm:ss): ");
        String startRideInput = reader.readLine();
        bean.setStartRide_Work(Time.valueOf(startRideInput)); // Set the start ride time


        System.out.print("Enter report: ");
        bean.setReport(reader.readLine()); // Set the report



        return bean; // Return the created AbsRide_WorkBean instance
    }
}
