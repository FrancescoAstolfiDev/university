package it.uniroma2.hstrain;

import java.sql.Date;
import java.sql.Time;

public class AbsRide_WorkBean {
    private String sPart;
    private String sArr;
    private Time startRide_Work;
    private Time endRide_Work;
    private Date day;
    private String train;
    private String worker;
    private String report;
    // Getter and Setter methods for sPart
    public String getsPart() {
        return sPart;
    }

    public void setsPart(String sPart) {
        this.sPart = sPart;
    }

    // Getter and Setter methods for sArr
    public String getsArr() {
        return sArr;
    }

    public void setsArr(String sArr) {
        this.sArr = sArr;
    }

    // Getter and Setter methods for startRide_Work
    public String getStartRide_Work() {
        return String.valueOf(startRide_Work);
    }

    public void setStartRide_Work(Time startRide_Work) {
        this.startRide_Work = startRide_Work;
    }

    // Getter and Setter methods for endRide_Work
    public String getEndRide_Work() {
        return String.valueOf(endRide_Work);
    }

    public void setEndRide_Work(Time endRide_Work) {
        this.endRide_Work = endRide_Work;
    }

    // Getter and Setter methods for day
    public String getDay() {
        return String.valueOf(day);
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (sPart != null) {
            sb.append("Start Part: ").append(sPart).append("\n");
        }
        if (sArr != null) {
            sb.append("End Part: ").append(sArr).append("\n");
        }
        if (startRide_Work != null) {
            sb.append("Start Ride (Work): ").append(startRide_Work).append("\n");
        }
        if (endRide_Work != null) {
            sb.append("End Ride (Work): ").append(endRide_Work).append("\n");
        }
        if (day != null) {
            sb.append("Day: ").append(day).append("\n");
        }
        if (train != null) {
            sb.append("Train: ").append(train).append("\n");
        }
        if (worker != null) {
            sb.append("Worker: ").append(worker).append("\n");
        }
        if (report != null) {
            sb.append("Report: ").append(report).append("\n");
        }

        return sb.toString();
    }

}
