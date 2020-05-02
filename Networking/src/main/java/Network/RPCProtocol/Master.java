package Network.RPCProtocol;

import Models.DTOAngajat;
import Models.DTOBJCursa;
import Models.DTOBJPartCapa;
import Models.DTOInfoSubmit;

public class Master {
    public static Protocol.Request CreateLoginRequest(DTOAngajat employee){
        Protocol.DTOAngajat dtoAngajat = Protocol.DTOAngajat.newBuilder().setUsername(employee.getUsername()).setPassword(employee.getPassword()).build();
        Protocol.Request request = Protocol.Request.newBuilder().setType(Protocol.Request.Type.LOGIN).setAngajat(dtoAngajat).build();
        return request;
    }
    public static Protocol.Request CreateLogoutRequest(DTOAngajat employee){
        Protocol.DTOAngajat dtoAngajat = Protocol.DTOAngajat.newBuilder().setUsername(employee.getUsername()).setPassword(employee.getPassword()).build();
        Protocol.Request request = Protocol.Request.newBuilder().setType(Protocol.Request.Type.LOGOUT).setAngajat(dtoAngajat).build();
        return request;
    }
    public static Protocol.Request CreateGetCurrentCurseRequest(){
        Protocol.Request request = Protocol.Request.newBuilder().setType(Protocol.Request.Type.GET_CURRENT_CURSE).build();
        return request;
    }
    public static Protocol.Request CreateSearchByTeamRequest(String team){
        Protocol.Request request= Protocol.Request.newBuilder().setType(Protocol.Request.Type.SEARCH_BY_TEAM).setTeam(team).build();
        return request;
    }
    public static Protocol.Request CreateSubmitInscRequest(DTOInfoSubmit infoSubmit){
        Protocol.InfoSubmit infoSubmit1=Protocol.InfoSubmit.newBuilder().setCapacitate(infoSubmit.getCapacitate()).setNumeParticipant(infoSubmit.getNumePart()).setNumeEchipa(infoSubmit.getNumeEchipa()).build();
        Protocol.Request request= Protocol.Request.newBuilder().setType(Protocol.Request.Type.SUBMIT_INSC).setInfoSubmit(infoSubmit1).build();
        return request;
    }
    public static Protocol.Request CreateGetAllTeamsRequest(){
        Protocol.Request request=Protocol.Request.newBuilder().setType(Protocol.Request.Type.GET_ALL_TEAMS).build();
        return request;
    }
    public static DTOBJCursa[] getCurse(Protocol.Response response){
        DTOBJCursa[] curse=new DTOBJCursa[response.getCurseCount()];
        for(int i=0;i<response.getCurseCount();i++){
            Protocol.DTOCursa cursa=response.getCurse(i);
            DTOBJCursa cursa1=new DTOBJCursa(cursa.getIdCursa(),cursa.getCapacitate(),cursa.getNrInscrisi());
            curse[i]=cursa1;
        }
        return curse;
    }
    public static DTOBJPartCapa[] getSearchResult(Protocol.Response response){
        DTOBJPartCapa[] participanti=new DTOBJPartCapa[response.getPartCount()];
        for(int i=0;i<response.getPartCount();i++){
            Protocol.DTOPart dtoPart=response.getPart(i);
            DTOBJPartCapa part=new DTOBJPartCapa(dtoPart.getIdParticipant(),dtoPart.getNumePart(),dtoPart.getCapacitate());
            participanti[i]=part;
        }
        return participanti;
    }
    public static String[] getAllTeams(Protocol.Response response){
        String[] teams=new String[response.getEchipeCount()];
        for(int i=0;i<response.getEchipeCount();i++){
            String team=response.getEchipe(i);
            teams[i]=team;

        }
        return teams;
    }
    public static String getError(Protocol.Response response){
        String errorMessage=response.getError();
        return errorMessage;
    }
}
