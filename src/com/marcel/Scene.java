package com.marcel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    public List<SceneObject> objects;

    public Scene()
    {
        objects = new ArrayList<>();
    }

}



class SceneObject
{
    public String name;
    public int x, y, z;
    public int width, height;
}
class ImageObject extends SceneObject
{
    public Image image;
    public int x, y;

    public ImageObject(String name, Image image, int x, int y, int z)
    {
        this.name = name;
        this.image = image;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    public ImageObject(String name, String path, int x, int y, int z)
    {
        this.name = name;
        this.image = new ImageIcon(path).getImage();
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }
}

class ButtonObject extends SceneObject
{
    public Color textCol;
    public Color backgroundCol;
    public String text;
    public double textSize;
    public int thickness;

    public ButtonObject(String name, String text, int textSize, Color textColor, int x, int y, int z, int width, int height, int thickness, Color backgroundCol)
    {
        this.name = name;
        this.backgroundCol = backgroundCol;

        this.text = text;
        this.textSize = textSize;
        this.textCol =  textColor;
        this.thickness = thickness;

        this.x = x;
        this.y = y;
        this.z = z;

        this.width = width;
        this.height = height;
    }
}