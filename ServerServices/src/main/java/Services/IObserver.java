package Services;

import Models.DTOAngajat;
import Models.DTOBJCursa;



public interface IObserver  {
    void AngajatSubmitted(DTOBJCursa[] curse) throws ServerException;

}
