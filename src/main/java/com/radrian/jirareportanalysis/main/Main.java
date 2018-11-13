package com.radrian.jirareportanalysis.main;

import java.io.File;

import javax.swing.JFileChooser;

import com.radrian.jirareportanalysis.model.JIRAReportAnalysis;

/**
 * @author RAdrian
 *
 */
public class Main {

	/**
	 * This method returns the file route of chosen file.
	 * 
	 */
	private String fetchFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(null);
		File file = fileChooser.getSelectedFile();
		return file.getAbsolutePath();
	}

	/**
	 * Executes this program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		String fileRoute = main.fetchFile();
		JIRAReportAnalysis analysis = new JIRAReportAnalysis(fileRoute);
		analysis.executeAnalysis();
		analysis.displayAnalysissResult();

	}

}
