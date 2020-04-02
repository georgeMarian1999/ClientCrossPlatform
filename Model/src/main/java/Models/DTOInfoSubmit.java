package Models;

import java.io.Serializable;

public class DTOInfoSubmit implements Serializable {
    private DTOAngajat who;
    private int capacitate;
    private String NumePart;
    private String NumeEchipa;

    public DTOInfoSubmit(DTOAngajat angajat,int capacitate1,String numeparticipant,String numeechipa) {
        this.who=angajat;
        this.capacitate = capacitate1;
        this.NumePart = numeparticipant;
        this.NumeEchipa = numeechipa;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public String getNumePart() {
        return NumePart;
    }

    public String getNumeEchipa() {
        return NumeEchipa;
    }

    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }

    public void setNumePart(String numePart) {
        NumePart = numePart;
    }

    public void setNumeEchipa(String numeEchipa) {
        NumeEchipa = numeEchipa;
    }

    public DTOAngajat getWho() {
        return who;
    }
    public String getUserWho(){
        return this.who.getUsername();
    }
    public void setWho(DTOAngajat who) {
        this.who = who;
    }

    @Override
    public String toString() {
        return "DTOInfoSubmit{" +
                "capacitate=" + capacitate +
                ", NumePart='" + NumePart + '\'' +
                ", NumeEchipa='" + NumeEchipa + '\'' +
                '}';
    }
}
