package model;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
	
	private final static Logger LOGGER = Logger.getLogger(Person.class);
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty patronymic;
	private StringProperty street;
	private IntegerProperty postalCode;
	private int id;
	private StringProperty birthday;
	private StringProperty phoneNumber;
	Locale currentLocale = Locale.getDefault();

	public Person() {
		this(0,null, null, null, null, 0, null, null);
	}

	public Person(int id,String lastName, String firstName, String patronymic,String street,int postalCode,String birthday,String phoneNumber) {
		this.id=id;
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.patronymic = new SimpleStringProperty(patronymic);
		this.street = new SimpleStringProperty(street);
		this.postalCode = new SimpleIntegerProperty(postalCode);
		this.birthday = new SimpleStringProperty(birthday);
		this.phoneNumber=new SimpleStringProperty(phoneNumber);
		LOGGER.debug(birthday + " date of birthday ");
	}
	public String getPhoneNumber() {
		return phoneNumber.get();
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber.set(phoneNumber);
	}
	public StringProperty phoneNumberProperty() {
		return phoneNumber;
	}
	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	public StringProperty firstNameProperty() {
		return firstName;
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	public StringProperty lastNameProperty() {
		return lastName;
	}

	public String getStreet() {
		return street.get();
	}

	public void setStreet(String street) {
		this.street.set(street);
	}

	public StringProperty streetProperty() {
		return street;
	}

	public int getPostalCode() {
		return postalCode.get();
	}

	public void setPostalCode(int postalCode) {
		this.postalCode.set(postalCode);
	}

	public IntegerProperty postalCodeProperty() {
		return postalCode;
	}

	public String getPatronymic() {
		return patronymic.get();
	}

	public void setPatronymic(String patronymic) {
		this.patronymic.set(patronymic);
	}

	public StringProperty patronymicProperty() {
		return patronymic;
	}

	public StringProperty birthdayProperty() {
		return birthday;
	}

	public String getBirthday() {
		return birthday.get();
	}

	public void setBirthday(String birthday) {
		LOGGER.debug(birthday + " date of birthday ");
		this.birthday.set(birthday);
	}

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
}