package util;


import java.util.Locale;

import model.Lang;

public class LocaleManager{

    public static final Locale RU_LOCALE = new Locale("ru");
    public static final Locale UA_LOCALE = new Locale("uk");

    private static Lang currentLang;

    public static Lang getCurrentLang() {
        return currentLang;
    }

    public static void setCurrentLang(Lang currentLang) {
        LocaleManager.currentLang = currentLang;
    }
}
