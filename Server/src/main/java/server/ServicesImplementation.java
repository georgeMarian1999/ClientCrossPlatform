package server;

import Models.*;
import Service.Service;
import Services.IObserver;
import Services.IServices;
import Services.ServerException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImplementation implements IServices {

    private Service service;
    private Iterable<Angajat> angajati;
    private Iterable<DTOBJCursa> cursedisp;
    private Map<String,IObserver> loggedEmployees;

    private final int noOfThreads=5;

    public ServicesImplementation(Service service1){
        service=service1;
        angajati=service1.findAllEmployees();
        cursedisp=service1.GroupByCapacitate();
        loggedEmployees=new ConcurrentHashMap<>();
    }


    public synchronized void login(DTOAngajat angajat, IObserver client) throws ServerException {
        boolean isEmployee=this.service.LocalLogin(angajat.getUsername(),angajat.getPassword());
        if(isEmployee){
            if(loggedEmployees.get(angajat.getUsername())!=null){
                throw new ServerException("User is already logged in");
            }
            loggedEmployees.put(angajat.getUsername(),client);
            notifyEmployeeLoggedIn(angajat);
        }else{
            System.out.println("Authentiocation failed");
            throw new ServerException("Wrong username or password");
        }

    }
    private void notifyEmployeeLoggedIn(DTOAngajat angajat) throws ServerException{
        Iterable<DTOAngajat> employees=this.service.findOthersEmployees(angajat);

        ExecutorService executor= Executors.newFixedThreadPool(noOfThreads);
        for(DTOAngajat ang : employees){
            IObserver client=loggedEmployees.get(ang.getUsername());
            if(client!=null){
            executor.execute(()->{
                try{
                    System.out.println("Notifying employee "+ang.getUsername()+" employee "+angajat.getUsername()+" logged in");
                    client.AngajatLoggedIn(angajat);
                }catch(ServerException e){
                    System.err.println("Error notifing employees "+e);
                }
            });
            }
        }
        executor.shutdown();
    }
    private void notifyEmployeeLoggedOut(DTOAngajat angajat) throws ServerException{
        Iterable<DTOAngajat> employees=this.service.findOthersEmployees(angajat);

        ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
        for(DTOAngajat ang : employees){
            IObserver client=loggedEmployees.get(ang.getUsername());
            if(client!=null){
            executor.execute(()->{
                try{
                    System.out.println("Notifying employee "+ang.getUsername()+" employee "+angajat.getUsername()+" logged out");
                    client.AngajatLoggedOut(angajat);
                }catch(ServerException e){
                    System.err.println("Error notifying employee logged out "+e);
                }
            });
            }
        }
        executor.shutdown();
    }
    private void notifyEmployeeSubmitted(DTOAngajat angajat) throws ServerException{
        System.out.println("S-a apelat notifyEmployeeSubmitted");
        Iterable<Angajat> employees=this.service.findAllEmployees();
        //for(DTOAngajat a : employees){
          //  System.out.println(a.getUsername());
        //}
        this.cursedisp=this.service.GroupByCapacitate();
        DTOBJCursa[] result=convert(this.cursedisp);
        ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
        for(Angajat ang : employees){

            IObserver client=loggedEmployees.get(ang.getUsername());
            System.out.println(client);
            if(client!=null){
            executor.execute(()->{
                try{
                    System.out.println("Notifiying employee "+ang.getUsername()+" employee "+angajat.getUsername()+" submitted");
                    client.AngajatSubmitted(result);
                    System.out.println("Employee "+ang.getUsername()+" notified");
                }catch (ServerException e){
                    System.err.println("Error notifying about submit");
                }
            });
            }else System.out.println("Error gettting logged in employees");
        }
        executor.shutdown();
    }
    public DTOBJCursa[] convert(Iterable<DTOBJCursa> source){
        ArrayList<DTOBJCursa> result=new ArrayList<>();
        for (DTOBJCursa c : source){
            result.add(c);
        }
        return result.toArray(new DTOBJCursa[result.size()]);
    }
    public synchronized void logout(DTOAngajat angajat, IObserver client) throws ServerException {
        IObserver localClient=loggedEmployees.remove(angajat.getUsername());
        if(localClient==null){
            throw new ServerException("User "+angajat.getUsername()+" is not logged in");
        }
        notifyEmployeeLoggedOut(angajat);

    }


    public synchronized void submitInscriere(DTOInfoSubmit infoSubmit) throws ServerException {
        System.out.println("Submitting by "+infoSubmit.getUserWho()+" ....");
        try{
            this.service.InscriereParticipant(infoSubmit.getCapacitate(),infoSubmit.getNumePart(),infoSubmit.getNumeEchipa());
            System.out.println("New submit saved in database");
            notifyEmployeeSubmitted(infoSubmit.getWho());
        }catch(ServerException e){
            throw new ServerException("Could not submit ..."+e);
        }
    }


    public synchronized Angajat[] getLoggedEmployees() throws ServerException {
        Iterable<Angajat> allEmployees=this.service.findAllEmployees();
        Set<Angajat> result=new TreeSet<>();
        for(Angajat ang : allEmployees){
            if(loggedEmployees.containsKey(ang.getUsername())){
                result.add(new Angajat(ang.getUsername()));
                System.out.println("+"+ang.getUsername());
            }
        }
        System.out.println("Size is "+result.size());
        return result.toArray(new Angajat[result.size()]);
    }


    public synchronized DTOBJCursa[] getCurseDisp() throws ServerException {
        ArrayList<DTOBJCursa> result=new ArrayList<>();
        for(DTOBJCursa c :this.cursedisp){
            result.add(c);
        }
        return result.toArray(new DTOBJCursa[result.size()]);
    }


    public synchronized DTOBJPartCapa[] searchByTeam(String team) throws ServerException {
        Iterable<DTOBJPartCapa> result=this.service.cautare(team);
        ArrayList<DTOBJPartCapa> ret=new ArrayList<>();
        for(DTOBJPartCapa part : result){
            ret.add(part);
        }

        return ret.toArray(new DTOBJPartCapa[ret.size()]);
    }
}
