package Repos.Repositories;

import Models.Inscriere;
import Repos.InscriereRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class InscriereRepo implements InscriereRepository {

    private JDBC utils;

    private static final Logger logger = LogManager.getLogger(InscriereRepo.class);

    public InscriereRepo(Properties propertiesFactoryBean) {
        logger.info("Initialiazing InscriereRepo with proprieties {}",propertiesFactoryBean);
        utils=new JDBC(propertiesFactoryBean);
    }

    @Override
    public int size() {
        logger.traceEntry("Se calculeaza marimea tabelei inscriere");

        Connection con=utils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as SIZE from Inscriere")){
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }

        }catch (SQLException ex){
            logger.error(ex);
        }
        return 0;
    }

    @Override
    public void save(Inscriere entity) {
        logger.traceEntry("Se salveaza inscirerea {}",entity);

        Connection con=utils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("insert into Inscriere values(?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setInt(2,entity.getIdParticipant());
            preStmt.setInt(3,entity.getIdCursa());
            int result=preStmt.executeUpdate();

        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit("S-a salvat inscrierea {}",entity);

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Inscriere entity) {

    }

    @Override
    public Inscriere findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Inscriere> findAll() {
        return null;
    }

    @Override
    public int findMaxId() {
        int id;
        logger.traceEntry("Se cauta id-ul maxim din tabela Inscrierer");

        Connection con=utils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select max(idInscriere) as maxim from Inscriere")){
            try(ResultSet result=preStmt.executeQuery()){
                if(result.next()){
                    id=result.getInt("maxim");
                    return id;
                }
            }

        }catch(SQLException ex){
            logger.error(ex);
        }
        return 0;
    }
}
