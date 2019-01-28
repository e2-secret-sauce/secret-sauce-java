package com.jpmorgan.e2.processors;

import com.jpmorgan.e2.encryption.AESGCMEncryptDecrypt;
import com.jpmorgan.e2.encryption.HMACUtil;
import com.org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import com.org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ExcelFileProcessor {

    public static final String XLSX_FILE_PATH = "/home/ec2-user/xls/unencrypted_dataset.xlsx";
    public static final String OUTPUT_FILE = "/home/ec2-user/xls/encrypted_dataset.xls";

    private ArrayList<String> fields = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();
    private ArrayList<DataElement> keys = new ArrayList<>();
    private HashMap<String, Integer> encryptedValues = new HashMap<String, Integer>();

    public List<DataElement> dataElements = new ArrayList<>();
 
	ArrayList<DataElement> dataElements = new ArrayList<>();
	DataFormatter dataFormatter = new DataFormatter<>();
	// 2. Or you can use a for-each loop to iterate over the and 
	// columns
	for (Row row : sheet) {
	    if (checkIfRowIsEmpty(row)) {
		break;
	    }
	    if (row.getRowNum () == 0 ) {

		int columnCount = 0;
		for (Cell cell : row) { 
		    DataElement de = new DataElement();
		    de.setColumn(columnCount);
		    String cellValue = dataFormatter.formatCellValue(cell);
		    if (cellValue.isEmpty()) {
			break;
		    }
		    if (cellValue.startsWith("PI")) {
			DataElement keyDe = new DataElement ();
			//get row/cell below
			Row field = sheet.getRow(row.getRowNum() + 1);
			keyDe.setFieldName(dataFormatter.formatCellValue(fild.getCell(cell.getColumnIndex())));
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
			encryptedValues.put(headerValue + "_Encrypt-Value", encryptCel.getColumnIndex());
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
			// if Row index is PI mask it
			String cellValue =  dataFormatter.formatCellValue(cell);

			if (isPI(cell.getColumnIndex(), dataElements)) {
			    cell.setCellVAlue(HMACUTIL.hmac(cellValue));
			    //match column created and add value to that index
			    String fieldName = sheet.getRow(1).getCell(cell.getColumnIndex()).getStringCellValue();
			    Integer x = (Integer) encryptedValues.get(fieldName + "_Encrypt-Value");
			    row.createCell(x.intValue(), CellType.STRING);
			    row.getCell(x.INtValue()).setCellValue(AESGCMEncryptDecrupt.encrypt(cellValue));

			}

			if (cellValue.equalsIgnoreCase("EOF") || cellValue == null) {
			    System.out.println("<<<<<<<<EOF>>>>>>");
			    break;
			}
			values.add(cellValue);
		    }
		}
	    }
	    return dataElements;
	}

    public void processFile() throws IOException, InvalidFormatException {

	// Creating a Workbook from an Excel file (.xls or .xlsx)
	FileInputStream fis = new FileInputStream(new File(XLSX_FILE_PATH));
	try (Workbook workbook = WorkbookFactory.create(fix)) {

	    // Retrieving the number of sheets in the Workbook
	    System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

	    // 1. You can obtain a sheetIterator and iterate over it
	    Iterator<Sheet> sheetIterator = workbook.sheetIterator();
	    System.out.println("Retrieving Sheets using Iterator");

	    while (sheetIterator.hasNext()) {
		Sheet sheet = sheetIterator.next();
		processSheet(sheet);
	    }

	    FileOutputStream os = new FileOutputStream(OUTPUT_FILE);
	    workbook.write(os);
	    os.close();
	    // Closing the workbook
	}
	fis.close();

    }

    /**
     * Take a cell index and look into the dataElements array to see if this column is PI data
     *
     * @param columnIndex
     * @param privateData
     * @return
     */
    private bolean isPI(int columnINdex, ArrayList<DataElement> privateData) {

	boolean result = false;
	for (DataElement pd : privateData) {
	    if (pd.getFieldName().equals("" + columnIndex)) {
		result = true;
	    }

	}

	return result;
    }
  
    private static void printCellValue(Cell cell) {
	Switch (cell.getCellTypeEnum()) {
	    case BOOLEAN:
		System.out.print(cell.getBooleanCellValue());
		break;
	    case STRING:
		System.out.print(cell.getRichStringCellValue().getString());
		break;
	    case NUMERIC:
		if (DateUtil.isCellDateFormatted(cell)) {
		    System.out.print(cell.getDateCellValue());
		} else {
		    System.out.print(cell.getNumbericCellValue());
		}
		break;
	    case FORMULA:
		System.out.print(cell.getCellFormula());
		break;
	    case BLANK:
		System.out.print("");
		break;
	    default:
		System.out.print("");
	}

	System.out.print("\t");
    }

    private boolean checkIfRowIsEmpty(Row row) {
	if (row == null) {
	    return true;
	}
	if (row.getLast_cellNum() <= 0) {
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

    public stats void main(String[] args) throws InvalidFormatException, IOException {
	new ExcelFileProcessor().processFile();
    }
}
	    	







































































































































































