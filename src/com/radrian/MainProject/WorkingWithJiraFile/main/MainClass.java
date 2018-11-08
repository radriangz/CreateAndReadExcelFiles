package com.radrian.MainProject.WorkingWithJiraFile.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFRow.CellIterator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainClass {
	private static final String ORIGINAL_ESTIMATE = "Estimación original";
	private static final String RESPONSIBLE = "Responsable";
	private static final byte HEADER_ROW = 0;

	public static void main(String[] args) {
		String fileName = "JIRA_resumen.xlsx";
		String fileRoute = "C:\\Users\\RAdrian\\Documents\\ExcelTesterFiles\\" + fileName;
		byte columnOriginalEstimate = 0;
		byte columnResponsible = 0;
		// String fileSheet = "Hoja1";

		try (FileInputStream file = new FileInputStream(new File(fileRoute))) {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			Row row;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					
					if (HEADER_ROW == cell.getRowIndex()) {
						if(CellType.STRING.equals(cell.getCellType())) {
							if(RESPONSIBLE.equals(cell.getStringCellValue())) {
								columnResponsible = (byte) cell.getColumnIndex();
							} else if(ORIGINAL_ESTIMATE.equals(cell.getStringCellValue())) {
								columnOriginalEstimate = (byte) cell.getColumnIndex();
							} else {
								continue;
							}
						}
					}
					if (columnResponsible == cell.getColumnIndex()) {
						System.out.println("Responsable: " + cell.getStringCellValue());
					} else if (columnOriginalEstimate == cell.getColumnIndex()) {
						if(CellType.STRING.equals(cell.getCellType())) {
							System.out.println("Estimación original " + cell.getStringCellValue());
						} else if (CellType.NUMERIC.equals(cell.getCellType())) {
							System.out.println("Estimación original " + cell.getNumericCellValue());
						} else {
							continue;
						}
					}
				}
			}
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
