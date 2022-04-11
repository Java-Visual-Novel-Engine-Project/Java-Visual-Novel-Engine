package com.marcel;

import static com.marcel.Util.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        puts("Hello, World! aaaaa");

        List<ConfigTokens.ConfigObject> data = ConfigReader.ReadConfigFile("Test Visual Novel/config.cfg");

        ConfigReader.PrintTokens(data);
    }
}


