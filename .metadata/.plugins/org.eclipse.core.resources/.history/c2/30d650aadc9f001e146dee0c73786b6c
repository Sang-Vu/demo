package com.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.demo.model.User;

public class UserDAO implements IUserDAO {
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pro_2023_04_16_02_07";
			String user = "root";
			String password = "";
			return DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			return null;
		}
	}

    @Override
    public Optional<User> getUserById(String id, String status) {
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement statement = null;
		ResultSet resultset = null;
		Connection conn = null;
		User result = null;
        try {
        	conn = getConnection();
			statement = conn.prepareStatement(query);
			//setParameter(statement, parameters);
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
    }
}
