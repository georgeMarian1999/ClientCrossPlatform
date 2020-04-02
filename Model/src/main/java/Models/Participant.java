package Models;


import java.io.Serializable;

public class Participant implements Serializable,Entity<Integer> {
    private Integer id;
    private String nume;
    private int idEchipa;

    public Participant(Integer ID,String name,Integer idEchipa){
        this.id=ID;
        this.nume=name;
        this.idEchipa=idEchipa;
    }


    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getIdEchipa() {
        return idEchipa;
    }

    public void setIdEchipa(Integer idEchipa) {
        this.idEchipa = idEchipa;
    }
}
