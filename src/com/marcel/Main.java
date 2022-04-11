package com.marcel;

import java.util.List;
import com.marcel.ConfigErrors.*;

import static com.marcel.Util.puts;

public class Main {

    public static void main(String[] args) {
        //puts("Hello, World! aaaaa");

        try
        {
            realMain();
        }
        catch (Exception e)
        {
            puts("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        //String path = ((ConfigTokens.ConfigVariableString)ConfigReader.GetConfigValue("Settings.Paths.Settings_File", data, true)).value;
        //puts("SETTINGS PATH : \"" + path + "\".");
        //puts(ConfigReader.GetConfigValue("Settings.Array", data, true));
        //puts(ConfigReader.GetConfigValue("Settings.Settings_Path", data, false));
        //puts(ConfigReader.GetConfigValue("Settings.Settings_Path", data, true));

        //ConfigReader.WriteConfigFile("test.cfg", data);



    }




    public static void realMain() throws Exception {
        List<ConfigTokens.ConfigObject> data = ConfigReader.ReadConfigFile("Test Visual Novel/config.cfg");

        ConfigReader.PrintTokens(data);

    }




}


