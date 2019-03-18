package com.secretsauce.processors;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.secretsauce.encryption.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class ResultsProcessor {

    private static Logger logger = LoggerFactory.getLogger(ResultsProcessor.class);

    @Autowired
    private EncryptionUtil encryptionUtil;

    public void analyzeResults(){

        DynamoDB dynamoDB  = new DynamoDB(AmazonDynamoDBClientBuilder
                                    .standard()
                                     .withCredentials(new ProfileCredentialsProvider("adfs"))
                                    .build());

        Table table = dynamoDB.getTable("secret-sauce-results");

        ItemCollection<ScanOutcome> items = table.scan();
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            String employeeId = iterator.next().getString("uid").replace("\"", "");
            logger.info("encrypted employee id [{}]", employeeId);
            logger.info("decrypted employee id [{}]", encryptionUtil.decrypt(employeeId));
        }
    }
}
