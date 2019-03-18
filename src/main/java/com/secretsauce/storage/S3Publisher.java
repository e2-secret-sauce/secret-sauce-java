package com.secretsauce.storage;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class S3Publisher {

    private static Logger logger = LoggerFactory.getLogger(S3Publisher.class);

    public void publish(String fileName) {

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new ProfileCredentialsProvider("adfs"))
                .withRegion("us-east-1")
                .build();

        List<Bucket> buckets = s3client.listBuckets();
        logger.info("Available S3 Buckets");
        for (Bucket bucket : buckets) {
            logger.info("* {}", bucket.getName());
        }

        String bucketName = "775297465882-secret-sauce-data";
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, new File(fileName));

        logger.info("Publishing [{}] to S3 Bucket [{}]", fileName, bucketName);
        s3client.putObject(request);
    }
}
