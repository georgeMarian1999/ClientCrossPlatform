package Services;

import Models.DTOAngajat;
import Models.DTOBJCursa;

public interface IObserver {
    void AngajatLoggedIn(DTOAngajat employee)throws ServerException;
    void AngajatLoggedOut(DTOAngajat employee)throws ServerException;
    void AngajatSubmitted(DTOBJCursa[] curse) throws ServerException;

}
