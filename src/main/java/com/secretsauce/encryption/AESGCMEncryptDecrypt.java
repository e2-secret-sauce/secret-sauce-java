package com.secretsauce.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

public class AESGCMEncryptDecrypt {

    private static Logger logger = LoggerFactory.getLogger(AESGCMEncryptDecrypt.class);
    static String aad = "aad";    // Additional authenticated data

    /**
     * The transformation to use.  This is in the form algorithm/mode/padding
     * See https://dcos.oracle.com/javase/8/docs/technotes/guies/security/StandarNames.html#Cipher
     */
    static final String transformation = "AES/GCM/NoPadding";

    static int ivSizeReturnedByHSM;

    //private static final String key = "$ecrest$auce2018!$ecret$auce2018!";
    private static final String key = "$ecrest$auce2018";
    private static SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");


    public static String encrypt(String plainText) {
        return Base64.getEncoder().encodeToString(encrypt(transformation, secretKeySpec, plainText, aad));
    }

    public static String decrypt(String cipherString) {
        byte[] cipherText = Base64.getDecoder().decode(cipherString);
        byte[] iv = Arrays.copyOfRange(cipherText, 0, 12);

        byte[] cipher = Arrays.copyOfRange(cipherText, 12, cipherText.length);
        return new String(decrypt(transformation, secretKeySpec, cipher, iv, aad));
    }

    private static byte[] encrypt(String transformation, Key key, String plainText, String aad) {
        try {
            // Create an encryption cipher.
            Cipher encCipher = Cipher.getInstance(transformation);
            encCipher.init(Cipher.ENCRYPT_MODE, key);
            encCipher.updateAAD(aad.getBytes());
            encCipher.update(plainText.getBytes());

            // Encrypt the plaintext data.
            byte[] ciphertext = encCipher.doFinal();

            // You'll get a new IV from the HSM after encryption. Save it. You'll need to recreate the
            // GCM parameter spec for decryption.  The IV returned by the HSM has a fixed length 16 bytes.
            // Append the IV to the cirphertext for easier managment.
            ivSizeReturnedByHSM = encCipher.getIV().length;
            byte[] finalResult = new byte[ivSizeReturnedByHSM + ciphertext.length];
            System.arraycopy(encCipher.getIV(), 0, finalResult, 0, ivSizeReturnedByHSM);
            System.arraycopy(ciphertext, 0, finalResult, ivSizeReturnedByHSM, ciphertext.length);
            return finalResult;

        } catch (Exception e) {
            logger.error("Couldn't perform AES/GCM encryption", e);
        }
        return null;
    }

    // Decrypt the ciphertext using the specified algorithm, key, IV, and AAD.
    private static byte[] decrypt(String transformation, Key key, byte[] cipherText, byte[] iv, String aad) {
        Cipher decCipher;
        try {
            // Create the decryption cipher.
            decCipher = Cipher.getInstance(transformation);

            // Create a GCM parameter spec from the IV.
            final int GCM_TAG_LENGTH = 16;
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);

            // Configure the decription cipher.
            decCipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            decCipher.updateAAD(aad.getBytes());

            // Decrypt the ciphertext and return the plaintext.
            return decCipher.doFinal(cipherText);

        } catch (Exception e) {
            logger.error("Couldn't perform AES/GCM encryption", e);
        }
        return null;
    }
}
