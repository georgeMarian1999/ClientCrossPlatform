package Models;

import java.io.Serializable;

public class DTOBJPartCapa implements Serializable,Entity<Integer> {
    private Integer id;
    private String nume;
    private int capactiate;

    public DTOBJPartCapa(Integer ID,String Nume,int cap){
        this.id=ID;
        this.nume=Nume;
        this.capactiate=cap;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getCapactiate() {
        return capactiate;
    }

    public void setCapactiate(int capactiate) {
        this.capactiate = capactiate;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }
}
