package Models;

import java.io.Serializable;

public class DTOInfoSubmit implements Serializable {
    private int capacitate;
    private String NumePart;
    private String NumeEchipa;

    public DTOInfoSubmit(int capacitate1,String numeparticipant,String numeechipa) {
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


    @Override
    public String toString() {
        return "DTOInfoSubmit{" +
                "capacitate=" + capacitate +
                ", NumePart='" + NumePart + '\'' +
                ", NumeEchipa='" + NumeEchipa + '\'' +
                '}';
    }
}
