package model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Classes {
	private int schoolClass;
	private String letter;
	ObservableList<Person> classListData;// = new ArrayList<>();



	public Classes(int i, String string, ObservableList<Person> personData) {
		classListData = FXCollections.observableArrayList();
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
