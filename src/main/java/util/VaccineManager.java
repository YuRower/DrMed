package util;

import model.VaccinationType;

public class VaccineManager {
	private static VaccinationType currentType;

	public static VaccinationType getCurrentVaccine() {
		return currentType;
	}

	public static void setCurrentVaccine(VaccinationType currentType) {
		VaccineManager.currentType = currentType;
	}
}
