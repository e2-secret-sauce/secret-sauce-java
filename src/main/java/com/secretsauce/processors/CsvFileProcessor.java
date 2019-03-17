package com.secretsauce.processors;

import com.opencsv.CSVWriter;
import com.secretsauce.SecretSauceApplication;
import com.secretsauce.storage.S3Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CsvFileProcessor {

    private static Logger logger = LoggerFactory.getLogger(CsvFileProcessor.class);

    @Autowired
    private CsvProcessor csvProcessor;

    @Autowired
    private S3Publisher s3Storage;


    public void processFile(String unencryptedFileName) throws IOException {

        logger.info("Encrypting PI data elements in file [{}]", unencryptedFileName);

        CsvProcessor.CsvData csvData = csvProcessor.parseCsv(new FileInputStream(new File(unencryptedFileName)));
        CsvProcessor.CsvData encryptedCsv = CsvUtil.encryptCsvFile(csvData);

        String processId = UUID.randomUUID().toString();
        String encryptedFileName = processId + ".encrypted.travel.data.csv";
        CSVWriter csvWriter = new CSVWriter(new FileWriter(encryptedFileName));
        for (List<String> list : encryptedCsv.getContents()) {
            String[] line = list.toArray(new String[list.size()]);
            csvWriter.writeNext(line);
        }
        csvWriter.close();

        logger.info("File [{}] is ready to publish to AWS", encryptedFileName);
        s3Storage.publish(encryptedFileName);
    }
}
