package Network.RPCProtocol;

import Models.*;
import Services.IObserver;
import Services.IServices;
import Services.ServerException;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientServicesProxy implements IServices {

    private String host;
    private int port;

    private IObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<Protocol.Response> responses;
    private volatile boolean finished=true;

    public ClientServicesProxy(String host,int port){
        this.host=host;
        this.port=port;
        responses=new LinkedBlockingQueue<Protocol.Response>();
    }

    @Override
    public void login(DTOAngajat angajat, IObserver client) throws ServerException {
        initializeconnection();
        Protocol.Request request = Master.CreateLoginRequest(angajat);
        sendRequest(request);
        Protocol.Response response=readResponse();
        if(response.getType()==Protocol.Response.Type.OK){
            this.client=client;
            return;
        }
        if(response.getType()==Protocol.Response.Type.ERROR){
            String err=Master.getError(response);
            closeConnection();
            throw new ServerException("Eroare raspuns login "+err);
        }
    }

    @Override
    public void logout(DTOAngajat angajat, IObserver client) throws ServerException {
        Protocol.Request request=Master.CreateLogoutRequest(angajat);
        sendRequest(request);
        Protocol.Response response=readResponse();
        closeConnection();
        if(response.getType()==Protocol.Response.Type.ERROR){
            String err=Master.getError(response);
            throw new ServerException("Eroare raspuns logout "+err);
        }
    }

    @Override
    public void submitInscriere(DTOInfoSubmit infoSubmit) throws ServerException {
        System.out.println("Se apeleaza submitInscriere din ClientServicesProxy");
        Protocol.Request request=Master.CreateSubmitInscRequest(infoSubmit);
        System.out.println("Sending submit request "+request);
        sendRequest(request);
        System.out.println("Awaiting response");
        Protocol.Response response=readResponse();
        System.out.println("Response received"+response);
        if(response.getType()==Protocol.Response.Type.OK){
            System.out.println("Succesfully submitted");
        }
        if(response.getType()==Protocol.Response.Type.ERROR){
            String err=Master.getError(response);
            throw new ServerException("Eroare raspuns submit "+err);
        }
    }


    @Override
    public DTOBJCursa[] getCurseDisp() throws ServerException {
        Protocol.Request request = Master.CreateGetCurrentCurseRequest();
        sendRequest(request);
        Protocol.Response response=readResponse();
        if(response.getType()==Protocol.Response.Type.ERROR){
            String err=Master.getError(response);
            throw new ServerException("Error getting all Cursa"+err);
        }

        return Master.getCurse(response);
    }

    @Override
    public DTOBJPartCapa[] searchByTeam(String team) throws ServerException {
        Protocol.Request request=Master.CreateSearchByTeamRequest(team);
        sendRequest(request);
        Protocol.Response response=readResponse();
        if(response.getType()== Protocol.Response.Type.ERROR){
            String err=Master.getError(response);
            throw new ServerException("Error searching by team"+err);
        }
        return Master.getSearchResult(response);
    }

    @Override
    public String[] getTeams() throws ServerException {
        Protocol.Request request=Master.CreateGetAllTeamsRequest();
        sendRequest(request);
        Protocol.Response response=readResponse();
        if(response.getType()== Protocol.Response.Type.ERROR){
            String err=Master.getError(response);
            throw  new ServerException("Error getting all teams"+err);
        }
        return Master.getAllTeams(response);

    }

    private Protocol.Response readResponse() throws ServerException{
        Protocol.Response response=null;
        try{
            response=responses.take();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return response;
    }
    private void sendRequest(Protocol.Request request) throws ServerException {
        try{
            System.out.println("Sending request ..."+request);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        }catch (IOException e){
            throw new ServerException("Error sending object "+e);
        }
    }
    private void closeConnection(){
        finished=true;
        try{
            output.close();
            input.close();
            connection.close();
            client=null;
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void initializeconnection()throws ServerException{
        try{
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();
            finished=false;
            startReader();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private class ReaderThread implements Runnable

    {

        @Override
        public void run() {
            while(!finished){
                try {
                    Protocol.Response response=Protocol.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);
                    if (isUpdate(response)){
                        handleUpdate(response);
                    }else{

                        try {
                            responses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void handleUpdate(Protocol.Response response) {
        if(response.getType()== Protocol.Response.Type.NEW_SUBMIT){
            DTOBJCursa[] cursa=Master.getCurse(response);
            System.out.println("Employee submitted from handleUpdate ClientSerivcesProxy");
            try{
                client.AngajatSubmitted(cursa);
            }catch (ServerException e){
                e.printStackTrace();
            }
        }

    }

    private boolean isUpdate(Protocol.Response response) {
        return response.getType()== Protocol.Response.Type.NEW_SUBMIT;
    }
}
