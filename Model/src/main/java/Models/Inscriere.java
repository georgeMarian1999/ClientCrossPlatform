package Models;

import java.io.Serializable;

public class Inscriere implements Serializable,Entity<Integer> {
    private Integer idInscriere;
    private Integer idParticipant;
    private Integer idCursa;

    public Inscriere(Integer idInscriere,Integer idParticipant,Integer idCursa){
        this.idInscriere=idInscriere;
        this.idParticipant=idParticipant;
        this.idCursa=idCursa;
    }

    @Override
    public Integer getId() {
        return this.idInscriere;
    }

    @Override
    public void setID(Integer integer) {
        this.idInscriere=integer;
    }

    public Integer getIdParticipant() {
        return idParticipant;
    }

    public Integer getIdCursa() {
        return idCursa;
    }

    public void setIdParticipant(Integer idParticipant) {
        this.idParticipant = idParticipant;
    }

    public void setIdCursa(Integer idCursa) {
        this.idCursa = idCursa;
    }
}
