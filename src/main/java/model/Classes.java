package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Classes {
	private int index;
	private String schoolClass;
	public  List<Person> classListData =FXCollections.observableArrayList();// = new ArrayList<>();
	public List<Person> getClassListData() {
		return classListData;
	}

	private final static Logger LOGGER = Logger.getLogger(Classes.class);
	


	public Classes(int index, String schoolClass, List<Person> classData) {
		this.classListData = classData;
		this.schoolClass = schoolClass;
		this.index = index;
	}

	

	public Classes() {
		// TODO Auto-generated constructor stub
	}



	@Override
	public String toString() {
		return schoolClass;//classListData.toString()  ;
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
