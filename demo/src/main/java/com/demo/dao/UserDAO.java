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
	//	try {
		//	Class.forName("com.mysql.cj.jdbc.Driver");
		//	String url = "jdbc:mysql://localhost:3306/pro_2023_04_16_02_07";
		//	String user = "root";
		//	String password = "";
		//	return DriverManager.getConnection(url, user, password);
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String jdbcUrl = "jdbc:oracle:thin:@//erp.pungkookvn.com:1521/pkerp";
	        String username = "PKPCM";
	        String password = "PKPCM@)!&";

	        return DriverManager.getConnection(jdbcUrl, username, password);
	//	} catch (ClassNotFoundException | SQLException e) {
		//	return null;
		//}
	}
//Data Source=(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=erp.pungkookvn.com)(PORT=1521))
	//(CONNECT_DATA=(SERVICE_NAME=pkerp)));User Id=PKPCM;Password=PKPCM@)!&;",
    @Override
    public Optional<User> getUserById(String id, String status) throws ClassNotFoundException {
        String query = "SELECT * FROM PKERP.T_CM_USMT WHERE USERID = ? AND STATUS = ?";
        PreparedStatement statement = null;
		ResultSet resultset = null;
		Connection conn = null;
		User result = null;
        try {
        	conn = getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, id);
			statement.setString(2, status);
			resultset = statement.executeQuery();
			while (resultset.next()) {
				//result.add(rowMapper.mapRow(resultset));
			}
			return Optional.of(result);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly
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
				return Optional.empty();
			}
		}
        return Optional.empty();
    }
}
