package com.radrian.jirareportanalysis.model;

import java.math.BigDecimal;

/**
 * <h1>Empleado</h1> Creates an Empleado type Object. Stores three parameters.
 * Can convert the time parameters into hours and days. Can add new values to
 * existing time values.
 * 
 * @param name              - Employee's name.
 * @param originalEstimate  - Originally considered time.
 * @param remainingEstimate - What remains of the originally considered time.
 * 
 * @author RAdrian
 */
public class Responsible {
	private static final byte LABORAL_DAY_HOURS = 8;
	private static final short SECONDS_IN_ONE_HOUR = 3600;

	private String name;
	private double originalEstimate;
	private double remainingEstimate;

	public Responsible(String name, double originalEstimate) {
		this.name = name;
		this.originalEstimate = originalEstimate;
		this.remainingEstimate = 0;
	}

	public Responsible(String name, double originalEstimate, double remainingEstimate) {
		this(name, originalEstimate);
		this.remainingEstimate = remainingEstimate;
	}

	public void addToOriginalEstimate(double empleadoOriginalEstimate) {
		this.originalEstimate += empleadoOriginalEstimate;
	}

	public void addToRemainingEstimate(double value) {
		this.remainingEstimate += value;
	}

	public String getName() {
		return name;
	}

	public double getOriginalEstimate() {
		return originalEstimate;
	}

	public double getRemainingEstimate() {
		return remainingEstimate;
	}

	public double getOriginalEstimateInHours() {
		return (this.originalEstimate / SECONDS_IN_ONE_HOUR);
	}

	public double getRemainingEstimaInToHours() {
		return (this.remainingEstimate / SECONDS_IN_ONE_HOUR);
	}

	public double getOriginalEstimateInLaboralDays() {
		return getOriginalEstimateInHours() / LABORAL_DAY_HOURS;
	}

	public double getRemainingEstimateInLaboralDays() {
		return getRemainingEstimaInToHours() / LABORAL_DAY_HOURS;
	}

	private String roundToTwoDecimals (double doubleToRound) {
		final byte DECIMALS_TO_ROUND = 2;
		BigDecimal bigDecimal = new BigDecimal(Double.toString(doubleToRound));
		return String.valueOf(bigDecimal.setScale(DECIMALS_TO_ROUND, BigDecimal.ROUND_DOWN));
	}
	
	public String getResponsibleInfo() {
		return (getName() + " | " + roundToTwoDecimals(getOriginalEstimateInLaboralDays()) + " días (" + roundToTwoDecimals(getOriginalEstimateInHours())
				+ " hr)" + " | " + roundToTwoDecimals(getRemainingEstimateInLaboralDays()) + " días (" + roundToTwoDecimals(getRemainingEstimaInToHours())
				+ " hr)\n");
	}

	@Override
	public String toString() {
		return ("Nombre de Empleado: " + name + "\nEstimación original: " + originalEstimate + " segundos ("
				+ Double.toString(getOriginalEstimateInHours()) + " horas, o "
				+ Double.toString(getOriginalEstimateInLaboralDays()) + " días laborales)." + "\nEstimación restante: "
				+ remainingEstimate + " segundos (" + Double.toString(getRemainingEstimaInToHours()) + " horas, o "
				+ Double.toString(getRemainingEstimateInLaboralDays()) + " días laborales).\n");
	}

}
