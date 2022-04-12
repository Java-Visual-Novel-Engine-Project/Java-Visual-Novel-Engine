## Example

```java
objectList.add(
  ConfigLabelObject(
    "Test_Label",
    new ConfigObject[]
    {
      new ConfigVariableObject(
        "Test_Variable",
        ConfigVariableString("bruh")
      ),
      new ConfigVariableObject(
        "What's_9_plus_10",
        ConfigVariableDouble(21.0)
      ),
      new ConfigLabelObject(
        "Test_Label_2",
        new ConfigObject[]
        {
          new ConfigVariableObject(
            "Test_Variable_2",
            ConfigVariableBoolean(false)
          ),
          new ConfigVariableObject(
            "Test_Array",
            ConfigVariableArray(
              new ConfigVariableObjectType[]
              {
                ConfigVariableString("bruh"),
                ConfigVariableInteger(10)
              }
            )
          )
        }
      )
    }
  )
);
```

```java
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
```
