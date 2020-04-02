package Repos.Repositories;

import Models.Participant;
import Repos.ParticipantRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantRepo implements ParticipantRepository {

    private JDBC utils;

    private static final Logger logger = LogManager.getLogger(ParticipantRepo.class);

    public ParticipantRepo(Properties props){
        logger.info("Initializing PersoaneREPO with properties: {} ",props);
        utils=new JDBC(props);
    }

    @Override
    public int size() {
        logger.traceEntry();

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Participant")) {
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
    public void save(Participant entity) {
        logger.traceEntry("se salveaza participantul  {} ",entity);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Participant values (?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setInt(3,entity.getIdEchipa());
            int result=preStmt.executeUpdate();

        }catch(SQLException ex){
            logger.error(ex);
        }
        logger.info("s a salvat participantul {} "+entity);
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("se sterge partcipantul cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Participant where idParticipant=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }
        catch (SQLException ex){
            logger.error(ex);
        }

        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Participant entity) {
        logger.traceEntry("se updateaza participantul cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Participant set idParticipant=?,nume=?,idEchipa=?  where idParticipant=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setInt(3,entity.getIdEchipa());
            preStmt.setInt(4,integer);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Participant findOne(Integer integer) {
        logger.traceEntry("se cauta participantul cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Participant where idParticipant=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()){
                if (result.next()){
                    Integer id=result.getInt("idParticipant");
                    String name;
                    Integer idEchipa;
                    name=result.getString("nume");
                    idEchipa=result.getInt("idEchipa");
                    Participant P=new Participant(id,name,idEchipa);
                    logger.traceExit(P);
                    return P;
                }
            }

        }catch (SQLException ex){
            logger.error(ex);
        }

        return null;
    }

    @Override
    public Iterable<Participant> findAll() {
        Connection con=utils.getConnection();
        List<Participant> Participanti=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Participant")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("idParticipant");
                    String name;
                    Integer idEchipa;
                    name=result.getString("nume");
                    idEchipa=result.getInt("idEchipa");
                    Participant P=new Participant(id,name,idEchipa);
                    Participanti.add(P);
                }
            }
        } catch (SQLException e) {
            logger.error(e);

            System.out.println("Error DB "+e);
        }
        logger.traceExit(Participanti);

        return Participanti;
    }

    @Override
    public int findIdByName(String nume) {
        int id;
        logger.traceEntry("Se cauta id-ul participantului {}",nume);

        Connection con=utils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select idParticipant from Participant where nume=?")){
            preStmt.setString(1,nume);
            try(ResultSet result=preStmt.executeQuery()){
                if(result.next()){
                    id=result.getInt("idParticipant");
                    return id;
                }
            }

        }catch(SQLException ex){
            logger.error(ex);
        }
        return 0;
    }

    @Override
    public int findMaxId() {
        int id;
        logger.traceEntry("Se cauta id-ul maxim din tabela Participant");

        Connection con=utils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select max(idParticipant) as maxim from Participant")){
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
