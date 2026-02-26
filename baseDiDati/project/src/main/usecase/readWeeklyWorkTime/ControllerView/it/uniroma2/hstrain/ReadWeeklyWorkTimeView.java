package it.uniroma2.hstrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.List;

public class ReadWeeklyWorkTimeView {

    public String inputWorkTime() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String ret;
        System.out.println("*********************************");
        System.out.println("*    SEARCH WEEKLY WORK TIME    *");
        System.out.println("*********************************\n");

        System.out.print("Matriculation number : ");
        ret=reader.readLine();



        return ret;

    }
    public void SeeWorkTime(List<AbsRide_WorkBean> listBean )  {
        System.out.println("*********************************");
        System.out.println("*      YOUR WORK TIME          *");
        System.out.println("*********************************\n");
        for (AbsRide_WorkBean bean : listBean) {
            System.out.println(bean.toString());
        }
    }
}
