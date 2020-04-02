package Models;

import java.io.Serializable;

public class Echipa implements Serializable,Entity<Integer> {
    private Integer id;
    private String nume;


    public Echipa(Integer id,String nume){
        this.id=id;
        this.nume=nume;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

}
