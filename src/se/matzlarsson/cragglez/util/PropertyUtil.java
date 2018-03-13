package se.matzlarsson.cragglez.util;

import javafx.util.StringConverter;

public class PropertyUtil {

    private static final StringConverter<Number> strToNum = new StringConverter<Number>() {
        @Override
        public String toString(Number num) { return num == null ? "null" : num.toString(); }

        @Override
        public Number fromString(String str) { return str == null || str.equals("null") ? null : Integer.valueOf(str); }
    };

    public static StringConverter<Number> stringToNumConverter(){
        return strToNum;
    }
}
