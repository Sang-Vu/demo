package com.demo.dao;

import com.demo.model.User;
import java.util.Optional;

public interface IUserDAO  {
	Optional<User> getUserById(String id, String status);
}
