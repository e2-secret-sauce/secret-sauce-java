package com.secretsauce.encryption;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class HMACUtil {

    public static final String ENCRYPTION_ALGORITHM = "HmacSHA256";

    private static byte[] key = generateHmacKey();

    private static byte[] generateHmacKey() {
        final int SHA256_KEYLENGTH = 256;
        SecureRandom scMac = new SecureRandom();
        byte[] secret = new byte[SHA256_KEYLENGTH];
        scMac.nextBytes(secret);
        SecretKeySpec hmacSecretKey = new SecretKeySpec(secret, ENCRYPTION_ALGORITHM);
        return hmacSecretKey.getEncoded();
    }

    public static String hmacSha1(String value) {
        try {
            // Get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);

            // Get an hamc sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(ENCRYPTION_ALGORITHM);
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());
            return Hex.encodeHexString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmac(String plainText) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(plainText);
    }
}
