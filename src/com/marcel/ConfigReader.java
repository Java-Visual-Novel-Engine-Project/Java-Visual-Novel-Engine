package com.marcel;

import static com.marcel.Util.puts;
import static com.marcel.ConfigTokens.*;

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

    public static List<ConfigObject> ReadConfigFile(String filename)
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

    public static List<ConfigObject> ReadConfigString(String data)
    {
        List<ConfigObject> objectList = new ArrayList<ConfigObject>();

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

                List<ConfigObject> tempList = ReadConfigString(data.substring(startBracket + 1, endBracket));
                ConfigLabelObject labelObject = new ConfigLabelObject(label, tempList.toArray(new ConfigObject[0]));

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
                    ConfigVariableObject obj = new ConfigVariableObject( // make new VariableObject with a VariableStringObject with the variable data
                            varName,
                            new ConfigVariableString(value)
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

    public static void WriteConfigFile(String filename, List<ConfigObject> objectList) {}

    public static void PrintTokens(List<ConfigObject> objectList)
    {
        for (ConfigObject obj : objectList)
        {
            if (obj instanceof ConfigLabelObject)
            {
                PrintToken(obj, 0);
            }
        }
    }

    private static void PrintToken(ConfigObject obj, int indentlevel)
    {
        if (obj instanceof ConfigVariableObject)
        {
            puts(
                    MessageFormat.format(
                            "{0}{1} = {2}",
                            "   ".repeat(indentlevel),
                            ((ConfigVariableObject) obj).name,
                            ((ConfigVariableObject) obj).variable
                    )
            );
        }
        else if (obj instanceof ConfigLabelObject)
        {
            puts(
                    MessageFormat.format(
                            "{0}[{1}]\n{0}'{'",
                            "   ".repeat(indentlevel),
                            ((ConfigLabelObject) obj).label
                    )
            );

            for (ConfigObject currentObject : ((ConfigLabelObject) obj).objects)
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
);*/
