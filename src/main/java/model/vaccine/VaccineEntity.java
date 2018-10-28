package model.vaccine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VaccineEntity  {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VaccineEntity [id=" + id + ", name=" + name + ", typeVaccine=" + typeVaccine
				+ ", medicalContradication=" + medicalContradication + ", age=" + age + ", date=" + date + ", doze="
				+ doze + ", series=" + series + ", nameOfDrug=" + nameOfDrug + "]";
	}

	private int id;
	private StringProperty name;
	private StringProperty typeVaccine;
	private StringProperty medicalContradication;
	private StringProperty age;
	private StringProperty date;
	private StringProperty reaction;
	private DoubleProperty doze;
	private StringProperty series;
	private StringProperty nameOfDrug;
	private Map<String, Integer> map;   // mutable field
	public VaccineEntity(int id, String name, String typeVaccine,String medicalContradication,String age,
			String date ,String reaction,double doze,String series,String nameOfDrug) {
		this.id = id;
		this.typeVaccine = new SimpleStringProperty(typeVaccine);
		this.name = new SimpleStringProperty(name);
		this.medicalContradication = new SimpleStringProperty(medicalContradication);
		this.age = new SimpleStringProperty(age);
		this.date = new SimpleStringProperty(date);
		this.reaction = new SimpleStringProperty(reaction);
		this.doze = new SimpleDoubleProperty(doze);
		this.nameOfDrug = new SimpleStringProperty(nameOfDrug);
		this.series = new SimpleStringProperty(series);

	}

	public VaccineEntity() {
		this(0, null, null, null, null, null, null, 0.0, null, null);
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StringProperty namePropert() {
		return name;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	public StringProperty reactionProperty() {
		return typeVaccine;
	}

	public void setReaction(String reaction) {
		this.reaction.set(reaction);
	}

	public String getReaction() {
		return reaction.get();
	}
	public StringProperty typeVaccineProperty() {
		return typeVaccine;
	}

	public void setTypeVaccine(String typeVaccine) {
		this.typeVaccine.set(typeVaccine);
	}

	public String getTypeVaccine() {
		return typeVaccine.get();
	}

	public void setTypeVaccineProperty(StringProperty typeVaccine) {
		this.typeVaccine = typeVaccine;
	}

	public StringProperty medicalContradicationProperty() {
		return medicalContradication;
	}

	public void setMedicalContradication(String medicalContradication) {
		this.medicalContradication.set(medicalContradication);
	}

	public String getMedicalContradication() {
		return medicalContradication.get();
	}

	public void setMedicalContradicationProperty(StringProperty medicalContradication) {
		this.medicalContradication = medicalContradication;
	}

	public StringProperty ageProperty() {
		return age;
	}

	public void setAge(String age) {
		this.age.set(age);
	}

	public String getAge() {
		return age.get();
	}

	public void setAgeProperty(StringProperty age) {
		this.age = age;
	}

	public StringProperty dateProperty() {
		return date;
	}

	public void setDate(StringProperty date) {
		this.date = date;
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public DoubleProperty dozeProperty() {
		return doze;
	}

	public Double getDoze() {
		return doze.get();
	}

	public void setDoze(Double doze) {
		this.doze.set(doze);
	}

	public void setDozeProperty(DoubleProperty doze) {
		this.doze = doze;
	}

	public StringProperty seriesProperty() {
		return series;
	}

	public void setSeries(String series) {
		this.series.set(series);
	}

	public String getSeries() {
		return series.get();
	}

	public void setSeriesProperty(StringProperty series) {
		this.series = series;
	}

	public StringProperty nameOfDrugProperty() {
		return nameOfDrug;
	}

	public void setNameOfDrug(String series) {
		this.series.set(series);
	}

	public String getNameOfDrug() {
		return nameOfDrug.get();
	}

	public void setNameOfDrugProperty(StringProperty nameOfDrug) {
		this.nameOfDrug = nameOfDrug;
	}

	//@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public String getDate() {
		return date.get();

	}
	/*@Override
	public VaccineEntity clone() throws CloneNotSupportedException {
		VaccineEntity vaccine = (VaccineEntity) super.clone();

		// primitive fields are ignored, as their content is already copied
		// immutable fields like String are ignored

		// create new objects for any non-primitive, mutable fields
		vaccine.map = new HashMap<>();

		return vaccine;		
	}*/
}
