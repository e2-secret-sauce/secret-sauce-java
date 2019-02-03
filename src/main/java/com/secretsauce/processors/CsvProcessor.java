package com.secretsauce.processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class CsvProcessor {

    public static class CsvData {

        private List<Header> headers;
        private List<List<String>> contents;

        public CsvData() {

        }

        public CsvData(List<String> headers, List<List<String>> contents) {
            this.headers = headers.stream().map(h -> new Header(h)).collect(Collectors.toList());
            this.contents = contents;
        }

        public List<Header> getHeaders() {
            return headers;
        }

        public void setHeaders(List<Header> headers) {
            this.headers = headers;
        }

        public List<List<String>> getContents() {
            return contents;
        }

        public void setContents(List<List<String>> contents) {
            this.contents = contents;
        }

        @Override
        public String toString() {
            return "[CsvData]\nHeaders: " + this.headers + "\nContents: " + this.contents;
        }

    }

    public static class Header {

        private String text;
        private boolean protect = false;

        public Header() {

        }

        public Header(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isProtect() {
            return protect;
        }

        public void setProtect(boolean protect) {
            this.protect = protect;
        }

        @Override
        public String toString() {
            return "text=" + this.text + "; protect=" + this.protect;
        }

    }

    public CsvData parseCsv(InputStream in) {
        
        Iterable<CSVRecord> records = this.records(in);
        boolean headerRow = true;
        List<String> headers = new ArrayList<>();
        List<List<String>> contents = new ArrayList<>();

        for (CSVRecord record : records) {
            if (headerRow) {
                headers = IteratorUtils.toList(record.iterator());
                headerRow = false;
            } else {
                contents.add(IteratorUtils.toList(record.iterator()));
            }
        }

        return new CsvData(headers, contents);
    }

    private Iterable<CSVRecord> records(InputStream in) {
        try {
            return CSVFormat.DEFAULT.parse(new InputStreamReader(in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}