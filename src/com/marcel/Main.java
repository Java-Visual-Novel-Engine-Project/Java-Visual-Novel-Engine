package com.marcel;

import java.util.List;
import com.marcel.ConfigErrors.*;
import  com.marcel.ConfigFile;

import static com.marcel.Util.puts;

public class Main {

    public static void main(String[] args) {
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

        file.PrintTokens();

        puts("TESTING: " + file.GetConfigValueString("Settings.test"));


    }




}

//List<ConfigTokens.ConfigObject> data = ConfigReader.ReadConfigFile("Test Visual Novel/config.cfg");

//ConfigReader.PrintTokens(data);

//puts(((ConfigTokens.ConfigVariableString)ConfigReader.GetConfigValue("Settings.test", data, true)).GetString());
//puts(((ConfigTokens.ConfigVariableString)ConfigReader.GetConfigValue("Settings.test", data, true)).GetOriginalString());


//String path = ((ConfigTokens.ConfigVariableString)ConfigReader.GetConfigValue("Settings.Paths.Settings_File", data, true)).value;
//puts("SETTINGS PATH : \"" + path + "\".");
//puts(ConfigReader.GetConfigValue("Settings.Array", data, true));
//puts(ConfigReader.GetConfigValue("Settings.Settings_Path", data, false));
//puts(ConfigReader.GetConfigValue("Settings.Settings_Path", data, true));

//ConfigReader.WriteConfigFile("test.cfg", data);