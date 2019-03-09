package com.secretsauce;

import com.secretsauce.processors.ExcelFileProcessor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SecretSauceApplication implements CommandLineRunner {

    @Autowired
    private ExcelFileProcessor excelFileProcessor;

    public static void main(String[] args) {
        SpringApplication.run(SecretSauceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        excelFileProcessor.processFile();
    }
}


