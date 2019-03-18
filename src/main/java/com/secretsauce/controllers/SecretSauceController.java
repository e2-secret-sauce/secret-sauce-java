package com.secretsauce.controllers;

import java.io.IOException;
import java.util.LinkedList;

import com.secretsauce.encryption.local.AESGCMEncryptDecrypt;
import com.secretsauce.encryption.local.HMACUtil;
import com.secretsauce.processors.CsvProcessor;
import com.secretsauce.processors.CsvProcessor.CsvData;
import com.secretsauce.processors.CsvProcessor.Header;

import com.secretsauce.processors.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SecretSauceController {

    @Autowired
    private CsvProcessor csvProcessor;

    @Autowired
    private CsvUtil csvUtil;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/upload")
    public CsvData upload(@RequestParam("file") MultipartFile file) throws IOException {
        return csvProcessor.parseCsv(file.getInputStream());
    }

    @RequestMapping("/protect")
    public CsvData protect(@RequestBody CsvData csvData) {
        return csvUtil.encryptCsvFile(csvData);
    }

}