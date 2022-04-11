package com.marcel;

import static com.marcel.Util.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        //puts("Hello, World! aaaaa");

        List<ConfigTokens.ConfigObject> data = ConfigReader.ReadConfigFile("Test Visual Novel/config.cfg");

        ConfigReader.PrintTokens(data);

        //String path = ((ConfigTokens.ConfigVariableString)ConfigReader.GetConfigValue("Settings.Paths.Settings_File", data, true)).value;
        //puts("SETTINGS PATH : \"" + path + "\".");
        //puts(ConfigReader.GetConfigValue("Settings.Array", data, true));
        //puts(ConfigReader.GetConfigValue("Settings.Settings_Path", data, false));
        //puts(ConfigReader.GetConfigValue("Settings.Settings_Path", data, true));

        //ConfigReader.WriteConfigFile("test.cfg", data);
    }
}


