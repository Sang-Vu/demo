package com.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.sql.DriverManager;

import com.demo.model.User;

public class UserDAO implements IUserDAO {
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String jdbcUrl = "jdbc:oracle:thin:@//erp.pungkookvn.com:1521/pkerp";
		String username = "PKPCM";
		String password = "PKPCM@)!&";
		return DriverManager.getConnection(jdbcUrl, username, password);
	}

	@Override
    public Optional<User> getUserById_Role_Pass(String id, String role, String pass, String status) throws ClassNotFoundException {
        StringBuilder sql = new StringBuilder(" SELECT usmt.USERID, usmt.NAME, urmt.ROLEID ");
        sql.append(" FROM PKERP.T_CM_USMT usmt ");
        sql.append(" INNER JOIN PKERP.T_CM_URMT urmt ON usmt.USERID = urmt.USERID ");
        sql.append(" WHERE usmt.USERID = ? AND usmt.PASSWD = ? AND urmt.ROLEID = ? AND usmt.STATUS = ? ");
        PreparedStatement statement = null;
		ResultSet resultset = null;
		Connection conn = null;
		User result = null;
        try {
        	conn = getConnection();
        	if(conn != null) {
        		statement = conn.prepareStatement(sql.toString());
    			statement.setString(1, id);
    			statement.setString(2, pass);
    			statement.setString(3, role);
    			statement.setString(4, status);
    			resultset = statement.executeQuery();
    			while (resultset.next()) {
    				result = new User(resultset.getString("USERID"), resultset.getString("ROLEID"), resultset.getString("NAME"));
    			break;
    			}
        	}
			return Optional.of(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (resultset != null) {
					resultset.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return Optional.empty();
			}
		} 
    }
}
