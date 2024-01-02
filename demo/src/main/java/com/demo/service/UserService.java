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
import javax.crypto.IllegalBlockSizeException;
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
	public static void encrypt_n(String info) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// Your key as a string
        String keyString = "4c454b8a31d611eebe560242ac120002";

     // Convert the key string to bytes using UTF-8 encoding
        byte[] keyBytes = keyString.getBytes("UTF-8");
        
     // Generate a secret key from the key bytes
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        
        // Generate a random IV
        byte[] iv = generateRandomIV();

        // Create a cipher and initialize with the secret key and IV
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        // Encrypt the data
        byte[] encryptedData = cipher.doFinal(info.getBytes());

        // Print the encrypted data
        System.out.println("Encrypted Data (Base64): " + Base64.getEncoder().encodeToString(encryptedData));
        // Print the IV (for sharing with the C# application)
        System.out.println("IV (Base64): " + Base64.getEncoder().encodeToString(iv));
	}
	
	public static void decrypt_n() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// Base64-encoded IV and encrypted data received from C#
        String base64IV = "6W9F84fd1AaSOwxWhcDeQw==";
        String base64EncryptedData = "ftbTEwuyzJgbIQnAYzqBaaFbYPdo9XriQVjXAJ9CjEaYcYH3wxRVP7QktJPy0D8lhKzaJhWi8AW5Rlr1n1chazESX7zketp/w+k7FeTORk0=";

        // Convert Base64 strings to byte arrays
        byte[] iv = Base64.getDecoder().decode(base64IV);
        byte[] encryptedData = Base64.getDecoder().decode(base64EncryptedData);

        // Your key as a string (must be 16, 24, or 32 bytes for AES-128, AES-192, or AES-256)
        String keyString = "4c454b8a31d611eebe560242ac120002";

        // Convert key string to bytes
        byte[] keyBytes = keyString.getBytes("UTF-8");

        // Create AES decryptor
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));

        // Decrypt the data
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // Convert the decrypted data to a string
        String decryptedText = new String(decryptedData, "UTF-8");

        // Print the decrypted text
        System.out.println("Decrypted Text: " + decryptedText);
      //  return decryptedText;
	}
	
	private static byte[] generateRandomIV() {
        // Generate a random IV using SecureRandom
        byte[] iv = new byte[16]; // IV size for AES is typically 16 bytes
        new SecureRandom().nextBytes(iv);
        return iv;
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
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");//PKCS5Padding
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

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");//PKCS5Padding
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
