package it.uniroma2.hstrain;


import java.util.ArrayList;
import java.util.List;

public class TrainBean {
    private String matricola;
    private int passengersCars_1C;
    private int passengersCars_2C;
    private List<PassengersCarBean> passengersCars;

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public int getPassengersCars_1C() {
        return passengersCars_1C;
    }

    public void setPassengersCars_1C(int passengersCars_1C) {
        this.passengersCars_1C = passengersCars_1C;
    }

    public int getPassengersCars_2C() {
        return passengersCars_2C;
    }

    public void setPassengersCars_2C(int passengersCars_2C) {
        this.passengersCars_2C = passengersCars_2C;
    }


    public List<PassengersCarBean> getPassengersCars() {
        return passengersCars;
    }
    
    public void setPassengersCars(List<PassengersCarBean> vagoni) {
        this.passengersCars = vagoni;
    }
    public void addPassengersCars(PassengersCarBean vagone){
        if(this.passengersCars==null ){
            this.passengersCars=new ArrayList<>();
        }
        this.passengersCars.add(vagone);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Train Matricola: ").append(matricola).append("\n");
        sb.append("Passenger Cars (1st Class): ").append(passengersCars_1C).append("\n");
        sb.append("Passenger Cars (2nd Class): ").append(passengersCars_2C).append("\n");

        if (passengersCars != null && !passengersCars.isEmpty()) {
            sb.append("Passenger Car Details:\n");
            for (PassengersCarBean car : passengersCars) {
                sb.append("ID: ").append(car.getIdRef()).append(", Seats: ").append(car.getSeats())
                        .append(", Class: ").append(car.getClassPassengersCar()).append("\n");
            }
        }

        return sb.toString();
    }

}
