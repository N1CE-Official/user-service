package ru.ws.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ws.utils.Constants;

public class AESChiper {	
	
	private static final Logger logger = LoggerFactory.getLogger(AESChiper.class);
	
	static String passphrase = Constants.PASSPHRASE;
	
	public static void main(String[] args) throws GeneralSecurityException, UnsupportedEncodingException {
	    String plaintext = "fabiofabio";
	    String encrypted = encrypt(plaintext);
	    String decrypted = decrypt("P/DrhsXc2XZ+TVMjSWH9bA==zNFi5hC4BmlOV4WHMTCyfw==");
	    System.out.println(encrypted);
	    System.out.println(decrypted);
	}

	private static SecretKeySpec getKeySpec(String passphrase) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		logger.debug("Start - getKeySpec");
		SecretKeySpec secretKeySpec = null;
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-256");
		    secretKeySpec = new SecretKeySpec(digest.digest(passphrase.getBytes("UTF-8")), "AES");
		}catch(Exception e) {
			logger.error("The error is: ", e);
			throw e;
		}finally {
			logger.debug("End - getKeySpec");
		}
	    return secretKeySpec;
	}

	private static Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
		logger.debug("Start - getCipher");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		}catch(Exception e) {
			logger.error("The error is: ", e);
			throw e;
		}finally {
			logger.debug("End - getCipher");
		}
	    return cipher;
	}

	public static String encrypt(String value) throws GeneralSecurityException, UnsupportedEncodingException {
		logger.debug("Start - encrypt");
		String result = null;
		try {
		    byte[] initVector = new byte[16];
//		    SecureRandom.getInstanceStrong().nextBytes(initVector);//lento su linux
		    SecureRandom.getInstance("SHA1PRNG");
		    Cipher cipher = getCipher();
		    cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(passphrase), new IvParameterSpec(initVector));
		    byte[] encrypted = cipher.doFinal(value.getBytes());
		    result = DatatypeConverter.printBase64Binary(initVector) + DatatypeConverter.printBase64Binary(encrypted);
		}catch(Exception e) {
			logger.error("The error is: ", e);
			throw e;
		}finally {
			logger.debug("End - encrypt");
		}	    
	    return result;
	}

	public static String decrypt(String encrypted) throws GeneralSecurityException, UnsupportedEncodingException {
		logger.debug("Start - decrypt");
		String result = null;
		try {
		    byte[] initVector = DatatypeConverter.parseBase64Binary(encrypted.substring(0, 24));
		    Cipher cipher = getCipher();
		    cipher.init(Cipher.DECRYPT_MODE, getKeySpec(passphrase), new IvParameterSpec(initVector));
		    byte[] original = cipher.doFinal(DatatypeConverter.parseBase64Binary(encrypted.substring(24)));
		    result = new String(original);
		}catch(Exception e) {
			logger.error("The error is: ", e);
			throw e;
		}finally {
			logger.debug("End - decrypt");
		}
	    return result;
	}

}
