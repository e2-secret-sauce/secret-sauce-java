package com.secretsauce.encryption.aws;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AwsKmsUtilTest {

    @Test
    @Ignore
    public void testEncrypt(){
        AwsKmsUtil awsEncryptionUtil = new AwsKmsUtil();
        assertThat(awsEncryptionUtil.encrypt("John Killmer")).isNotEmpty();
    }

    @Test
    public void testDecypt(){

    }
}
