package Models;

import java.io.Serializable;

public class DTOBJCursa implements Serializable,Entity<Integer> {
    private int id;
    private int capacitate;
    private int Nrinscrisi;

    public DTOBJCursa(int idcursa,int cap,int nr){
        this.id=idcursa;
        this.capacitate=cap;
        this.Nrinscrisi=nr;
    }
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }

    public int getNrinscrisi() {
        return Nrinscrisi;
    }

    public void setNrinscrisi(int nrinscrisi) {
        Nrinscrisi = nrinscrisi;
    }
}
