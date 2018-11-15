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
	private boolean areColumnIndexesAssigned = false;

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
//		DataFormatter dataFormatter = new DataFormatter();

		Cell responsibleCell = bodyRow.getCell(this.responsibleIndex);
		Cell originalEstimateCell = bodyRow.getCell(this.originalEstimateIndex);
		Cell remainingEstimateCell = bodyRow.getCell(remainingEstimateIndex);

		if (responsibleCell != null && originalEstimateCell != null) {
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
		StringBuilder outputText = new StringBuilder();
		outputText.append(RESPONSIBLE_COLUMN_TITLE).append(" | ").append(ORIGINAL_ESTIMATE_COLUMN_TITLE).append(" | ")
				.append(REMAINING_ESTIMATE_COLUMN_TITLE).append("\n");

		for (HashMap.Entry<String, Responsible> entry : empleadosHashMap.entrySet()) {
			outputText.append(entry.getValue().getResponsibleInfo());
		}
		JOptionPane.showMessageDialog(null, outputText.toString(), "Resultados", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Executes analysis for the JIRA report file.
	 */
	public void executeAnalysis() {
		if (this.sheet != null) {
			assignSheet();
			iterateSheetRows();
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo obtener la horja para leer los archivos", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}
}
