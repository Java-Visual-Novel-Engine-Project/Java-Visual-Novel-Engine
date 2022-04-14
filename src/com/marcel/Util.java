package com.marcel;

public class Util {

    public static void puts(Object... args) {
	  System.out.println(args[0]);
  }

    private static String tempString = "Test Visual Novel/";

    public static String getPath(String path) { return tempString + path; }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
