package model.vaccine;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class TuberculinTest {

	private IntegerProperty row;
	private StringProperty doze;
	private StringProperty series;
	private LocalDate date;
	
	public StringProperty getDoze() {
		return doze;
	}
	
	public void setDoze(StringProperty doze) {
		this.doze = doze;
	}

	public StringProperty getSeries() {
		return series;
	}
	
	public void setSeries(StringProperty series) {
		this.series = series;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public IntegerProperty getRow() {
		return row;
	}

	public void setRow(IntegerProperty row) {
		this.row = row;
	}

}
