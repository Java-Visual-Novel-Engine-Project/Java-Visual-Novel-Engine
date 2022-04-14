package com.marcel;

import java.util.HashMap;
import java.awt.Color;

import static com.marcel.ButtonObject.ButtonParams.Names.*;
import static com.marcel.Params.*;

class ButtonObject extends SceneObject
{
    public static class ButtonParams
    {
        enum Names { NAME, TOP_LEFT_POS, SIZE, LAYER_ORDER, LABEL, BG_COLOR, TEXT_SIZE, TEXT_COLOR, THICKNESS }

        public static Param name(String name)          { return param(NAME, name); }
        public static Param topLeftPos(Point point)    { return param(TOP_LEFT_POS, point); }
        public static Param size(Size size)            { return param(SIZE, size); }
        public static Param layerOrder(int layerOrder) { return param(LAYER_ORDER, layerOrder); }
        public static Param label(String label)        { return param(LABEL, label); }
        public static Param bgColor(Color bgColor)     { return param(BG_COLOR, bgColor); }
        public static Param textSize(double textSize)  { return param(TEXT_SIZE, textSize); }
        public static Param textColor(Color textColor) { return param(TEXT_COLOR, textColor); }
        public static Param thickness(int thickness)   { return param(THICKNESS, thickness); }
    }

    public static HashMap<Point, ButtonObject> PosInstMap = new HashMap<Point, ButtonObject>();

    public String label;

    public Color bgColor;

    public double textSize;
    public Color textColor;

    public int thickness;

    public ButtonObject(Param... params)
    {
        this.name = getParam(params, NAME, null);
        this.topLeftPos = getParam(params, TOP_LEFT_POS, null);
        this.size = getParam(params, SIZE, null);
        this.layerOrder = getParam(params, LAYER_ORDER, null);

        this.label = getParam(params, LABEL, null);
        this.bgColor = getParam(params, BG_COLOR, null);
        this.textSize = getParam(params, TEXT_SIZE, null);
        this.textColor =  getParam(params, TEXT_COLOR, null);
        this.thickness = getParam(params, THICKNESS, null);

    }
}
