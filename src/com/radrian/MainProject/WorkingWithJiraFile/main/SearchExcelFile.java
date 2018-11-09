package com.radrian.MainProject.WorkingWithJiraFile.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.streaming.SXSSFRow.CellIterator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SearchExcelFile {
	private final String ORIGINAL_ESTIMATE = "Estimación original";
	private final String RESPONSIBLE = "Responsable";
	private final byte HEADER_ROW = 0;

	private String fileRoute = "";
	private Iterator<Row> rowIterator = null;
	private HashMap<String, Double> empleadosHashMap = new HashMap<String, Double>();
	private String empleadoStringName = "";
	private double empleadoOriginalEstimate = 0.0f;
	private byte columnOriginalEstimate = -1;
	private byte columnResponsible = -1;

	private void setEmpleadoStringName(String stringCellValue) {
		this.empleadoStringName = stringCellValue;
	}

	private void setEmpleadoOriginalEstimate(Double cellNumericCellValue) {
		this.empleadoOriginalEstimate = Double.valueOf(cellNumericCellValue);
	}

	private double convertToDays(double doublValueToDays) {
		return (doublValueToDays / 3600 / 8);
	}
	
	private double convertToHours(double dobleValueToHours) {
		return (dobleValueToHours/3600);
	}
	
	private void printHashMapValues() {
		for (HashMap.Entry<String, Double> entry : empleadosHashMap.entrySet()) {
			System.out.println("Días estimados del empleado " + entry.getKey() + " = \n"
					+ convertToDays(entry.getValue()) + " días. (" + convertToHours(entry.getValue()) + " horas).");
		}
	}

	/**
	 * This method requests the file route and file name and returns the resultant
	 * string
	 */
	private void fetchFile() {
		// here goes a method that requests the files to the user, using JPane.
		String fileName = "JIRA_resumen.xlsx";
		String fileRoute = "C:\\Users\\RAdrian\\Documents\\ExcelTesterFiles\\";
		this.fileRoute = fileRoute + fileName;
	}

	/**
	 * after the file route being set, it makes the instances necessary to set the
	 * rowIterator.
	 */
	private void setIteratorRow() {
		/*
		 * This method is only interested in obtaining the rowIterator which, in this
		 * case, is the only object needed to work on the table, do I need to make more
		 * methods for this method?
		 */
		try (FileInputStream file = new FileInputStream(new File(fileRoute))) {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void iterateHeaderRow(Iterator<Cell> cellIterator) {

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if (columnResponsible >= 0 && columnOriginalEstimate >= 0) {
				break;
			}
			if (CellType.STRING.equals(cell.getCellType())) {
				if (RESPONSIBLE.equals(cell.getStringCellValue())) {
					columnResponsible = (byte) cell.getColumnIndex();
				} else if (ORIGINAL_ESTIMATE.equals(cell.getStringCellValue())) {
					columnOriginalEstimate = (byte) cell.getColumnIndex();
				} else {
					continue;
				}
			}
		}
	}
	
	private void manageHashMapAddition(int cellCollumnIndex) {
		if (!empleadoStringName.equals("") && (columnOriginalEstimate == cellCollumnIndex)) {
			if (!empleadosHashMap.containsKey(empleadoStringName)) {
				empleadosHashMap.put(empleadoStringName, empleadoOriginalEstimate);
			} else {
				empleadosHashMap.put(empleadoStringName, (empleadosHashMap.get(empleadoStringName) + empleadoOriginalEstimate));
			}
		}
	}
	
	private void iterateBodyRows(Iterator<Cell> cellIterator) {
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if (columnOriginalEstimate < cell.getColumnIndex()) {
				break;//this break was a continue, maybe change it back if something weird happens.
			} else if (columnResponsible == cell.getColumnIndex()) {
				setEmpleadoStringName(cell.getStringCellValue());
			} else if (columnOriginalEstimate == cell.getColumnIndex()) {
				setEmpleadoOriginalEstimate(Double.valueOf(cell.getNumericCellValue()));
			} else {
				continue;
			}
			manageHashMapAddition(cell.getColumnIndex());
		}
	}

	private void iterateThroughRows() {
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			if(HEADER_ROW == row.getRowNum()) {
				iterateHeaderRow(cellIterator);
			} else {
				iterateBodyRows(cellIterator);
			}

		}
	}

	private void execute() {
		fetchFile();
		setIteratorRow();
		iterateThroughRows();
		printHashMapValues();
	}
	
	public static void main(String[] args) {
		SearchExcelFile sef = new SearchExcelFile();
		sef.execute();
	}
}
