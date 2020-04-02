package Services;

import Models.*;

public interface IServices  {
    void login(DTOAngajat angajat, IObserver client) throws ServerException;
    void logout(DTOAngajat angajat,IObserver client) throws ServerException;
    void submitInscriere(DTOInfoSubmit infoSubmit) throws ServerException;
    Angajat[] getLoggedEmployees() throws ServerException;
    DTOBJCursa[] getCurseDisp() throws ServerException;
    DTOBJPartCapa[] searchByTeam(String team) throws ServerException;
}
