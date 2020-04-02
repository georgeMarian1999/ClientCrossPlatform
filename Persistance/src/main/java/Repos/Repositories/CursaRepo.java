package Repos.Repositories;

import Models.Cursa;
import Models.DTOBJCursa;
import Repos.CursaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CursaRepo implements CursaRepository {
    private JDBC utils;

    private static final Logger logger = LogManager.getLogger(CursaRepo.class);

    public CursaRepo(Properties props){
        logger.info("Initializing PersoaneREPO with properties: {} ",props);
        utils=new JDBC(props);
    }



    @Override
    public int size() {
        logger.traceEntry();

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Cursa")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }

    @Override
    public void save(Cursa entity) {
        logger.traceEntry("se salveaza Cursa  {} ",entity);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Cursa values (?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setInt(2,entity.getCapacitate());
            int result=preStmt.executeUpdate();

        }catch(SQLException ex){
            logger.error(ex);
        }
        logger.info("s a salvat Cursa {} "+entity);
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("se sterge cursa cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Cursa where idCursa=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }
        catch (SQLException ex){
            logger.error(ex);
        }

        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Cursa entity) {
        logger.traceEntry("se updateaza cursa cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Cursa set idCursa=?,capacitate=? where idCursa=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setInt(2,entity.getCapacitate());
            preStmt.setInt(3,integer);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Cursa findOne(Integer integer) {
        logger.traceEntry("se cauta cursa cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Cursa where idCursa=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()){
                if (result.next()){
                    Integer id,capac;
                    id=result.getInt("idCursa") ;
                    capac=result.getInt("capacitate");
                    Cursa C=new Cursa(id,capac);
                    logger.traceExit(C);
                    return C;
                }
            }

        }catch (SQLException ex){
            logger.error(ex);
        }

        return null;
    }

    @Override
    public Iterable<Cursa> findAll() {
        Connection con=utils.getConnection();
        List<Cursa> Curse=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Cursa")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("idCursa");
                    Integer capac;
                    capac=result.getInt("capacitate");
                    Cursa P=new Cursa(id,capac);
                    Curse.add(P);
                }
            }
        } catch (SQLException e) {
            logger.error(e);

            System.out.println("Error DB "+e);
        }
        logger.traceExit(Curse);

        return Curse;
    }

    @Override
    public Iterable<DTOBJCursa> GroupByCapacitate() {
        List<DTOBJCursa> curse=new ArrayList<>();
        logger.traceEntry("Se groupeaza cursele dupa capacitate");

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select C.idCursa,C.capacitate,count(P.idParticipant) as Nr from Cursa C LEFT JOIN Inscriere I on C.idCursa = I.idCursa LEFT JOIN Participant P on I.idParticipant = P.idParticipant GROUP BY C.capacitate;")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    int id=result.getInt("idCursa");
                    int capacitate=result.getInt("capacitate");
                    int nrinscrisi=result.getInt("Nr");
                    DTOBJCursa obj=new DTOBJCursa(id,capacitate,nrinscrisi);
                    curse.add(obj);
                }
            }

        }catch(SQLException ex){
            logger.error(ex);
        }
        return curse;
    }

    @Override
    public int findIdByCapacitate(int capacitate) {
        int id;

        logger.traceEntry("Se cauta id-ul cursei cu capacitatea {}",capacitate);

        Connection con=utils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select idCursa from Cursa where capacitate=?")){
            preStmt.setInt(1,capacitate);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()){
                    id=result.getInt("idCursa");
                    return id;
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
        }
        return 0;
    }
}
