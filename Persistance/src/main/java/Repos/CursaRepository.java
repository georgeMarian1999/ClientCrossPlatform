package Repos;

import Models.Cursa;
import Models.DTOBJCursa;

public interface CursaRepository extends CRUDRepository<Integer, Cursa> {
    Iterable<DTOBJCursa> GroupByCapacitate();
    int findIdByCapacitate(int capacitate);
}
