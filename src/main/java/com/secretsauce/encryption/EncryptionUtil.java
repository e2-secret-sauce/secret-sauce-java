package com.secretsauce.encryption;

public interface EncryptionUtil {
    String encrypt(String data);

    String decrypt(String cipherText);
}
