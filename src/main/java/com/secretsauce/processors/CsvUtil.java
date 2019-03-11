package com.secretsauce.processors;

import com.secretsauce.encryption.local.AESGCMEncryptDecrypt;
import com.secretsauce.encryption.local.HMACUtil;

import java.util.LinkedList;

public class CsvUtil {

    public static CsvProcessor.CsvData encryptCsvFile(CsvProcessor.CsvData csvData){
        System.out.println(csvData);
        CsvProcessor.CsvData protectedData = new CsvProcessor.CsvData(new LinkedList<>(), new LinkedList<>());

        for (int i = 0; i < csvData.getContents().size(); i++) {
            protectedData.getContents().add(new LinkedList<>());
        }

        for (CsvProcessor.Header header : csvData.getHeaders()) {
            protectedData.getHeaders().add(new CsvProcessor.Header(header.getText()));
            if (header.isProtect()) {
                protectedData.getHeaders().add(new CsvProcessor.Header(header.getText() + "_ENC"));
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
