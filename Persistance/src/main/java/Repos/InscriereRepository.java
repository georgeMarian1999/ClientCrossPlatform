package Repos;

import Models.Inscriere;

public interface InscriereRepository extends CRUDRepository<Integer, Inscriere> {
    int findMaxId();
}
