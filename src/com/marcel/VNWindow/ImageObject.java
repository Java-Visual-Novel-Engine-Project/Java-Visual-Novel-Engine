package com.marcel.VNWindow;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageObject extends SceneObject
{
    public Image image;

    public ImageObject(String name, Image image, Point topLeftPos, int layerOrder)
    {
        this.name = name;
        this.image = image;
        this.topLeftPos = topLeftPos;
        this.layerOrder = layerOrder;
        this.size = new Size(image.getWidth(null), image.getHeight(null));
    }

    public ImageObject(String name, String path, Point topLeftPos, int layerOrder)
    {
        this.name = name;
        this.image = new ImageIcon(path).getImage();
        this.topLeftPos = topLeftPos;
        this.layerOrder = layerOrder;
        this.size = new Size(image.getWidth(null), image.getHeight(null));
    }
}
