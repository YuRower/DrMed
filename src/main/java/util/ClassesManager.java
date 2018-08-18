package util;

import java.util.Locale;

import model.Classes;


public class ClassesManager {



    private static Classes currentClass;

    public static Classes getCurrentClass() {
        return currentClass;
    }

    public static void setCurrentClass(Classes currentClass) {
    	ClassesManager.currentClass = currentClass;
    }
}
