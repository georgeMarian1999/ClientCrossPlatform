package Service;

import Models.*;
import Repos.Repositories.*;

public class Service {
    private AngajatRepo angajati;
    private CursaRepo curse;
    private EchipaRepo echipe;
    private ParticipantRepo participanti;
    private InscriereRepo inscrieri;

    public Service(AngajatRepo ang, CursaRepo cursa, EchipaRepo echipa, ParticipantRepo part, InscriereRepo insc){
        this.angajati=ang;
        this.curse=cursa;
        this.echipe=echipa;
        this.participanti=part;
        this.inscrieri=insc;
    }
    public Iterable<Angajat> findAllEmployees(){
        return this.angajati.findAll();
    }
    public void AddAngajat(Angajat angajat){
        this.angajati.save(angajat);
    }
    public void AddCursa(Cursa cursa){
        this.curse.save(cursa);
    }
    public void AddEchipa(Echipa echipa){
        this.echipe.save(echipa);
    }
    public void AddParticipant(Participant participant){
        this.participanti.save(participant);
    }
    public void AddInscriere(Inscriere inscriere){
        this.inscrieri.save(inscriere);
    }
    public void DeleteAngajat(int id){
        this.angajati.delete(id);
    }
    public void DeleteCursa(int id){
        this.curse.delete(id);
    }
    public void DeleteEchipa(int id){
        this.echipe.delete(id);
    }
    public void DeleteParticipant(int id){
        this.participanti.delete(id);
    }
    public int AngajatiSize(){
        return this.angajati.size();
    }
    public int CurseSize(){
        return this.curse.size();
    }
    public int EchipeSize(){
        return this.echipe.size();
    }
    public int ParticipantiSize(){
        return this.participanti.size();
    }
    public void updateAngajat(Angajat angajatvechi, Angajat angajatnou){
        this.angajati.update(angajatvechi.getId(),angajatnou);
    }
    public Iterable<DTOAngajat> findOthersEmployees(DTOAngajat angajat){
        return this.angajati.findOtherEmployees(angajat);
    }
    public void updateCursa(Cursa cursaveche, Cursa cursanoua){
        this.curse.update(cursaveche.getId(),cursanoua);
    }
    public void updateEchipa(Echipa echipaveche, Echipa echipanoua){
        this.echipe.update(echipaveche.getId(),echipanoua);
    }
    public void updateParticipant(Participant participantvechi, Participant participantnou){
        this.participanti.update(participantvechi.getId(),participantnou);
    }
    public boolean LocalLogin(String username,String password){
        return this.angajati.LocalLogin(username,password);
    }
    public Iterable<DTOBJPartCapa> cautare(String numeEchipa){
        return this.echipe.cautare(numeEchipa);
    }
    public Iterable<DTOBJCursa> GroupByCapacitate(){
        return this.curse.GroupByCapacitate();
    }
    public void InscriereParticipant(int capacitate,String numeParticipant,String numeEchipa){
        int idCursa,idEchipa,idParticipant,idInscriere;
        idParticipant=this.participanti.findMaxId()+1;
        idEchipa=this.echipe.FindidByName(numeEchipa);
        idCursa=this.curse.findIdByCapacitate(capacitate);
        idInscriere=this.inscrieri.findMaxId()+1;
        Participant Nou=new Participant(idParticipant,numeParticipant,idEchipa);
        this.AddParticipant(Nou);
        Inscriere Noua=new Inscriere(idInscriere,idParticipant,idCursa);
        this.AddInscriere(Noua);
    }


}
