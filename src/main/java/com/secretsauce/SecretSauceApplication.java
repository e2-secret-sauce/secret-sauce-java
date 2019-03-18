package com.secretsauce;

import com.secretsauce.processors.CsvFileProcessor;
import com.secretsauce.processors.ExcelFileProcessor;
import com.secretsauce.processors.ResultsProcessor;
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

    @Autowired
    private ResultsProcessor resultsProcessor;

    public static void main(String[] args) {
        SpringApplication.run(SecretSauceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String action = args[0];  //analyze or publish

        if ("publish".equals(action)) {
            String filePath = args[1];
            logger.info("Processing file type [{}]", filePath);
            if (filePath.endsWith(".xlsx")) {
                excelFileProcessor.processFile();
            } else if (filePath.endsWith(".csv")) {
                csvFileProcessor.processFile(filePath);
            }
        } else if ("analyze".equalsIgnoreCase(action)) {
            logger.info("Downloading results from AWS");
            resultsProcessor.analyzeResults();
        }

        exit(0);
    }
}


