package it.uniroma2.hstrain;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class Main {
    public static void main(String[] args) {
        ApplicationController applicationController=new ApplicationController();
        applicationController.start();
    }
}