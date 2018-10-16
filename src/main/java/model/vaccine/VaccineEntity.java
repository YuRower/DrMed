package model.vaccine;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

public class VaccineEntity {
	
	private int id ; 
	private StringProperty name; 
	private StringProperty typeVaccine; 
	private StringProperty medicalContradication;
	private StringProperty age ; 
	private LocalDate date;
	private DoubleProperty doze;
	private StringProperty series;
	private StringProperty nameOfDrug;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public StringProperty getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(StringProperty name) {
		this.name = name;
	}
	/**
	 * @return the typeVaccine
	 */
	public StringProperty getTypeVaccine() {
		return typeVaccine;
	}
	/**
	 * @param typeVaccine the typeVaccine to set
	 */
	public void setTypeVaccine(StringProperty typeVaccine) {
		this.typeVaccine = typeVaccine;
	}
	/**
	 * @return the medicalContradication
	 */
	public StringProperty getMedicalContradication() {
		return medicalContradication;
	}
	/**
	 * @param medicalContradication the medicalContradication to set
	 */
	public void setMedicalContradication(StringProperty medicalContradication) {
		this.medicalContradication = medicalContradication;
	}
	/**
	 * @return the age
	 */
	public StringProperty getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(StringProperty age) {
		this.age = age;
	}
	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	/**
	 * @return the doze
	 */
	public DoubleProperty getDoze() {
		return doze;
	}
	/**
	 * @param doze the doze to set
	 */
	public void setDoze(DoubleProperty doze) {
		this.doze = doze;
	}
	/**
	 * @return the series
	 */
	public StringProperty getSeries() {
		return series;
	}
	/**
	 * @param series the series to set
	 */
	public void setSeries(StringProperty series) {
		this.series = series;
	}
	/**
	 * @return the nameOfDrug
	 */
	public StringProperty getNameOfDrug() {
		return nameOfDrug;
	}
	/**
	 * @param nameOfDrug the nameOfDrug to set
	 */
	public void setNameOfDrug(StringProperty nameOfDrug) {
		this.nameOfDrug = nameOfDrug;
	}

}
