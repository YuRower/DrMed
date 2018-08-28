package model;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", street=" + street + ", postalCode="
				+ postalCode + ", city=" + city + ", birthday=" + birthday + ", currentLocale=" + currentLocale + "]";
	}

	private final static Logger LOGGER = Logger.getLogger(Person.class);
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty street;
	private IntegerProperty postalCode;
	private StringProperty city;
	private StringProperty birthday;
	Locale currentLocale = Locale.getDefault();

	public Person() {
		this(null, null);
	}

	public Person(String firstName, String lastName) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.street = new SimpleStringProperty("some street");
		this.postalCode = new SimpleIntegerProperty(1234);
		this.city = new SimpleStringProperty("some city");
		this.birthday = new SimpleStringProperty(
				String.valueOf(DateFormat.getDateInstance(DateFormat.DEFAULT, currentLocale)));
		LOGGER.debug(birthday + " date of birthday ");
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		System.out.println("set");
		System.out.println(firstName);
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
		LOGGER.debug(postalCode + " d setPostalCode ");

		this.postalCode.set(postalCode);
	}

	public IntegerProperty postalCodeProperty() {
		return postalCode;
	}

	public String getCity() {
		return city.get();
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public StringProperty cityProperty() {
		return city;
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
}