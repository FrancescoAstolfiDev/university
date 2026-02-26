package it.uniroma2.hstrain;

import java.sql.Date;
import java.sql.Time;
public class TicketBean {
        private String trainRide;
        private Date day;
        private String fiscalCode;
        private String stationStart;//sono o capolinea o inizio e fine fermate da mettere nel biglietto
        private String stationDest;//sono o capolinea o inizio e fine fermate da mettere nel biglietto
        private String stationStop;
        private String idTicket;
        private String passengerCar;
        private String train;
        private String seat;
        private Time startRide;
        private Time arriveRide;
        private String classCarPassengers;
        public TicketBean() {
        }
        public TicketBean(String train,String passengerCar,String seat,String stationStart,Time arriveRide,String stationDest,Time startRide,String stationStop) {
            this.stationStart = stationStart;
            this.stationDest = stationDest;
            this.stationStop = stationStop;
            this.passengerCar = passengerCar;
            this.train = train;
            this.seat = seat;
            this.startRide = startRide;
            this.arriveRide = arriveRide;
        }

        public void setIdTicket(String idTicket) {
            this.idTicket = idTicket;
        }
    public String getIdTicket() {
        return this.idTicket;
    }


    public String getTrainRide() {
            return trainRide;
        }

        public void setTrainRide(String trainRide) {
            this.trainRide = trainRide;
        }

        public String getFiscalCode() {
            return fiscalCode;
        }

        public void setFiscalCode(String fiscalCode) {
            this.fiscalCode = fiscalCode;
        }

        public String getDay() {
            return String.valueOf(day);
        }

        public void setDay(Date day) {
            this.day = day;
        }

        public String getStationStart() {
            return stationStart;
        }

        public void setStationStart(String stationStart) {
            this.stationStart = stationStart;
        }

        public String getStationDest() {
            return stationDest;
        }

        public void setStationDest(String stationDest) {
            this.stationDest = stationDest;
        }

        public String getStationStop() {
            return stationStop;
        }

        public void setStationStop(String stationStop) {
            this.stationStop = stationStop;
        }

        public String getPassengerCar() {
            return passengerCar;
        }

        public void setPassengerCar(String passengerCar) {
            this.passengerCar = passengerCar;
        }

        public String getTrain() {
            return train;
        }

        public void setTrain(String train) {
            this.train = train;
        }

        public String getSeat() {
            return seat;
        }

        public void setSeat(String seat) {
            this.seat = seat;
        }

        public Time getStartRide() {
            return startRide;
        }

        public void setStartRide(Time startRide) {
            this.startRide = startRide;
        }

        public Time getArriveRide() {
            return arriveRide;
        }

        public void setArriveRide(Time arriveRide) {
            this.arriveRide = arriveRide;
        }
    @Override
    public String toString() {
        String border = "+-----------------------+\n";
        String content = "Ticket Details:\n";

        if (trainRide != null) {
            content += "Train Ride: " + trainRide + "\n";
        }
        if (day != null) {
            content += "Day: " + day + "\n";
        }
        if (fiscalCode != null) {
            content += "Fiscal Code: " + fiscalCode + "\n";
        }
        if (stationStart != null) {
            content += "Start Station: " + stationStart + "\n";
        }
        if (stationDest != null) {
            content += "Destination Station: " + stationDest + "\n";
        }
        if (stationStop != null) {
            content += "Stop Station: " + stationStop + "\n";
        }
        if (idTicket != null) {
            content += "Ticket ID: " + idTicket + "\n";
        }
        if (passengerCar != null) {
            content += "Passenger Car: " + passengerCar + "\n";
        }
        if (train != null) {
            content += "Train: " + train + "\n";
        }
        if (seat != null) {
            content += "Seat: " + seat + "\n";
        }
        if (startRide != null) {
            content += "Start Ride Time: " + startRide + "\n";
        }
        if (arriveRide != null) {
            content += "Arrive Ride Time: " + arriveRide + "\n";
        }

        String bottomBorder = "+-----------------------+";

        return border + content + bottomBorder;
    }

    public String getClassCarPassengers() {
        return classCarPassengers;
    }

    public void setClassCarPassengers(String classCarPassengers) {
        this.classCarPassengers = classCarPassengers;
    }
}

