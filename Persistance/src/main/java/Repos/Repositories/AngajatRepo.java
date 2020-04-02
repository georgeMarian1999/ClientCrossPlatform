package Repos.Repositories;

import Models.Angajat;
import Models.DTOAngajat;
import Repos.AngajatRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AngajatRepo implements AngajatRepository {
    private JDBC utils;

    private static final Logger logger = LogManager.getLogger(AngajatRepo.class);

    public AngajatRepo(Properties props){
        logger.info("Initializing PersoaneREPO with properties: {} ",props);
        utils=new JDBC(props);
    }


    @Override
    public int size() {
        logger.traceEntry();

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Angajat ")) {
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
    public void save(Angajat entity) {
            logger.traceEntry("se salveaza angajatul  {} ",entity);

            Connection con=utils.getConnection();
            try(PreparedStatement preStmt=con.prepareStatement("insert into Angajat values (?,?,?)")){
                preStmt.setInt(1,entity.getId());
                preStmt.setString(2,entity.getUsername());
                preStmt.setString(3,entity.getPassword());
                int result=preStmt.executeUpdate();

            }catch(SQLException ex){
                logger.error(ex);
            }
            logger.info("s a salvat angajatul {} "+entity);
            logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("se sterge angajatul cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Angajat where idAngajat=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }
        catch (SQLException ex){
            logger.error(ex);
        }

        logger.traceExit();

    }

    @Override
    public void update(Integer integer, Angajat entity) {
        logger.traceEntry("se updateaza angajatul cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Angajat set idAngajat=?,username=?,password=?  where idAngajat=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getUsername());
            preStmt.setString(3,entity.getPassword());
            preStmt.setInt(4,integer);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Angajat findOne(Integer integer) {
        logger.traceEntry("se cauta angajatul cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Angajat where idAngajat=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()){
                if (result.next()){
                    Integer id=result.getInt("idAngajat");
                    String username,password;
                    username=result.getString("username");
                    password=result.getString("password");
                    Angajat P=new Angajat(id,username,password);
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
    public Iterable<Angajat> findAll() {
        Connection con=utils.getConnection();
        List<Angajat> Employees=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Angajat")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("idAngajat");
                    String username,password;
                    username=result.getString("username");
                    password=result.getString("password");
                    Angajat P=new Angajat(id,username,password);
                    Employees.add(P);
                }
            }
        } catch (SQLException e) {
            logger.error(e);

            System.out.println("Error DB "+e);
        }
        logger.traceExit(Employees);

        return Employees;
    }

    public Iterable<DTOAngajat> findOtherEmployees(DTOAngajat angajat){
        Connection con=utils.getConnection();
        List<DTOAngajat> result=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select username,password from Angajat where username!=? and password!=?")){
            preStmt.setString(1,angajat.getUsername());
            preStmt.setString(2,angajat.getPassword());
            try(ResultSet resultSet=preStmt.executeQuery()){
                while(resultSet.next()){
                    String user=resultSet.getString("username");
                    String pass=resultSet.getString("password");
                    DTOAngajat angajat1=new DTOAngajat(user,pass);
                    result.add(angajat1);
                }
            }
        }catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit(result);
        return result;
    }
    @Override
    public boolean LocalLogin(String username, String Password) {
        Connection con=utils.getConnection();
        logger.traceEntry("Se cauta anajatul cu username-ul {}",username);
        try(PreparedStatement preStmt=con.prepareStatement("select idAngajat,username,password from Angajat where username=? AND password=?")){
            preStmt.setString(1,username);
            preStmt.setString(2,Password);
            try(ResultSet result=preStmt.executeQuery()){
                if(result.next()){
                    logger.traceExit("S-a gasit angajatul");
                    return true;
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
        }
        logger.traceExit("Nu s-a gasit angajatul");
        return false;

    }
}