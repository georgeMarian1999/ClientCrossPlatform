package Repos;

import Models.DTOBJPartCapa;
import Models.Echipa;

public interface EchipaRepository extends CRUDRepository<Integer, Echipa> {
    Iterable<DTOBJPartCapa> cautare(String numeEchipa);
    int FindidByName(String numeEchipa);
}
