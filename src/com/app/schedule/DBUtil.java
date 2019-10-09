
package com.app.schedule;

import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * @author chenp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DBUtil {

    private static Logger logger = Logger.getLogger(DBUtil.class);
    
    
    /**
     * release resources
     * ignore Exception
     * @param rs
     */
    public static void closeResource(java.sql.ResultSet rs){
        try{
            if(rs != null){
                rs.close();
                rs = null;
            }
        }catch(SQLException sex){
            
        }
    }
    
    /**
     * release
     * ignore Exception
     * @param st
     */
    public static void closeResource(java.sql.Statement st){
        try{
            if(st != null){
                st.close();
                st = null;
            }
        }catch(SQLException sex){
            
        }
    }
        
    /**
     * close database connection
     * ignore exception
     * @param conn
     */
    public static void closeResource(java.sql.Connection conn){
        try{
            if(conn != null){
                conn.close();
                conn = null;
            }
        }catch(SQLException sex){
            logger.error("Error happened when closeConnection()! Exception is : \n" + sex, sex);
        }
    }

}
