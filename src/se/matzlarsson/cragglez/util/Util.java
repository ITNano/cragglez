package se.matzlarsson.cragglez.util;

public class Util {

    public static int getInt(String s){
        try{
            return Integer.parseInt(s);
        }catch(NumberFormatException nfe){
            return 0;
        }
    }

}
