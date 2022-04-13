package com.marcel;

import com.marcel.RACF.ConfigFile;

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

		ConfigFile file = new ConfigFile("Test Visual Novel/config.racf");

		file.DisplayTokens();

		puts("TESTING: " + file.GetConfigValueString("Settings.test"));








	}

}
