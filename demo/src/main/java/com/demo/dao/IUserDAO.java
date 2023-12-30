package com.demo.dao;

import com.demo.model.User;
import java.util.Optional;

public interface IUserDAO  {
	Optional<User> getUserById_Role_Pass(String id, String role, String pass, String status) throws ClassNotFoundException;
}
