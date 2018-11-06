package com.radrian.ReadAndWrtieExcelFiles.tester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelFileText {
	public static void main(String[] args) {
		String fileName = "JIRA_resumen.xlsx";
		String fileRoute = "C:\\Users\\RAdrian\\Documents\\ExcelTesterFiles\\" + fileName;
		String fileSheet = "Hoja1";

		try (FileInputStream file = new FileInputStream(new File(fileRoute))) {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			Row row;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;

				while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					try {
						System.out.println(cell.getNumericCellValue() + " | ");
					} catch (IllegalStateException e) {
						System.out.println(cell.getStringCellValue() + " | ");
					}
				}
				System.out.println();
			}
			workbook.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
