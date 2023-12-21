package com.demo.service;

import java.util.Optional;

import com.demo.model.User;
import com.demo.dao.IUserDAO;

public class UserService implements IUserService {
	private IUserDAO userDAO;

	@Override
	public Optional<User> getUserById(String id) throws ClassNotFoundException {
		String status = "OK"
		return userDAO.getUserById(id, status);
	}
}
