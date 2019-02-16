package com.secretsauce.encryption.aws;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.regions.Regions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class AwsKmsUtil {
    private static Logger logger = LoggerFactory.getLogger(AwsKmsUtil.class);

    public String encrypt(String data){

        String keyArn = "";

        final AwsCrypto crypto = new AwsCrypto();

        // Set up the KmsMasterKeyProvider backed by the default credentials
        final KmsMasterKeyProvider prov =
                new KmsMasterKeyProvider()
                        .builder()
                          .withCredentials(new ProfileCredentialsProvider("adfs"))
                          .withDefaultRegion(Regions.US_EAST_1.toString())
                          .withKeysForEncryption(keyArn)
                        .build();

        // blogs.aws.amazon.com/security/post/Tx2LZ6WBJJANTNW/How-to-Protect-the-Integrity-of-Your-Encrypted-Data-by-Using-AWS-Key-Management
        final Map<String, String> context = Collections.singletonMap("Example", "String");

        final String ciphertext = crypto.encryptString(prov, data, context).getResult();
        logger.info("plainText [{}] became cipherText [{}]", data, ciphertext);

        return ciphertext;
    }

    public String decrypt(String cipherText){
        return "";
    }
}
