package com.radrian.MainProject.WorkingWithJiraFile.Classes;

/**<h1>Empleado</h1>
 * Creates an Empleado type Object.
 * 
 * @param name
 * @param originalEstimation - Time originally considered
 * @
 * 
 * @author RAdrian
 */
public class Empleado {
	private String name;
	private int originalEstimate;
	private int remainingEstimate;
	
	public Empleado(String name) {
		this.name = name;
	}

	public Empleado(String name, int originalSeconds, int remainingSeconds) {
		this.name = name;
		this.originalEstimate = originalSeconds;
		this.remainingEstimate = remainingSeconds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOriginalEstimate() {
		return originalEstimate;
	}

	public void setOriginalEstimate(int seconds) {
		this.originalEstimate = seconds;
	}
	
	public int getRemainingEstimate() {
		return remainingEstimate;
	}

	public void setRemainingEstimate(int seconds) {
		this.remainingEstimate = seconds;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("\nNombre de Empleado: " + name + 
				"\nEstimación original: " + originalEstimate + 
				"\nEstimación restante: " + remainingEstimate);
	}

}
