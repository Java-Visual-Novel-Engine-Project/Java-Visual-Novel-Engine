package com.marcel;

import static com.marcel.Util.*;

import java.awt.*;
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
            puts("File Not Found!");
            return null;
        }

        String strData = new String(binData, StandardCharsets.UTF_8);

        return ReadConfigString(strData);
    }

    private static int CheckComments(String data, int cursorPosition)
    {
        char current = data.charAt(cursorPosition);

        if (current == '(') {

            cursorPosition = data.indexOf(')', cursorPosition);
            cursorPosition++;
            return cursorPosition;
        }

        if (current == '\"')
        {
            current = ' ';
            cursorPosition++;
            while (current != '\"')
            {
                current = data.charAt(cursorPosition);
                if (current == '\\')
                    cursorPosition++;
                cursorPosition++;
            }

            return cursorPosition;
        }

        if (current == '/') {

            if (data.charAt(cursorPosition + 1) == '/') {

                cursorPosition = data.indexOf('\n', cursorPosition);
                cursorPosition++;
                return cursorPosition;

            } else if (data.charAt(cursorPosition + 1) == '*') {

                int starPosition = data.indexOf('*', cursorPosition + 2);

                if (data.charAt(starPosition + 1) == '/') {

                    cursorPosition = starPosition + 1;
                    cursorPosition++;
                    return cursorPosition;
                }

            }
        }

        return cursorPosition;
    }


    private static String validVarChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789-äöü";

    public static List<ConfigTokens.ConfigObject> ReadConfigString(String data)
    {
        List<ConfigTokens.ConfigObject> objectList = new ArrayList<ConfigTokens.ConfigObject>();

        int contentSize = data.length();
        int cursorPosition = 0;

        //CT. new ConfigLabelObject("Test_Label_2", __);

        //('[', '/', '(')

        while (cursorPosition < contentSize) {

            char current = data.charAt(cursorPosition);


            {
                int tempCursorPosition = CheckComments(data, cursorPosition);
                if (cursorPosition != tempCursorPosition)
                {
                    cursorPosition = tempCursorPosition;
                    continue;
                }
            }


            if (current == '[') {
                int labelEndPosition = data.indexOf(']', cursorPosition);

                String label = data.substring(
                        cursorPosition + 1,
                        labelEndPosition
                );

                //puts();

                cursorPosition = labelEndPosition + 1;


                int layer = 0, startBracket = 0, endBracket = 0;

                while (cursorPosition < contentSize)
                {
                    current = data.charAt(cursorPosition);

                    if (current == '{')
                    {
                        layer++;
                        if (layer == 1)
                            startBracket = cursorPosition;
                        cursorPosition++;
                        continue;
                    }
                    if (current == '}')
                    {
                        layer--;
                        if (layer == 0)
                        {
                            endBracket = cursorPosition;
                            break;
                        }
                        cursorPosition++;
                        continue;
                    }

                    {
                        int tempCursorPosition = CheckComments(data, cursorPosition);
                        if (cursorPosition != tempCursorPosition)
                        {
                            cursorPosition = tempCursorPosition;
                            continue;
                        }
                    }
                    cursorPosition++;
                }

                puts("LABEL: \""+ label + "\"");
                puts("BRACKET DATA:\n<" + data.substring(startBracket + 1, endBracket) + ">\n");

                List<ConfigTokens.ConfigObject> tempList = ReadConfigString(data.substring(startBracket + 1, endBracket));
                ConfigTokens.ConfigLabelObject labelObject = CT.new ConfigLabelObject(label, tempList.toArray(new ConfigTokens.ConfigObject[0]));

                cursorPosition++;

                continue;
            }

            if (current == '=')
            {
                int equalSignPosition = cursorPosition;
                cursorPosition--;
                while (validVarChars.indexOf(data.charAt(cursorPosition)) == -1)
                    cursorPosition--;

                int varNameEnd = cursorPosition;
                while (validVarChars.indexOf(data.charAt(cursorPosition)) != -1)
                    cursorPosition--;
                int varNameStart = cursorPosition;

                String varName = data.substring(varNameStart, varNameEnd+1);
                puts("VARNAME: " + varName);

                cursorPosition = equalSignPosition;
                cursorPosition++;
                while (validVarChars.indexOf(data.charAt(cursorPosition)) == -1)
                    cursorPosition++;
                int valueStart = cursorPosition;

                char valueChar = data.charAt(valueStart);
                if (valueChar == '\"')
                {
                    // String
                    current = ' ';
                    cursorPosition++;
                    while (current != '\"')
                    {
                        current = data.charAt(cursorPosition);
                        if (current == '\\')
                            cursorPosition++;
                        cursorPosition++;
                    }
                    int valueEnd = cursorPosition;

                    String value = data.substring(valueStart+1, valueEnd); // get string data
                    ConfigTokens.ConfigVariableObject obj = CT.new ConfigVariableObject( // make new VariableObject with a VariableStringObject with the variable data
                            varName,
                            CT.new ConfigVariableString(value)
                    );
                    objectList.add(obj); // add the object to the actual ObjectThing List
                }
                else if ("-.0123456789".indexOf(valueChar) != -1)
                {
                    // Probably Int or Double

                }
                else if (valueChar == '$')
                {
                    // Reference

                }
                else if (valueChar == '[')
                {
                    // Array

                }
                else if (valueChar == '{')
                {
                    // Dictionary
                    // we can pass the content between the {} into the function again like in the label thing

                }
                else
                {
                    String value = "";

                    // get the word like the varname

                    if (value.equals("true"))
                    {
                        // True Boolean
                    }
                    else if (value.equals("false"))
                    {
                        // Fals Boolean
                    }
                    else
                    {
                        // Big nono
                    }
                }
            }

            //puts();
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
            puts(
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
            puts(
                    MessageFormat.format(
                            "{0}[{1}]\n{0}'{'",
                            "   ".repeat(indentlevel),
                            ((ConfigTokens.ConfigLabelObject) obj).label
                    )
            );

            for (ConfigTokens.ConfigObject currentObject : ((ConfigTokens.ConfigLabelObject) obj).objects)
                PrintToken(currentObject, indentlevel + 1);

            puts(
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
