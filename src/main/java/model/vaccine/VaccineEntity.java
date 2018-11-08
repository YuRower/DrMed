package model.vaccine;



import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VaccineEntity {

	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VaccineEntity [id=" + id + ", name=" + name + ", typeVaccine=" + typeVaccine
				+ ", medicalContradication=" + medicalContradication + ", reaction=" + reaction + ", age=" + age
				+ ", date=" + date + ", doze=" + doze + ", series=" + series + ", nameOfDrug=" + nameOfDrug
				+ ", reactionLocale=" + reactionLocale + ", reactionGeneral=" + reactionGeneral + "]";
	}

	private int id;
	private StringProperty name;
	private StringProperty typeVaccine;
	private StringProperty medicalContradication;
	
	private StringProperty reaction;
	private StringProperty age;
	private StringProperty date;
	private DoubleProperty doze;
	private StringProperty series;
	private StringProperty nameOfDrug;
	private StringProperty reactionLocale;
	private StringProperty reactionGeneral;

	public VaccineEntity(int id, String name, String typeVaccine, String medicalContradication, String age, String date,
			String reaction, double doze, String series, String nameOfDrug,String reactionLocale,String reactionGeneral) {//should fix via builder
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
		this.reactionLocale = new SimpleStringProperty(reactionLocale);
		this.reactionGeneral = new SimpleStringProperty(reactionGeneral);

	}

	public VaccineEntity() {
		this(0, null, null, null, null, null, null, 0.0, null, null,null, null);
	}

	public VaccineEntity(String name) {
		this(0, name, null, null, null, null, null, 0.0, null, null,null, null);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);;
	}

	public StringProperty reactionProperty() {
		return reaction;
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

	public void setNameOfDrug(String nameOfDrug) {
		this.nameOfDrug.set(nameOfDrug);
	}

	public String getNameOfDrug() {
		return nameOfDrug.get();
	}

	public void setNameOfDrugProperty(StringProperty nameOfDrug) {
		this.nameOfDrug = nameOfDrug;
	}

	public String getDate() {
		return date.get();

	}
	
	public StringProperty reactionLocaleProperty() {
		return reactionLocale;
	}

	public String getReactionLocal() {
		return reactionLocale.get();
	}
	
	public void setReactionLocale(String reactionLocale) {
		this.reactionLocale.set(reactionLocale);
	}

	public String getReactionGeneral() {
		return reactionGeneral.get();
	}

	public StringProperty reactionGeneralProperty() {
		return reactionGeneral;
	}
	
	public void setReactionGeneral(String reactionGeneral) {
		this.reactionGeneral.set(reactionGeneral);
	}
}
