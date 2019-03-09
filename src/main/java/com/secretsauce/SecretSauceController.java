package com.secretsauce;

import java.io.IOException;
import java.util.LinkedList;

import com.secretsauce.encryption.local.AESGCMEncryptDecrypt;
import com.secretsauce.encryption.local.HMACUtil;
import com.secretsauce.processors.CsvProcessor;
import com.secretsauce.processors.CsvProcessor.CsvData;
import com.secretsauce.processors.CsvProcessor.Header;

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
        System.out.println(csvData);
        CsvData protectedData = new CsvData(new LinkedList<>(), new LinkedList<>());

        for (int i = 0; i < csvData.getContents().size(); i++) {
            protectedData.getContents().add(new LinkedList<>());
        }

        for (Header header : csvData.getHeaders()) {
            protectedData.getHeaders().add(new Header(header.getText()));
            if (header.isProtect()) {
                protectedData.getHeaders().add(new Header(header.getText() + "_ENC"));
            }
        }

        for (int i = 0; i < csvData.getHeaders().size(); i++) {
            for (int j = 0; j < csvData.getContents().get(i).size(); j++) {
                if (csvData.getHeaders().get(j).isProtect()) {
                    protectedData.getContents().get(i).add(HMACUtil.hmac(csvData.getContents().get(i).get(j)));
                    protectedData.getContents().get(i).add(new AESGCMEncryptDecrypt().encrypt(csvData.getContents().get(i).get(j)));
                } else {
                    protectedData.getContents().get(i).add(csvData.getContents().get(i).get(j));
                }
            }
        }

        return protectedData;
    }

}