package com.radrian.mainproject.workingwithjirafile.classes;

/**<h1>Empleado</h1>
 * Creates an Empleado type Object. Stores three parameters.
 * Can convert the time parameters into hours and days.
 * Can add new values to existing time values.
 * 
 * @param name - Employee's name.
 * @param originalEstimate - Originally considered time.
 * @param remainingEstimate - What remains of the originally considered time.
 * 
 * @author RAdrian
 */
public class Empleado {
	private String name;
	private double originalEstimate;
	private double remainingEstimate;
	
	public Empleado(String name, double originalEstimate) {
		this.name = name;
		this.originalEstimate = originalEstimate;
		this.remainingEstimate = 0;
	}

	public Empleado(String name, double originalEstimate, double remainingEstimate) {
		this(name, originalEstimate);
		this.remainingEstimate = remainingEstimate;
	}

	public void addToOriginalEstimate(float value) {
		this.originalEstimate += value;
	}
	
	public void addToRemainingEstimate(float value) {
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

	public float convertToHours(float secondsToHours) {
		return (secondsToHours/3600);
	}
	
	public float convertToLaboralDays(float secondsToDays) {
		return (convertToHours(secondsToDays)/8);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("\nNombre de Empleado: " + name + 
				"\nEstimación original: " + originalEstimate + 
				"\nEstimación restante: " + remainingEstimate);
	}

}
