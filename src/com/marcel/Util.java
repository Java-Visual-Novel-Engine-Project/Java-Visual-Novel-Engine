package com.marcel;

public class Util {

  public static void puts(Object... args) {
	  System.out.println(args[0]);
  }

  private static String tempString = "Test Visual Novel/";

  public static String getPath(String path) {return tempString + path;}

}
