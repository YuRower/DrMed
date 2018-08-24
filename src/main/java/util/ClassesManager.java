package util;

import model.Classes;

public class ClassesManager {

	private static Classes currentClass;
	private static int currentIndex;

	public static Classes getCurrentClass() {
		return currentClass;
	}

	public static int getCurrentIndex() {
		return currentIndex;
	}

	public static void setCurrentClass(Classes currentClass) {
		ClassesManager.currentClass = currentClass;
	}

	public static void setCurrentIndex(int currentIndex) {
		ClassesManager.currentIndex = currentIndex;
	}
}
