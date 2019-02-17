package com.secretsauce.encryption.aws;

import com.secretsauce.encryption.EncryptionUtil;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AwsKmsUtilTest {

    @Test
    @Ignore
    public void testEncrypt(){
        EncryptionUtil awsEncryptionUtil = new AwsKmsUtil();
        assertThat(awsEncryptionUtil.encrypt("John Killmer")).isNotEmpty();
    }

    @Test
    @Ignore
    public void testDecrypt(){
        String cipherText = "AYADeN/tES2uin07YGeTnTmQ5yYAcAACAAdFeGFtcGxlAAZTdHJpbmcAFWF3cy1jcnlwdG8tcHVibGljLWtleQBEQXU1aUlUWUdKRFBxUWx6UUtURS8vckNRMVVpcUNzN05ML2prOW54WUFhKzlFbSszNEZVZUR1V0FmM3pUZDdaazFBPT0AAQAHYXdzLWttcwBLYXJuOmF3czprbXM6dXMtZWFzdC0xOjc3NTI5NzQ2NTg4MjprZXkvNTA4Mzc1ODktMmUzMS00MmNlLWEzMjctZDU0Njc2N2NkYTIxALgBAgEAeDaPIJefpaeyzs6a0bg5eLZIKeH+6axnBULsUgvBDb2TAU7XEknbQO9xmVoPYBtDRNAAAAB+MHwGCSqGSIb3DQEHBqBvMG0CAQAwaAYJKoZIhvcNAQcBMB4GCWCGSAFlAwQBLjARBAz0TglsjchIPG5/dYsCARCAO9VvPigTE8hGzBTXfV3UsTzpQUydVvrkUdFzM6Hxs9kvfyxd4jG95Fs0aIIfj0Rzgc/R2KLDiXc0MNFpAgAAAAAMAAAQAAAAAAAAAAAAAAAAAJNr0YkdxDinFS3e5IfZqTz/////AAAAAQAAAAAAAAAAAAAAAQAAAAxrD4+Q3SlAc6IYdJREeA6LXSoTR1EOZHe2RKWlAGcwZQIxAMEoE7wfOzgukflQH53xZOYGxKJAO6RbGhSzGYYFNrF3vS4cn0QncEEq892sasLuJAIwaqB3N0hGHcqlLS8Knb8dNK5ri8bGeAa2rG9YVvmP1SLR2WCgPK2o9BK34ec8PRqQ";
        EncryptionUtil awsEncryptionUtil = new AwsKmsUtil();
        assertThat(awsEncryptionUtil.decrypt(cipherText)).isEqualTo("John Killmer");
    }
}
