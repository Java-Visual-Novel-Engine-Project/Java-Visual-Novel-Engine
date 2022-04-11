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


    private static ConfigLabelObject FindLabel(String name, List<ConfigObject> objectList)
    {
        for (ConfigObject obj : objectList)
            if (obj instanceof  ConfigLabelObject)
                return (ConfigLabelObject) obj;

        return null;
    }


    private static ConfigLabelObject FindLabel(String name, ConfigObject[] objectList)
    {
        for (ConfigObject obj : objectList)
            if (obj instanceof  ConfigLabelObject)
                return (ConfigLabelObject) obj;

        return null;
    }

    public static ConfigVariableObjectType GetConfigValue(String configPath, List<ConfigObject> objectList, boolean resolveReference)
    {
        String[] parts = configPath.split(java.util.regex.Pattern.quote("."));

        ConfigLabelObject currentLabel = FindLabel(parts[0], objectList);

        for (int i = 0; i < parts.length - 2; i++)
            currentLabel = FindLabel(parts[i+1], currentLabel.objects);


        ConfigVariableObjectType var = null;

        for (ConfigObject obj : currentLabel.objects) {
            if (obj instanceof ConfigVariableObject)
                if (((ConfigVariableObject)obj).name.equals(parts[parts.length - 1]))
                    var = ((ConfigVariableObjectType) ((ConfigVariableObject)obj).variable);

        }

        if (resolveReference)
            if (var != null)
                if (var instanceof ConfigVariableReference)
                    var = ConfigReader.GetConfigValue((((ConfigVariableReference)var).variableName), objectList, true);

        // Potentially add indexing!

        return var;
    }





    private static int CheckComments(String data, int cursorPosition, boolean skipString)
    {
        char current = data.charAt(cursorPosition);

        if (current == '(') {

            cursorPosition = data.indexOf(')', cursorPosition);
            cursorPosition++;
            return cursorPosition;
        }

        if (current == '\"' && skipString)
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
                int tempCursorPosition = CheckComments(data, cursorPosition, true);
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
                        int tempCursorPosition = CheckComments(data, cursorPosition, true);
                        if (cursorPosition != tempCursorPosition)
                        {
                            cursorPosition = tempCursorPosition;
                            continue;
                        }
                    }
                    cursorPosition++;
                }

                //puts("LABEL: \""+ label + "\"");
                //puts("BRACKET DATA:\n<" + data.substring(startBracket + 1, endBracket) + ">\n");

                List<ConfigObject> tempList = ReadConfigString(data.substring(startBracket + 1, endBracket));
                ConfigLabelObject labelObject = new ConfigLabelObject(label, tempList.toArray(new ConfigObject[0]));

                objectList.add(labelObject);

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
                int varNameStart = cursorPosition+1;

                String varName = data.substring(varNameStart, varNameEnd+1);
                //puts("VARNAME: " + varName);

                cursorPosition = equalSignPosition;
                cursorPosition++;
                while (validVarChars.indexOf(data.charAt(cursorPosition)) == -1 && "\"${[".indexOf(data.charAt(cursorPosition)) == -1)
                    cursorPosition++;
                int valueStart = cursorPosition;

                char valueChar = data.charAt(valueStart);
                //puts("VAL CHAR: " + valueChar);
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

                    String value = data.substring(valueStart+1, valueEnd-1); // get string data
                    //puts("VALUE: \"" + value + "\"");

                    ConfigVariableObject obj = new ConfigVariableObject( // make new VariableObject with a VariableStringObject with the variable data
                            varName,
                            new ConfigVariableString(value)
                    );
                    objectList.add(obj); // add the object to the actual ObjectThing List

                    continue;
                }
                else if ("-.0123456789".indexOf(valueChar) != -1)
                {
                    // Probably Int or Double
                    cursorPosition++;
                    current = '0';
                    while (".0123456789".indexOf(current) != -1)
                    {
                        current = data.charAt(cursorPosition);
                        cursorPosition++;
                    }
                    int valueEnd = cursorPosition;

                    String value = data.substring(valueStart, valueEnd-1); // get string data
                    //puts("VALUE: \"" + value + "\"");

                    ConfigVariableObject obj;

                    if (value.contains("."))
                    {
                        obj = new ConfigVariableObject( // make new VariableObject with a VariableStringObject with the variable data
                                varName,
                                new ConfigVariableDouble(Double.parseDouble(value))
                        );
                    }
                    else
                    {
                        obj = new ConfigVariableObject( // make new VariableObject with a VariableStringObject with the variable data
                                varName,
                                new ConfigVariableInteger(Integer.parseInt(value))
                        );
                    }

                    objectList.add(obj); // add the object to the actual ObjectThing List

                    continue;
                }
                else if (valueChar == '$')
                {
                    cursorPosition++;

                    while (validVarChars.indexOf(data.charAt(cursorPosition)) != -1 || data.charAt(cursorPosition) == '.')
                        cursorPosition++;


                    int valueEnd = cursorPosition;

                    String value = data.substring(valueStart+1, valueEnd); // get string data
                    //puts("VALUE: \"" + value + "\"");

                    ConfigVariableObject obj = new ConfigVariableObject( // make new VariableObject with a VariableStringObject with the variable data
                            varName,
                            new ConfigVariableReference(value)
                    );
                    objectList.add(obj); // add the object to the actual ObjectThing List

                    continue;
                }
                else if (valueChar == '[')
                {
                    int layer = 0, startBracket = 0, endBracket = 0;

                    while (cursorPosition < contentSize)
                    {
                        current = data.charAt(cursorPosition);

                        if (current == '[')
                        {
                            layer++;
                            if (layer == 1)
                                startBracket = cursorPosition;
                            cursorPosition++;
                            continue;
                        }
                        if (current == ']')
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
                            int tempCursorPosition = CheckComments(data, cursorPosition, true);
                            if (cursorPosition != tempCursorPosition)
                            {
                                cursorPosition = tempCursorPosition;
                                continue;
                            }
                        }
                        cursorPosition++;
                    }

                    //puts("BRACKET DATA:\n<" + data.substring(startBracket + 1, endBracket) + ">\n");

                    List<ConfigVariableObjectType> tempList = ReadConfigStringArray(" "+data.substring(startBracket + 1, endBracket) +" ");
                    //ConfigLabelObject labelObject = new ConfigLabelObject(label, tempList.toArray(new ConfigObject[0]));

                    ConfigVariableObject obj = new ConfigVariableObject(
                            varName,
                            new ConfigVariableArray(tempList.toArray(new ConfigVariableObjectType[0]))
                    );

                    objectList.add(obj);

                    continue;

                }
                else if (valueChar == '{')
                {
                    // Dictionary
                    // we can pass the content between the {} into the function again like in the label thing


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
                            int tempCursorPosition = CheckComments(data, cursorPosition, true);
                            if (cursorPosition != tempCursorPosition)
                            {
                                cursorPosition = tempCursorPosition;
                                continue;
                            }
                        }
                        cursorPosition++;
                    }

                    //puts("BRACKET DATA:\n<" + data.substring(startBracket + 1, endBracket) + ">\n");

                    List<ConfigObject> tempList = ReadConfigString(" "+data.substring(startBracket + 1, endBracket) +" ");
                    //ConfigLabelObject labelObject = new ConfigLabelObject(label, tempList.toArray(new ConfigObject[0]));

                    List<ConfigVariableObject> vars = new ArrayList<>();
                    for (ConfigObject obj_ : tempList)
                    {
                        if (obj_ instanceof ConfigVariableObject)
                            vars.add((ConfigVariableObject) obj_);
                    }

                    ConfigVariableObject obj = new ConfigVariableObject(
                            varName,
                            new ConfigVariableDictionary(vars)
                    );


                    objectList.add(obj);

                    continue;

                }
                else
                {
                    cursorPosition++;
                    current = data.charAt(cursorPosition);
                    while ("truefals".indexOf(current) != -1)
                    {
                        current = data.charAt(cursorPosition);
                        cursorPosition++;
                    }
                    int valueEnd = cursorPosition;

                    String value = data.substring(valueStart, valueEnd-1); // get string data
                    //puts("VALUE: \"" + value + "\"");



                    ConfigVariableObject obj;

                    // get the word like the varname

                    if (value.equals("true"))
                    {
                        obj = new ConfigVariableObject( // make new VariableObject with a VariableStringObject with the variable data
                                varName,
                                new ConfigVariableBoolean(true)
                        );
                    }
                    else if (value.equals("false"))
                    {
                        obj = new ConfigVariableObject( // make new VariableObject with a VariableStringObject with the variable data
                                varName,
                                new ConfigVariableBoolean(false)
                        );
                    }
                    else
                    {
                        return null;
                    }

                    objectList.add(obj); // add the object to the actual ObjectThing List

                    continue;
                }
            }

            //puts();
            cursorPosition++;
        }

        return objectList;
    }





    public static List<ConfigVariableObjectType> ReadConfigStringArray(String data)
    {
        List<ConfigVariableObjectType> objectList = new ArrayList<>();

        int contentSize = data.length();
        int cursorPosition = 0;

        //CT. new ConfigLabelObject("Test_Label_2", __);

        //('[', '/', '(')

        while (cursorPosition < contentSize) {

            char current = data.charAt(cursorPosition);

            {
                int tempCursorPosition = CheckComments(data, cursorPosition, false);
                if (cursorPosition != tempCursorPosition)
                {
                    cursorPosition = tempCursorPosition;
                    continue;
                }
            }

            if (validVarChars.indexOf(data.charAt(cursorPosition)) != -1 || "\"${[".indexOf(data.charAt(cursorPosition)) != -1)
            {
                //while (validVarChars.indexOf(data.charAt(cursorPosition)) == -1 && "\"${[".indexOf(data.charAt(cursorPosition)) == -1)
                    //cursorPosition++;
                int valueStart = cursorPosition;

                char valueChar = data.charAt(valueStart);
                //puts("VAL CHAR: " + valueChar);
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

                    String value = data.substring(valueStart+1, valueEnd-1); // get string data
                    //puts("VALUE: \"" + value + "\"");

                    objectList.add(new ConfigVariableString(value)); // add the object to the actual ObjectThing List


                    continue;
                }
                else if ("-.0123456789".indexOf(valueChar) != -1)
                {
                    // Probably Int or Double
                    cursorPosition++;
                    current = '0';
                    while (".0123456789".indexOf(current) != -1)
                    {
                        current = data.charAt(cursorPosition);
                        cursorPosition++;
                    }
                    int valueEnd = cursorPosition;

                    String value = data.substring(valueStart, valueEnd-1); // get string data
                    //puts("VALUE: \"" + value + "\"");

                    ConfigVariableObject obj;

                    if (value.contains("."))
                        objectList.add(new ConfigVariableDouble(Double.parseDouble(value)));
                    else
                        objectList.add(new ConfigVariableInteger(Integer.parseInt(value)));

                     // add the object to the actual ObjectThing List

                    continue;
                }
                else if (valueChar == '$')
                {
                    cursorPosition++;

                    while (validVarChars.indexOf(data.charAt(cursorPosition)) != -1 || data.charAt(cursorPosition) == '.')
                        cursorPosition++;


                    int valueEnd = cursorPosition;

                    String value = data.substring(valueStart+1, valueEnd); // get string data
                    //puts("VALUE: \"" + value + "\"");

                    objectList.add(new ConfigVariableReference(value)); // add the object to the actual ObjectThing List


                    continue;
                }
                else if (valueChar == '[')
                {
                    int layer = 0, startBracket = 0, endBracket = 0;

                    while (cursorPosition < contentSize)
                    {
                        current = data.charAt(cursorPosition);

                        if (current == '[')
                        {
                            layer++;
                            if (layer == 1)
                                startBracket = cursorPosition;
                            cursorPosition++;
                            continue;
                        }
                        if (current == ']')
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
                            int tempCursorPosition = CheckComments(data, cursorPosition, true);
                            if (cursorPosition != tempCursorPosition)
                            {
                                cursorPosition = tempCursorPosition;
                                continue;
                            }
                        }
                        cursorPosition++;
                    }

                    //puts("BRACKET DATA:\n<" + data.substring(startBracket + 1, endBracket) + ">\n");

                    List<ConfigVariableObjectType> tempList = ReadConfigStringArray(" "+data.substring(startBracket + 1, endBracket) +" ");
                    //ConfigLabelObject labelObject = new ConfigLabelObject(label, tempList.toArray(new ConfigObject[0]));

                    objectList.add(new ConfigVariableArray(tempList.toArray(new ConfigVariableObjectType[0])));

                    continue;

                }
                else if (valueChar == '{')
                {
                    // Dictionary
                    // we can pass the content between the {} into the function again like in the label thing


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
                            int tempCursorPosition = CheckComments(data, cursorPosition, true);
                            if (cursorPosition != tempCursorPosition)
                            {
                                cursorPosition = tempCursorPosition;
                                continue;
                            }
                        }
                        cursorPosition++;
                    }

                    //puts("BRACKET DATA:\n<" + data.substring(startBracket + 1, endBracket) + ">\n");

                    List<ConfigObject> tempList = ReadConfigString(" "+data.substring(startBracket + 1, endBracket) +" ");
                    //ConfigLabelObject labelObject = new ConfigLabelObject(label, tempList.toArray(new ConfigObject[0]));

                    List<ConfigVariableObject> vars = new ArrayList<>();
                    for (ConfigObject obj_ : tempList)
                    {
                        if (obj_ instanceof ConfigVariableObject)
                            vars.add((ConfigVariableObject) obj_);
                    }

                    objectList.add(new ConfigVariableDictionary(vars));

                    continue;

                }
                else
                {
                    cursorPosition++;
                    current = data.charAt(cursorPosition);
                    while ("truefals".indexOf(current) != -1)
                    {
                        current = data.charAt(cursorPosition);
                        cursorPosition++;
                    }
                    int valueEnd = cursorPosition;

                    String value = data.substring(valueStart, valueEnd-1); // get string data
                    //puts("VALUE: \"" + value + "\"");



                    ConfigVariableObject obj;

                    // get the word like the varname

                    if (value.equals("true"))
                        objectList.add(new ConfigVariableBoolean(true));

                    else if (value.equals("false"))
                        objectList.add(new ConfigVariableBoolean(false));

                    else
                    {
                        return null;
                    }
                    // add the object to the actual ObjectThing List

                    continue;
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
        for (int i = 0; i < objectList.size(); i++)
        {
            ConfigObject obj = objectList.get(i);
            if (obj instanceof ConfigLabelObject)
            {
                PrintToken(obj, 0, i == 0);
            }
        }
    }

    private static void PrintToken(ConfigObject obj, int indentlevel, boolean firstLabel)
    {
        if (obj instanceof ConfigVariableObject)
        {
            puts(
                    MessageFormat.format(
                            "{0}{1} = {2}",
                            "\t".repeat(indentlevel),
                            ((ConfigVariableObject) obj).name,
                            ((ConfigVariableObject) obj).variable
                    )
            );
        }
        else if (obj instanceof ConfigLabelObject)
        {
            if (!firstLabel)
                puts("");

            puts(
                    MessageFormat.format(
                            "{0}[{1}]\n{0}'{'",
                            "\t".repeat(indentlevel),
                            ((ConfigLabelObject) obj).label
                    )
            );

            //for (ConfigObject currentObject : ((ConfigLabelObject) obj).objects)
                //PrintToken(currentObject, indentlevel + 1);

            boolean labelBefore = false;

            for (int i = 0; i < ((ConfigLabelObject) obj).objects.length; i++) {
                ConfigObject currentObject = ((ConfigLabelObject) obj).objects[i];
                if (currentObject instanceof ConfigVariableObject)
                    if (labelBefore)
                        puts("");

                PrintToken(currentObject, indentlevel + 1, i == 0);

                if (currentObject instanceof ConfigLabelObject)
                    labelBefore = true;
                else
                    labelBefore = false;
            }


            puts(
                    MessageFormat.format(
                            "{0}'}'",
                            "\t".repeat(indentlevel)
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
