package com.marcel;
import com.marcel.ConfigReader;

import java.util.List;
import com.marcel.ConfigTokens.*;

public class ConfigFile {
    //public String filename;
    List<ConfigObject> objectList;
    public ConfigFile(String filename) throws Exception {
        objectList = ConfigReader.ReadConfigFile(filename);
    }

    public ConfigFile(List<ConfigObject> objectList)
    {
        this.objectList = objectList;
    }

    public void ReadFromFile(String filename) throws Exception {
        objectList = ConfigReader.ReadConfigFile(filename);
    }

    public void WriteToFile(String filename)
    {
        ConfigReader.WriteConfigFile(filename, objectList);
    }

    public void PrintTokens()
    {
        ConfigReader.PrintTokens(objectList);
    }


    public ConfigVariableObjectType GetConfigValue(String configPath, boolean resolveReference)
    {
        return ConfigReader.GetConfigValue(configPath, objectList, resolveReference);
    }

    public ConfigVariableObjectType GetConfigValue(String configPath)
    {
        return ConfigReader.GetConfigValue(configPath, objectList, true);
    }

    public String GetConfigValueString(String configPath, boolean resolveReference)
    {
        return ConfigReader.GetConfigValue(configPath, objectList, resolveReference).toString();
    }

    public String GetConfigValueString(String configPath)
    {
        return ConfigReader.GetConfigValue(configPath, objectList, true).genericToString();
    }

}
