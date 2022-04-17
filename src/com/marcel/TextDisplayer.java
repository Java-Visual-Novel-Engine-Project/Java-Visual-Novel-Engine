package com.marcel;

public class TextDisplayer {

    public static double defaultTextSpeed = 50;
    public double passedTime;
    public double speed;
    public String text;


    public TextDisplayer()
    {
        passedTime = 0;
        speed = defaultTextSpeed;
        text = "";
    }

    public TextDisplayer(String text)
    {
        passedTime = 0;
        speed = defaultTextSpeed;
        this.text = text;
    }

    public TextDisplayer(String text, double speed)
    {
        passedTime = 0;
        this.speed = speed;
        this.text = text;
    }


    public void UpdateTime(double deltaTime)
    {
        passedTime += deltaTime;
    }

}
