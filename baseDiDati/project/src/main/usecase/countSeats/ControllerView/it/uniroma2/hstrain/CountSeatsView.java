package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CountSeatsView {
    public TrainBean CountSeats() throws IOException {
        System.out.println("*********************************");
        System.out.println("*        INPUT TRAIN           *");
        System.out.println("*********************************\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TrainBean bean = new TrainBean();

        System.out.print("Input the train: ");
        bean.setMatricola(reader.readLine());

        return bean;
    }
    public void SeeSeatsTrain(TrainBean bean ) {
        System.out.println("*********************************");
        System.out.println("*         YOUR SEATS            *");
        System.out.println("*********************************\n");
        System.out.println(bean);
    }
}
