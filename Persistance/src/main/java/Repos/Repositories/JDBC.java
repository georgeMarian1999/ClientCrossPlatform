package Repos.Repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBC {
    private Properties JDBCProps;

    private static final Logger logger = LogManager.getLogger(JDBC.class);

    public JDBC(Properties props){
        this.JDBCProps=props;
    }
    private Connection instance=null;
    public Connection getNewConnection(){
        logger.traceEntry();

        String url=JDBCProps.getProperty("Curse.jdbc.url");
        String user=JDBCProps.getProperty("Curse.jdbc.user");
        String pass=JDBCProps.getProperty("Curse.jdbc.pass");
        logger.info("trying to connect to database ... {}",url);
        logger.info("user: {}",user);
        logger.info("pass: {}", pass);
        Connection con=null;
        try{
            if(user!=null && pass!=null)
                con= DriverManager.getConnection(url,user,pass);
            else con= DriverManager.getConnection(url);
        }
        catch(SQLException ex){
            logger.error(ex);
        }
        return con;
    }
    public Connection getConnection(){
        logger.traceEntry();
        try{
        if(instance==null || instance.isClosed())
            instance=this.getNewConnection();
        }catch(SQLException ex){
            logger.error(ex);
        }
        logger.traceExit(instance);
        return instance;
    }
}
