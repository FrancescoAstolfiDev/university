package it.uniroma2.hstrain;

import java.sql.Date;

public class SignupBean {
    private String idUsr;
    private String password;
    private String confirmPassword;
    private String nome;
    private String cognome;
    private Date dateBorn;
    private String creditCard_Role;

    // Getter and setter for idUsr
    public String getIdUsr() {
        return idUsr;
    }

    public void setIdUsr(String idUsr) {
        this.idUsr = idUsr;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and setter for confirmPassword
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    // Getter and setter for nome
    public String getNome() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    // Getter and setter for cognome
    public String getCognome() {
        return cognome;
    }

    public void setSurname(String cognome) {
        this.cognome = cognome;
    }

    // Getter and setter for dateBorn
    public String getDateBorn() {
        return String.valueOf(dateBorn);
    }

    public void setDateBorn(Date dateBorn) {
        this.dateBorn = dateBorn;
    }

    // Getter and setter for creditCard_Role
    public String getCreditCard_Role() {
        return creditCard_Role;
    }

    public void setCreditCard_Role(String creditCard_Role) {
        this.creditCard_Role = creditCard_Role;
    }
}
