package se.matzlarsson.cragglez.util;

import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.util.StringConverter;

import java.util.function.Supplier;

public class PropertyUtil {

    private static final StringConverter<Number> strToNum = new StringConverter<>() {
        @Override
        public String toString(Number num) { return num == null ? "null" : num.toString(); }

        @Override
        public Number fromString(String str) { return str == null || str.equals("null") ? null : Integer.valueOf(str); }
    };

    public static StringConverter<Number> stringToNumConverter(){
        return strToNum;
    }

    public static StringBinding multipleBound(Observable[] bindings, Supplier<String> func){
        return new StringBinding() {
            { super.bind(bindings); }

            @Override
            protected String computeValue() {
                return func.get();
            }
        };
    }
}
