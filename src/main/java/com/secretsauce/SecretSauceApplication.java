package com.secretsauce;

import com.secretsauce.processors.CsvFileProcessor;
import com.secretsauce.processors.ExcelFileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;

@SpringBootApplication
public class SecretSauceApplication implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(SecretSauceApplication.class);

    @Autowired
    private ExcelFileProcessor excelFileProcessor;

    @Autowired
    private CsvFileProcessor csvFileProcessor;

    public static void main(String[] args) {
        SpringApplication.run(SecretSauceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String fileType = args[0];
        String filePath = args[1];
        logger.info("Processing file type [{}]", fileType);

        if ("excel".equalsIgnoreCase(fileType)) {
            excelFileProcessor.processFile();
        } else if ("csv".equalsIgnoreCase(fileType)) {
            csvFileProcessor.processFile(filePath);
        }

        exit(0);
    }
}


