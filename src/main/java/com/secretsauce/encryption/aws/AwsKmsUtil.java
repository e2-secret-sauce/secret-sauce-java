package com.secretsauce.encryption.aws;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.regions.Regions;
import com.secretsauce.encryption.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@Profile("aws")
public class AwsKmsUtil implements EncryptionUtil {

    private static Logger logger = LoggerFactory.getLogger(AwsKmsUtil.class);

    private static final String KEY_ARN = "arn:aws:kms:us-east-1:775297465882:key/50837589-2e31-42ce-a327-d546767cda21";

    public AwsKmsUtil() {
        logger.info("Supplying encryption algorithms with keys from AWS KMS");
    }

    @Override
    public String encrypt(String data){


        // Set up the KmsMasterKeyProvider backed by the default credentials
        final KmsMasterKeyProvider prov = KmsMasterKeyProvider
                        .builder()
                          .withCredentials(new ProfileCredentialsProvider("adfs"))
                          .withDefaultRegion(Regions.US_EAST_1.toString())
                          .withKeysForEncryption(KEY_ARN)
                        .build();

        //blogs.aws.amazon.com/security/post/Tx2LZ6WBJJANTNW/How-to-Protect-the-Integrity-of-Your-Encrypted-Data-by-Using-AWS-Key-Management
        final Map<String, String> context = Collections.singletonMap("Example", "String");

        final AwsCrypto crypto = new AwsCrypto();
        final String cipherText = crypto.encryptString(prov, data, context).getResult();
        logger.info("plainText [{}] became cipherText [{}]", data, cipherText);

        return cipherText;
    }

    @Override
    public String decrypt(String cipherText){
        // Set up the KmsMasterKeyProvider backed by the default credentials
        final KmsMasterKeyProvider prov = KmsMasterKeyProvider
                .builder()
                .withCredentials(new ProfileCredentialsProvider("adfs"))
                .withDefaultRegion(Regions.US_EAST_1.toString())
                .withKeysForEncryption(KEY_ARN)
                .build();

        final AwsCrypto crypto = new AwsCrypto();
        String plainText = crypto.decryptString(prov, cipherText).getResult();
        logger.info("cipherText [{}] became plainText[{}]", cipherText, plainText);
        return plainText;
    }
}
