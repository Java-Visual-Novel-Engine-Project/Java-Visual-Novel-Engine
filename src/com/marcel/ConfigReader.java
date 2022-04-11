package com.marcel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class ConfigReader {

    private static ConfigTokens CT = ConfigTokens.instance;

    public static List<ConfigTokens.ConfigObject> ReadConfigFile(String filename)
    {
        Path path = Paths.get(filename);
        byte[] binData;
        
        try {
           binData = Files.readAllBytes(path);

        } catch (IOException exception) {
            System.out.println("File Not Found!");
            return null;
        }

        String strData = new String(binData, StandardCharsets.UTF_8);

        return ReadConfigString(strData);
    }

    public static List<ConfigTokens.ConfigObject> ReadConfigString(String data)
    {
        List<ConfigTokens.ConfigObject> objectList = new ArrayList<ConfigTokens.ConfigObject>();

        int contentSize = data.length();
        int cursorPosition = 0;

        //CT. new ConfigLabelObject("Test_Label_2", __);

        //('[', '/', '(')

        while (cursorPosition < contentSize) {

            char current = data.charAt(cursorPosition);

            if (current == '(') {

                cursorPosition = data.indexOf(')', cursorPosition);
                cursorPosition++; 
                continue;

            }

            if (current == '/') {

                if (data.charAt(cursorPosition+1) == '/') {

                    cursorPosition = data.indexOf('\n', cursorPosition);
                    cursorPosition++; 
                    continue;

                } else if (data.charAt(cursorPosition+1) == '*') {

                    int starPosition = data.indexOf('*', cursorPosition+2);

                    if (data.charAt(starPosition+1) == '/') {

                        cursorPosition = starPosition + 1;
                        cursorPosition++; 
                        continue;

                    }

                }
            }

            if (current == '[') {
                int labelEndPosition = data.indexOf(']', cursorPosition);
                
                System.out.println(
                    data.substring(
                        cursorPosition + 1,
                        labelEndPosition
                    )
                );

                cursorPosition = labelEndPosition + 1;

                // Handle { }

                continue;
            }
            //System.out.println();
            cursorPosition++;
        }

        return objectList;
    }

    public static void WriteConfigFile(String filename, List<ConfigTokens.ConfigObject> objectList) {}

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
