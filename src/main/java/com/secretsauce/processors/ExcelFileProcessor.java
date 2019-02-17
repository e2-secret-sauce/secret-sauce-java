package com.secretsauce.processors;


import com.secretsauce.encryption.EncryptionUtil;
import com.secretsauce.encryption.local.HMACUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@Service
public class ExcelFileProcessor {

    private Logger logger = LoggerFactory.getLogger(ExcelFileProcessor.class);

    private EncryptionUtil encryptionUtil = null;

    public static final String XLSX_FILE_PATH = "C:\\ds\\workspaces\\secretsauce\\secret-sauce-java\\src\\main\\resources\\unencrypted_dataset.xlsx";
    public static final String OUTPUT_FILE = "C:\\ds\\workspaces\\secretsauce\\secret-sauce-java\\src\\main\\resources\\encrypted_dataset.xls";


    @Autowired
    public ExcelFileProcessor(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    public void processFile() throws IOException, InvalidFormatException {

        // Creating a Workbook from an Excel file (.xls or .xlsx)
        FileInputStream fis = new FileInputStream(new File(XLSX_FILE_PATH));
        try (Workbook workbook = WorkbookFactory.create(fis)) {

            logger.info("Workbook has {} sheet(s) to process", workbook.getNumberOfSheets());

            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                processSheet(sheet);
            }

            FileOutputStream os = new FileOutputStream(OUTPUT_FILE);
            workbook.write(os);
            os.close();
        }
        fis.close();
    }

    private void processSheet(Sheet sheet) {
        HashMap<String, Integer> encryptedValues = new HashMap<String, Integer>();
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        ArrayList<DataElement> keys = new ArrayList<>();
        ArrayList<DataElement> dataElements = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet) {
            if (checkIfRowIsEmpty(row)) {
                break;
            }
            if (row.getRowNum() == 0) {

                int columnCount = 0;
                for (Cell cell : row) {
                    DataElement de = new DataElement();
                    de.setColumn(columnCount);
                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (cellValue.isEmpty()) {
                        break;
                    }
                    if (cellValue.startsWith("PI")) {
                        DataElement keyDe = new DataElement();
                        //get row/cell below
                        Row field = sheet.getRow(row.getRowNum() + 1);
                        keyDe.setFieldName(dataFormatter.formatCellValue(field.getCell(cell.getColumnIndex())));
                        keyDe.setKey(true);
                        keyDe.setPI(true);
                        de.setFieldName("" + cell.getColumnIndex());
                        de.setKey(true);
                        de.setPI(true);
                        keys.add(de);

                        //create the headers for the new columns
                        String headerValue = dataFormatter.formatCellValue(field.getCell(cell.getColumnIndex()));
                        Cell encryptCell = field.createCell(field.getLastCellNum(), CellType.STRING);
                        encryptCell.setCellValue(headerValue + "_Encrypt-Value");
                        encryptedValues.put(headerValue + "_Encrypt-Value", encryptCell.getColumnIndex());
                        if (cellValue.equalsIgnoreCase("PI")) {
                            de.setFieldName("" + cell.getColumnIndex());
                            de.setPI(true);
                        }
                    } else {
                        de.setFieldName("N");
                    }

                    dataElements.add(de);
                    columnCount++;
                }
            } else if (row.getRowNum() == 1) {
                // row 2 is column headers marker
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    fields.add(cellValue);
                }
            } else {
                int cellCount = row.getLastCellNum() - 1;
                for (int i = 0; i < cellCount; i++) {
                    Cell cell = row.getCell(i);

                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (isPI(cell.getColumnIndex(), dataElements)) {

                        String hmacCipher = HMACUtil.hmac(cellValue);
                        cell.setCellValue(hmacCipher);

                        String fieldName = sheet.getRow(1).getCell(cell.getColumnIndex()).getStringCellValue();
                        Integer x = encryptedValues.get(fieldName + "_Encrypt-Value");
                        row.createCell(x.intValue(), CellType.STRING);

                        String aesCipher = encryptionUtil.encrypt(cellValue);
                        row.getCell(x.intValue()).setCellValue(aesCipher);
                    }

                    if (cellValue.equalsIgnoreCase("EOF") || cellValue == null) {
                        break;
                    }
                    values.add(cellValue);
                }
            }
        }
        logger.info("Completed processing dataset");
    }

    private boolean isPI(int columnIndex, ArrayList<DataElement> privateData) {

        boolean result = false;
        for (DataElement pd : privateData) {
            if (pd.getFieldName().equals("" + columnIndex)) {
                result = true;
            }
        }

        return result;
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell
                    .getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

}


