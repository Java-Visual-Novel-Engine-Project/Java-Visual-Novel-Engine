package com.marcel;

import java.util.List;
import com.marcel.ConfigErrors.*;
import  com.marcel.ConfigFile;

import static com.marcel.Util.puts;

public class Main {

	public static void main(String[] args)
  {
		try
	  {
			realMain();
		}
		catch (Exception e)
		{
			puts("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void realMain() throws Exception
	{
		puts("Starting...");

		ConfigFile file = new ConfigFile("Test Visual Novel/config.cfg");

		file.DisplayTokens();

		puts("TESTING: " + file.GetConfigValueString("Settings.test"));
	}

}
