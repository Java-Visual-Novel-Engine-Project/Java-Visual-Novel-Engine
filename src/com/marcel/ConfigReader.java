package com.marcel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class ConfigReader {

    private static ConfigTokens CT = ConfigTokens.instance;

    public static List<ConfigTokens.ConfigObject> ReadConfigFile(String filename)
    {
        List<ConfigTokens.ConfigObject> objectList = new ArrayList<ConfigTokens.ConfigObject>();






        return objectList;
    }

    public static void WriteConfigFile(String filename, List<ConfigTokens.ConfigObject> objectList)
    {

    }

    public static void PrintTokens(List<ConfigTokens.ConfigObject> objectList)
    {
        for (ConfigTokens.ConfigObject obj : objectList)
        {
            if (obj instanceof ConfigTokens.ConfigLabelObject)
            {
                PrintToken(obj, 0);
            }
        }
    }

    private static void PrintToken(ConfigTokens.ConfigObject obj, int indentlevel)
    {
        if (obj instanceof ConfigTokens.ConfigVariableObject)
        {
            System.out.println(
                    MessageFormat.format(
                            "{0}{1} = {2}",
                            "   ".repeat(indentlevel),
                            ((ConfigTokens.ConfigVariableObject) obj).name,
                            ((ConfigTokens.ConfigVariableObject) obj).variable
                    )
            );
        }
        else if (obj instanceof ConfigTokens.ConfigLabelObject)
        {
            System.out.println(
                    MessageFormat.format(
                            "{0}[{1}]\n{0}'{'",
                            "   ".repeat(indentlevel),
                            ((ConfigTokens.ConfigLabelObject) obj).label
                    )
            );

            for (ConfigTokens.ConfigObject currentObject : ((ConfigTokens.ConfigLabelObject) obj).objects)
                PrintToken(currentObject, indentlevel + 1);

            System.out.println(
                    MessageFormat.format(
                            "{0}'}'",
                            "   ".repeat(indentlevel)
                    )
            );
        }
    }


}




/*objectList.add(
                CT.new ConfigLabelObject(
                        "Test_Label",
                        new ConfigTokens.ConfigObject[]
                                {
                                        CT. new ConfigVariableObject(
                                                "Test_Variable",
                                                CT.new ConfigVariableString("bruh")
                                        ),
                                        CT. new ConfigVariableObject(
                                                "What's_9_plus_10",
                                                CT.new ConfigVariableDouble(21.0)
                                        ),
                                        CT. new ConfigLabelObject(
                                                "Test_Label_2",
                                                new ConfigTokens.ConfigObject[]
                                                        {
                                                                CT. new ConfigVariableObject(
                                                                        "Test_Variable_2",
                                                                        CT.new ConfigVariableBoolean(false)
                                                                ),
                                                                CT. new ConfigVariableObject(
                                                                        "Test_Array",
                                                                        CT.new ConfigVariableArray(
                                                                                new ConfigTokens.ConfigVariableObjectType[]
                                                                                        {
                                                                                                CT.new ConfigVariableString("bruh"),
                                                                                                CT.new ConfigVariableInteger(10)
                                                                                        }
                                                                        )
                                                                )
                                                        }
                                        )
                                }
                        )
        );*/
