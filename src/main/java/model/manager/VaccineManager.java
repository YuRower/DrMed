package model.manager;

import model.VaccineTypeLocation;

public class VaccineManager {
	private static VaccineTypeLocation currentType;

	public static VaccineTypeLocation getCurrentVaccine() {
		return currentType;
	}

	public static void setCurrentVaccine(VaccineTypeLocation currentType) {
		VaccineManager.currentType = currentType;
	}
}
