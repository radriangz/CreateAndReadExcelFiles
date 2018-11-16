package com.radrian.jirareportanalysis.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * <h1>JIRAReportAnalysis
 * <h1>
 * <p>
 * Analyzes a JIRA excel report file, for responsible employees and their
 * original and remaining estimate times.
 * <p>
 * 
 * @author Radrian
 * 
 * 
 */
public class JIRAReportAnalysis {
	private final String ORIGINAL_ESTIMATE_COLUMN_TITLE = "Estimación original";
	private final String REMAINING_ESTIMATE_COLUMN_TITLE = "Estimación Restante";
	private final String RESPONSIBLE_COLUMN_TITLE = "Responsable";

	private String fileRoute;
	private Sheet sheet;
	private HashMap<String, Responsible> empleadosHashMap = new HashMap<String, Responsible>();
	private byte originalEstimateIndex = -1;
	private byte responsibleIndex = -1;
	private byte remainingEstimateIndex = -1;
	private boolean areColumnIndexesAssigned;
	private boolean isAnalysisSuccessfull;

	public JIRAReportAnalysis(String fileRoute) {
		this.fileRoute = fileRoute;
	}

	/**
	 * Gets first sheet from selected file.
	 */
	private void assignSheet() {
		try {
			File initialFile = new File(fileRoute);
			InputStream inputStreamFile = new FileInputStream(initialFile);
			Workbook workbook = WorkbookFactory.create(inputStreamFile);
			sheet = workbook.getSheetAt(0);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Your InputStream was neither an OLE2 stream, nor an OOXML stream",
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			/*
			 * TO DO Implementar un método que al obtener el error "Your InputStream was
			 * neither an OLE2 stream, nor an OOXML stream" reciba el archivo en cuestión y
			 * lo trate como un html, para poder generar un documento excel válido que
			 * podamos analizar.
			 * 
			 * Docs. https://github.com/alanhay/html-exporter
			 * https://shreyanshshah.wordpress.com/2013/11/30/convert-html-table-to-excel-
			 * file-using-jsoup-and-apache-poi/
			 * https://community.atlassian.com/t5/Answers-Developer-Questions/How-to-take-
			 * out-certain-fields-from-excel-sheet-export-in-issue/qaq-p/573065
			 * 
			 */
		}
	}

	/**
	 * Iterates sheet's rows.
	 */
	private void iterateSheetRows() {
		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (!areColumnIndexesAssigned) {
				findColumIndexes(row);
				continue;
			}
			processResponsibleDataFromRow(row);
		}
	}

	/**
	 * Searches for column titles' indexes for the responsible employee, their
	 * original estimate and remaining estimate.
	 * 
	 * @param headerRow
	 */
	private void findColumIndexes(Row headerRow) {
		Iterator<Cell> cellIterator = headerRow.cellIterator();

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();

			if (CellType.STRING.equals(cell.getCellType())) {
				if (RESPONSIBLE_COLUMN_TITLE.equals(cell.getStringCellValue())) {
					responsibleIndex = (byte) cell.getColumnIndex();
				} else if (ORIGINAL_ESTIMATE_COLUMN_TITLE.equals(cell.getStringCellValue())) {
					originalEstimateIndex = (byte) cell.getColumnIndex();
				} else if (REMAINING_ESTIMATE_COLUMN_TITLE.equals(cell.getStringCellValue())) {
					remainingEstimateIndex = (byte) cell.getColumnIndex();
				}
			}

			if (responsibleIndex >= 0 && originalEstimateIndex >= 0 && remainingEstimateIndex >= 0) {
				areColumnIndexesAssigned = true;
				break;
			}
		}
	}
	
	/**
	 * Processes row's data for the responsible employee and summarizes the original
	 * and remaining estimate.
	 * 
	 * @param bodyRow
	 */
	private void processResponsibleDataFromRow(Row bodyRow) {
		Cell responsibleCell = bodyRow.getCell(this.responsibleIndex);
		Cell originalEstimateCell = bodyRow.getCell(this.originalEstimateIndex);
		Cell remainingEstimateCell = bodyRow.getCell(remainingEstimateIndex);

		if (responsibleCell != null && originalEstimateCell != null && !responsibleCell.toString().isEmpty()) {
			String responsibleName = responsibleCell.getStringCellValue();
			double originalEstimate = originalEstimateCell.getNumericCellValue();
			double remainingEstimate = remainingEstimateCell == null ? 0.0
					: remainingEstimateCell.getNumericCellValue();

			if (empleadosHashMap.containsKey(responsibleName)) {
				empleadosHashMap.get(responsibleName).addToOriginalEstimate(originalEstimate);
				empleadosHashMap.get(responsibleName).addToRemainingEstimate(remainingEstimate);
			} else {
				empleadosHashMap.put(responsibleName,
						new Responsible(responsibleName, originalEstimate, remainingEstimate));
			}
		}
	}

	/**
	 * Display analysis results for the JIRA excel report file.
	 */
	public void displayAnalysissResult() {
		if (this.isAnalysisSuccessfull) {
			StringBuilder outputText = new StringBuilder();
			outputText.append(RESPONSIBLE_COLUMN_TITLE).append(" | ").append(ORIGINAL_ESTIMATE_COLUMN_TITLE)
					.append(" | ").append(REMAINING_ESTIMATE_COLUMN_TITLE).append("\n");

			for (HashMap.Entry<String, Responsible> entry : empleadosHashMap.entrySet()) {
				outputText.append(entry.getValue().getResponsibleInfo());
			}
			JOptionPane.showMessageDialog(null, outputText.toString(), "Resultados", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "El análisis no pudo completarse", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Executes analysis for the JIRA report file.
	 * 
	 * @return
	 */
	public boolean executeAnalysis() {
		boolean executed = false;

		assignSheet();

		if (this.sheet != null) {
			iterateSheetRows();
			executed = true;
			this.isAnalysisSuccessfull = true;
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo obtener la hoja para leer los archivos", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		return executed;
	}
}