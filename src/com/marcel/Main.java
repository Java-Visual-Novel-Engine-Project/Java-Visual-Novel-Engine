package com.marcel;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World! aaaaa");

        List<ConfigTokens.ConfigObject> data = ConfigReader.ReadConfigFile("Test Visual Novel/config.cfg");

        ConfigReader.PrintTokens(data);
    }
}


