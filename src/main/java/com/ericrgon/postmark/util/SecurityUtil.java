package com.ericrgon.postmark.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public final class SecurityUtil {

    // Number of PBKDF2 hardening rounds to use. Larger values increase
    // computation time. You should select a value that causes computation
    // to take >100ms.
    private final static int ITERATIONS = 1000;

    // Generate a 256-bit key
    private final static int OUTPUT_KEY_LENGTH = 256;

    private final static String ENCRYPTION_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt){
        try{
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
            KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, ITERATIONS, OUTPUT_KEY_LENGTH);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

            return secretKey;
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Key spec doesn't exist.");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algorithm doesn't exist." );
        }
    }

    public static SecretKey retrieveKey(char[] pin, byte[] salt){
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
            PBEKeySpec keySpec = new PBEKeySpec(pin, salt, ITERATIONS, OUTPUT_KEY_LENGTH);
            return secretKeyFactory.generateSecret(keySpec);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    public static SecretKey generateSalt() {
        try {
            // Generate a 256-bit key
            final int outputKeyLength = 256;

            SecureRandom secureRandom = new SecureRandom();


            KeyGenerator keyGenerator = null;
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(outputKeyLength, secureRandom);

            SecretKey key = keyGenerator.generateKey();
            return key;

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("AES encryption doesn't exist.");
        }
    }

    public static byte[] encrypt(SecretKey key,String rawData){
        //Encrypt the credentials
        try {
            byte[] rawDataArray = rawData.getBytes("UTF-8");
            Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE, key);

            byte[] result = cipher.doFinal(rawDataArray);
            return result;
        } catch (NoSuchAlgorithmException e) {
            Log.d("SEC", e.toString());
        } catch (NoSuchPaddingException e) {
            Log.d("SEC", e.toString());
        } catch (InvalidKeyException e) {
            Log.d("SEC", e.toString());
        } catch (BadPaddingException e) {
            Log.d("SEC", e.toString());
        } catch (IllegalBlockSizeException e) {
            Log.d("SEC", e.toString());
        } catch (UnsupportedEncodingException e) {
            Log.d("SEC", e.toString());
        } catch (InvalidAlgorithmParameterException e) {
            Log.d("SEC", e.toString());
        } catch (InvalidParameterSpecException e) {
            Log.d("SEC", e.toString());
        } catch (NoSuchProviderException e) {
            Log.d("SEC", e.toString());
        }

        return null;
    }

    public static String decrypt(SecretKey key,byte[] encryptedData){
        try {
            Cipher cipher = generateCipher(Cipher.DECRYPT_MODE,key);

            byte[] decyptedData = cipher.doFinal(encryptedData);
            return new String(decyptedData,"UTF-8");
        } catch (NoSuchPaddingException e) {
            Log.d("SEC", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("SEC", e.toString());
        } catch (InvalidKeyException e) {
            Log.d("SEC", e.toString());
        } catch (BadPaddingException e) {
            Log.d("SEC", e.toString());
        } catch (IllegalBlockSizeException e) {
            Log.d("SEC", e.toString());
        } catch (UnsupportedEncodingException e) {
            Log.d("SEC", e.toString());
        } catch (InvalidAlgorithmParameterException e) {
            Log.d("SEC", e.toString());
        } catch (InvalidParameterSpecException e) {
            Log.d("SEC", e.toString());
        } catch (NoSuchProviderException e) {
            Log.d("SEC", e.toString());
        }

        return "";
    } 
    private static Cipher generateCipher(int mode,SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidParameterSpecException, NoSuchProviderException {
        byte[] data = key.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(mode, skeySpec);

        return cipher;
    }
}
