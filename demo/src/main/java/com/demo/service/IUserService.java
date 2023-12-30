package com.demo.service;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;

import com.demo.model.User;

public interface IUserService {
	Optional<User> getUserById_Role(String id, String role, String pass) throws ClassNotFoundException;
	String encrypt(String text, String pwd) throws UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException;
	String encrypt2(String text, String pwd) throws UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException;
}
