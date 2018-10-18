package model.vaccine;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VaccineEntity {

	private int id;
	private StringProperty name;
	private StringProperty typeVaccine;
	private StringProperty medicalContradication;
	private StringProperty age;
	private LocalDate date;
	private DoubleProperty doze;
	private StringProperty series;
	private StringProperty nameOfDrug;

	public VaccineEntity(int id, String name, String typeVaccine) {
		super();
		this.id = id;
		this.name = new SimpleStringProperty(name);
		this.typeVaccine = new SimpleStringProperty(typeVaccine);

	}

	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	
	public StringProperty getName() {
		return name;
	}

	
	public void setName(StringProperty name) {
		this.name = name;
	}

	
	public StringProperty getTypeVaccine() {
		return typeVaccine;
	}

	
	public void setTypeVaccine(StringProperty typeVaccine) {
		this.typeVaccine = typeVaccine;
	}

	
	public StringProperty getMedicalContradication() {
		return medicalContradication;
	}

	
	public void setMedicalContradication(StringProperty medicalContradication) {
		this.medicalContradication = medicalContradication;
	}

	
	public StringProperty getAge() {
		return age;
	}

	
	public void setAge(StringProperty age) {
		this.age = age;
	}


	public LocalDate getDate() {
		return date;
	}

	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	
	public DoubleProperty getDoze() {
		return doze;
	}

	
	public void setDoze(DoubleProperty doze) {
		this.doze = doze;
	}

	
	public StringProperty getSeries() {
		return series;
	}

	
	public void setSeries(StringProperty series) {
		this.series = series;
	}

	
	public StringProperty getNameOfDrug() {
		return nameOfDrug;
	}

	
	public void setNameOfDrug(StringProperty nameOfDrug) {
		this.nameOfDrug = nameOfDrug;
	}

}
