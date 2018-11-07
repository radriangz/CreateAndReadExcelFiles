package com.radrian.MainProject.WorkingWithJiraFile.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainClass {
	public static void main(String[] args) {
		String fileName = "JIRA_resumen.xlsx";
		String fileRoute = "C:\\Users\\RAdrian\\Documents\\ExcelTesterFiles\\" + fileName;
		//String fileSheet = "Hoja1";
		
		try(FileInputStream file = new FileInputStream(new File(fileRoute))) {
			XSSFWorkbook workBook = new XSSFWorkbook(file);
			XSSFSheet sheet = workBook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			
			Row row;
			
			
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
