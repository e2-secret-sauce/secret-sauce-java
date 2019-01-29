package com.secretsauce;

import com.secretsauce.processors.ExcelFileProcessor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SecretSauceApplication {

    public static void main(String[] args) {

        SpringApplication.run(SecretSauceApplication.class, args);

        try {
            new ExcelFileProcessor().processFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}


