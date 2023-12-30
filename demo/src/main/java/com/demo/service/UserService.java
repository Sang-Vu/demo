package com.demo.service;

import java.util.Optional;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.nio.charset.StandardCharsets;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

import com.demo.model.User;
import com.demo.dao.IUserDAO;

public class UserService implements IUserService {
	@Inject
	private IUserDAO userDAO;

	@Override
	public Optional<User> getUserById_Role(String id, String role, String pass) throws ClassNotFoundException {
		String status = "OK";
		return userDAO.getUserById_Role_Pass(id, role, pass, status);
	}

	@Override
	public String encrypt(String text, String pwd)
			throws UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		try {
			byte[] originalBytes = text.getBytes("UTF-8");
			byte[] passwordBytes = pwd.getBytes("UTF-8");

			// Hash the password with SHA-256
			passwordBytes = MessageDigest.getInstance("SHA-256").digest(passwordBytes);

			// Getting the salt size
			int saltSize = getSaltSize(passwordBytes);
			// Generating salt bytes
			byte[] saltBytes = getRandomBytes(saltSize);

			// Appending salt bytes to original bytes
			byte[] bytesToBeEncrypted = new byte[saltBytes.length + originalBytes.length];
			System.arraycopy(saltBytes, 0, bytesToBeEncrypted, 0, saltBytes.length);
			System.arraycopy(originalBytes, 0, bytesToBeEncrypted, saltBytes.length, originalBytes.length);

			byte[] encryptedBytes = aesEncrypt(bytesToBeEncrypted, passwordBytes);

			return Base64.getEncoder().encodeToString(encryptedBytes).replace("+", "__");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String encrypt2(String text, String pwd)
			throws UnsupportedEncodingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		try {
			byte[] originalBytes = text.getBytes(StandardCharsets.UTF_8);
            byte[] passwordBytes = pwd.getBytes(StandardCharsets.UTF_8);

            // Hash the password with SHA-256
            passwordBytes = MessageDigest.getInstance("SHA-256").digest(passwordBytes);

            // Getting the salt size
            int saltSize = getSaltSize2(passwordBytes);
            // Generating salt bytes
            byte[] saltBytes = getRandomBytes(saltSize);

            // Derive key and IV using PBKDF2 with SHA1 (consistent with C# code)
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(new String(passwordBytes, StandardCharsets.UTF_8).toCharArray(), saltBytes, 1000, 256);
            SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            // Encrypt the data using Cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Use IV from Cipher
            byte[] ivBytes = cipher.getIV();

            byte[] encryptedBytes = cipher.doFinal(originalBytes);

            // Combine IV and encrypted data
            byte[] combinedBytes = new byte[ivBytes.length + encryptedBytes.length];
            System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
            System.arraycopy(encryptedBytes, 0, combinedBytes, ivBytes.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combinedBytes).replace("+", "__");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	private int getSaltSize(byte[] passwordBytes)
			throws InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(new String(passwordBytes, StandardCharsets.UTF_8).toCharArray(),
					passwordBytes, 1000, 2);
			byte[] key = factory.generateSecret(spec).getEncoded();

			int saltSize = 0;
			for (byte b : key) {
				saltSize += (int) b & 0xFF; // Convert to unsigned int
			}
			return saltSize;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return 0;
	}
	private int getSaltSize2(byte[] passwordBytes) {
		try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(new String(passwordBytes, StandardCharsets.UTF_8).toCharArray(), passwordBytes, 1000, 16 * 8);
            byte[] key = factory.generateSecret(spec).getEncoded();

            int saltSize = 0;
            for (byte b : key) {
                saltSize += (int) b & 0xFF; // Convert to unsigned int
            }
            return saltSize;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

	private byte[] getRandomBytes(int length) {
		byte[] ba = new byte[length];
		new SecureRandom().nextBytes(ba);
		return ba;
	}

	private byte[] aesEncrypt(byte[] bytesToBeEncrypted, byte[] passwordBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
			BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
		byte[] encryptedBytes;

		// Set your salt here, change it to meet your flavor:
		byte[] saltBytes = passwordBytes;
		// Example:
		// saltBytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };

		// Derive key and IV using PBKDF2 with SHA1 (consistent with C# code)
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(new String(passwordBytes, "UTF-8").toCharArray(), saltBytes, 1000, 256);
		SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

		// Use the IV from the key derivation
		IvParameterSpec ivParameterSpec = new IvParameterSpec(factory.generateSecret(spec).getEncoded());

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			byteArrayOutputStream.write(ivParameterSpec.getIV()); // Write the IV to the output stream
			try (CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher)) {
				cipherOutputStream.write(bytesToBeEncrypted);
			}
			encryptedBytes = byteArrayOutputStream.toByteArray();
		}

		return encryptedBytes;
	}
}
