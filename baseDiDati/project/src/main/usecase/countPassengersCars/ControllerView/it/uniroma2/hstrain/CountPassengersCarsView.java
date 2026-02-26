package it.uniroma2.hstrain;

import java.util.List;

public class CountPassengersCarsView {
    public void SeePassengersCars(List<TrainBean> listBean ) {
        System.out.println("*********************************");
        System.out.println("*     YOUR PASSENGERS CARS      *");
        System.out.println("*********************************\n");
        for (TrainBean train : listBean) {
            System.out.println(train.toString());
        }
    }
}
