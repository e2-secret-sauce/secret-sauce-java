package com.secretsauce.encryption.local;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class HMACUtil {
    private static Logger logger = LoggerFactory.getLogger(HMACUtil.class);

    private static final String ENCRYPTION_ALGORITHM = "HmacSHA256";

    private static byte[] key = generateHmacKey();

    private static byte[] generateHmacKey() {
        final int SHA256_KEYLENGTH = 256;
        SecureRandom scMac = new SecureRandom();
        byte[] secret = new byte[SHA256_KEYLENGTH];
        scMac.nextBytes(secret);
        SecretKeySpec hmacSecretKey = new SecretKeySpec(secret, ENCRYPTION_ALGORITHM);
        return hmacSecretKey.getEncoded();
    }

    public static String hmac(String plainText) {
        String cipherText = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(plainText);
        logger.info("Applying HMAC: [{}] becomes [{}]", plainText, cipherText);
        return cipherText;
    }
}
