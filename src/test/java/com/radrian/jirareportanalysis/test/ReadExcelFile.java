package com.radrian.jirareportanalysis.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadExcelFile {

	private static void read(String filename) {
		try (InputStream inp = new FileInputStream(filename)) {

			HashMap<String, Double> valores = new HashMap<>();

			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);

			int rowIndex = 1;
			int columnName = 2;
			int columnTime = 4;
			boolean rowHasValue = true;

			while (rowHasValue) {
				Row row = sheet.getRow(rowIndex);
				if (row != null) {
					Cell cellName = row.getCell(columnName);
					Cell cellTime = row.getCell(columnTime);

					if (cellName != null) {
						if (valores.containsKey(cellName.toString())) {
							valores.put(cellName.toString(), valores.get(cellName.toString())
									+ (cellTime != null ? Double.valueOf(cellTime.toString()) : 0));
						} else {
							valores.put(cellName.toString(),
									cellTime != null ? Double.valueOf(cellTime.toString()) : 0);
						}

						rowIndex++;
					}
				} else {
					rowHasValue = false;
				}
			}

			for (Map.Entry<String, Double> entry : valores.entrySet()) {
				System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue() + " horas: "
						+ (entry.getValue() / 3600d) + " días: " + (entry.getValue() / 28800));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String filename = "C:\\Users\\RAdrian\\Documents\\ExcelTesterFiles\\JIRA_(8).xls";
		read(filename);
	}

}
