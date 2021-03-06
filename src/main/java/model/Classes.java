package model;

import java.util.List;

import javafx.collections.FXCollections;

public class Classes {
	private int index;
	private String schoolClass;
	public static List<Person> classListData = FXCollections.observableArrayList();

	public Classes(int index, String schoolClass, List<Person> classData) {
		classListData.addAll(classData);
		this.schoolClass = schoolClass;
		this.index = index;
	}

	public Classes() {
	}

	@Override
	public String toString() {
		return schoolClass;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(String schoolClass) {
		this.schoolClass = schoolClass;
	}

}
