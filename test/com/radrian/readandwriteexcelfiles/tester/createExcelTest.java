package com.radrian.readandwriteexcelfiles.tester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class createExcelTest {

	public static void main(String[] args) {
		String fileName = "ExampleFile.xlsx";
		String fileRoute = "C:\\Users\\RAdrian\\Documents\\ExcelTesterFiles\\" + fileName;
		String sheet = "Sheet01";

		XSSFWorkbook book = new XSSFWorkbook();
		XSSFSheet sheet01 = book.createSheet(sheet);

		// creating the header on excel sheet
		String[] header = new String[] { "Code", "Product", "Price", "Units" };

		// excel sheet content
		String[][] document = new String[][] {
				{ "AP150", "ACCESS POINT TP-LINK TL-WA901ND 450Mbps Wireless N 1RJ45 10-100 3Ant.", "112.00", "50" },
				{ "RTP150", "ROUTER TP-LINK TL-WR940ND 10-100Mbpps LAN WAN 2.4 - 2.4835Ghz", "19.60", "25" },
				{ "TRT300", "TARJETA DE RED TPLINK TL-WN881ND 300Mpbs Wire-N PCI-Exp.", "10.68", "15" },
				{ "TRT300", "DE RED TPLINK TL-WN881ND 300Mpbs Wire-N PCI-Exp.", "10.68", "15" },
				{ "TR0", "DE RED TPLINK TL-WN881ND 300Mpbs Wire-N PCI-Exp.", "10.68", "15" } };

		// Set header text to bold
		CellStyle style = book.createCellStyle();
		Font font = book.createFont();
		font.setBold(true);
		style.setFont(font);

		// Generate data for the document..
		for (int i = 0; i <= document.length; i++) {
			// create the rows
			XSSFRow row = sheet01.createRow(i);
			for (int j = 0; j < header.length; j++) {
				if (i == 0) { // this is only for the header
					XSSFCell cell = row.createCell(j); // creates the cells for the header, with the position
					cell.setCellStyle(style);// adds the style created before
					cell.setCellValue(header[j]);// adds the content
				} else { // creates the content
					XSSFCell cell = row.createCell(j); // creates the cells for the content
					cell.setCellValue(document[i - 1][j]);// adds the content
				}
			}
		}
		
		File file;
		file = new File(fileRoute);
		try (FileOutputStream fileOuS = new FileOutputStream(file)) {
			if (file.exists()) {
				file.delete();
				System.out.println("File deleted.");
			}
			book.write(fileOuS);
			fileOuS.flush();
			fileOuS.close();
			System.out.println("File created.");
			book.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
