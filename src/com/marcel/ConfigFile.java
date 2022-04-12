package com.marcel;

import java.util.List;

import static com.marcel.ConfigTokens.*;

import static com.marcel.ConfigReader.ReadConfigFile;
import static com.marcel.ConfigReader.WriteConfigFile;
import static com.marcel.ConfigReader.PrintTokens;
import static com.marcel.ConfigReader.GetValue;

public class ConfigFile {

	List<ConfigObject> objectList;

	public ConfigFile(String filename) throws Exception {
		objectList = ReadConfigFile(filename);
	}

	public ConfigFile(List<ConfigObject> objectList)
	{
		this.objectList = objectList;
	}

	public void ReadFromFile(String filename) throws Exception {
		objectList = ReadConfigFile(filename);
	}

	public void WriteToFile(String filename)
	{
		WriteConfigFile(filename, objectList);
	}

	public void DisplayTokens()
	{
		PrintTokens(objectList);
	}

	public ConfigVariableObjectType GetConfigValue(String configPath, boolean resolveReference)
	{
		return GetValue(configPath, objectList, resolveReference);
	}

	public ConfigVariableObjectType GetConfigValue(String configPath)
	{
		return GetValue(configPath, objectList, true);
	}

	public String GetConfigValueString(String configPath, boolean resolveReference)
	{
		return GetValue(configPath, objectList, resolveReference).toString();
	}

	public String GetConfigValueString(String configPath)
	{
		return GetValue(configPath, objectList, true).genericToString();
	}

}
