package com.demo.service;
import java.util.Optional;

import com.demo.model.User;

public interface IUserService {
	Optional<User> getUserById(String id, String status) throws ClassNotFoundException;
}
