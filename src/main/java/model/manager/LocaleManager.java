package model.manager;


import java.util.Locale;

import model.Lang;

public class LocaleManager{
	public static final String BUNDLES_FOLDER = "property.text";

    public static final Locale RU_LOCALE = new Locale("ru","RU");
    public static final Locale UA_LOCALE = new Locale("uk","UA");
    public static final Locale EN_LOCALE = new Locale("en","EN");


    private static Lang currentLang;

    public static Lang getCurrentLang() {
        return currentLang;
    }

    public static void setCurrentLang(Lang currentLang) {
        LocaleManager.currentLang = currentLang;
    }
}
