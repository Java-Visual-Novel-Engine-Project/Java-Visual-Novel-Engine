package com.marcel;

import java.util.HashMap;
import java.awt.Color;

import static com.marcel.ButtonObject.ButtonParams.Names.*;
import static com.marcel.Params.*;

class ButtonObject extends SceneObject
{
    public static class ButtonParams
    {
        enum Names { NAME, TOP_LEFT_POS, SIZE, LAYER_ORDER, LABEL, BG_COLOR, TEXT_SIZE, TEXT_COLOR, BORDER_COLOR, SELECTED_BORDER_COLOR, THICKNESS, ENFORCE_DIMENSIONS }

        public static Param name(String name)          { return param(NAME, name); }
        public static Param topLeftPos(Point point)    { return param(TOP_LEFT_POS, point); }
        public static Param size(Size size)            { return param(SIZE, size); }
        public static Param layerOrder(int layerOrder) { return param(LAYER_ORDER, layerOrder); }
        public static Param label(String label)        { return param(LABEL, label); }
        public static Param bgColor(Color bgColor)     { return param(BG_COLOR, bgColor); }
        public static Param textSize(double textSize)  { return param(TEXT_SIZE, textSize); }
        public static Param textColor(Color textColor) { return param(TEXT_COLOR, textColor); }
        public static Param borderColor(Color borderColor) { return param(BORDER_COLOR, borderColor); }
        public static Param selectedBorderColor(Color selectedBorderColor) { return param(SELECTED_BORDER_COLOR, selectedBorderColor); }
        public static Param thickness(int thickness)   { return param(THICKNESS, thickness); }
        public static Param enforceDimensions(boolean enforce)   { return param(ENFORCE_DIMENSIONS, enforce); }

    }

    public static HashMap<Point, ButtonObject> PosInstMap = new HashMap<Point, ButtonObject>();

    public String label;

    public Color bgColor;

    public double textSize;
    public Color textColor;
    public Color borderColor;
    public Color selectedBorderColor;

    public int thickness;
    public boolean enforceDimensions;

    public Point center;

    public ButtonObject(Param... params)
    {
        this.name = getParam(params, NAME, null);
        this.topLeftPos = getParam(params, TOP_LEFT_POS, null);
        this.size = getParam(params, SIZE, null);
        this.layerOrder = getParam(params, LAYER_ORDER, 0);

        this.label = getParam(params, LABEL, "");
        this.bgColor = getParam(params, BG_COLOR, Color.WHITE);
        this.textSize = getParam(params, TEXT_SIZE, 20.0);
        this.textColor =  getParam(params, TEXT_COLOR, Color.black);
        this.borderColor =  getParam(params, BORDER_COLOR, Color.black);
        this.selectedBorderColor =  getParam(params, SELECTED_BORDER_COLOR, Color.MAGENTA);

        this.thickness = getParam(params, THICKNESS, 1);

        this.enforceDimensions = getParam(params, ENFORCE_DIMENSIONS, false);

        this.center = new Point(topLeftPos.x, topLeftPos.y);

    }
}