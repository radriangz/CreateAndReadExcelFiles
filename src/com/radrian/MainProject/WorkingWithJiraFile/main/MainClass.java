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

public class MainClass {
	private static final String ORIGINAL_ESTIMATE = "Estimación original";
	private static final String RESPONSIBLE = "Responsable";
	private static final byte HEADER_ROW = 0;
	private static HashMap<String, Double> empleadosHashMap = new HashMap<String, Double>();
	private static String empleadoStringName = "";
	private static double empleadoOriginalEstimate = 0.0f;

	public static void main(String[] args) {
		String fileName = "JIRA_resumen.xlsx";
		String fileRoute = "C:\\Users\\RAdrian\\Documents\\ExcelTesterFiles\\" + fileName;
		byte columnOriginalEstimate = -1;
		byte columnResponsible = -1;
		// String fileSheet = "Hoja1";

		try (FileInputStream file = new FileInputStream(new File(fileRoute))) {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if (HEADER_ROW == cell.getRowIndex()) {
						if (CellType.STRING.equals(cell.getCellType())) {
							if (RESPONSIBLE.equals(cell.getStringCellValue())) {
								columnResponsible = (byte) cell.getColumnIndex();
							} else if (ORIGINAL_ESTIMATE.equals(cell.getStringCellValue())) {
								columnOriginalEstimate = (byte) cell.getColumnIndex();
							}
						}
						if (columnResponsible >= 0 && columnOriginalEstimate >= 0) {
							break;
						}
					} else {
						if (columnResponsible == cell.getColumnIndex()) {
							empleadoStringName = cell.getStringCellValue();
						} else if (columnOriginalEstimate == cell.getColumnIndex()) {
							empleadoOriginalEstimate = Double.valueOf(cell.getNumericCellValue());
						} else if (columnOriginalEstimate < cell.getColumnIndex()) {
							continue;
						} else {
							continue;
						}
					}
					if (!empleadoStringName.equals("") && columnOriginalEstimate == cell.getColumnIndex()) {
						if (!isDuplicateKey()) {
							addNewEmpleadoToHashMap();
						} else if (isDuplicateKey()) {
							addNewValueToKey();
						}
					}
//					System.out.println("empleadoname: " + empleadoStringName + "\n" +
//							"nuevo valor empleado: " + empleadoOriginalEstimate + "\n" +
//							"celda: " + cell.getColumnIndex());
				}
			}

			printHashMapValues();

			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isDuplicateKey() {
		return empleadosHashMap.containsKey(empleadoStringName);
	}

	private static void addNewEmpleadoToHashMap() {
		empleadosHashMap.put(empleadoStringName, empleadoOriginalEstimate);
	}

	private static void addNewValueToKey() {
		empleadosHashMap.put(empleadoStringName, (empleadosHashMap.get(empleadoStringName) + empleadoOriginalEstimate));
	}

	private static void printHashMapValues() {
		for (HashMap.Entry<String, Double> entry : empleadosHashMap.entrySet()) {
//		    String key = entry.getKey();
//		    Double value = entry.getValue();
			System.out.println("Días estimados del empleado " + entry.getKey() + " = \n"
					+ convertToDays(entry.getValue()) + " días. (" + entry.getValue().toString() + " horas).");
		}
	}

	private static double convertToDays(double doubleValue) {
		return (doubleValue / 3600 / 8);
	}
}
