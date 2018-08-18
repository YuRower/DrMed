package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Classes {
	private int schoolClass;
	private String letter;
	public static ObservableList<Person> classListData;// = new ArrayList<>();
	private final static Logger LOGGER = Logger.getLogger(Classes.class);

	public Classes() {

	}

	

	@Override
	public String toString() {
		return "Classes [schoolClass=" + schoolClass + ", letter=" + letter + ", classListData=" + classListData + "]";
	}



	public Classes(int schoolClass, String letter, List<Person> personData) {
		classListData = FXCollections.observableArrayList();
		classListData.addAll(personData);
		LOGGER.info(classListData.toString()+"Class list");
		this.schoolClass = schoolClass;
		this.letter = letter;
	}

	

	public ObservableList<Person> getClassListData() {
		return classListData;
	}

	public void setClassListData(ObservableList<Person> classListData) {
		this.classListData = classListData;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public int getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(int schoolClass) {
		this.schoolClass = schoolClass;
	}

}
