package com.demo.service;

import java.util.Optional;

import com.demo.model.User;
import com.demo.dao.IUserDAO;

public class UserService implements IUserService {
	private IUserDAO userDAO;

	@Override
	public Optional<User> getUserById(String id, String status) throws ClassNotFoundException {
		return userDAO.getUserById(id, status);
	}
}
