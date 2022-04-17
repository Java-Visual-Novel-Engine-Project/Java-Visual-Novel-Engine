package com.marcel.VNWindow;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    public List<SceneObject> sceneObjects;
    public SceneObject selectedObject;

    public Scene()
    {
        sceneObjects = new ArrayList<>();
        selectedObject = null;
    }

    private Point getCenter(ButtonObject button)
    {
        Point btnTopLeftPos = button.topLeftPos;
        Point btnCenterPos  = button.center;

        return new Point(
            btnTopLeftPos.x + btnCenterPos.x,
            btnTopLeftPos.y + btnCenterPos.y
        );
    }

    private double getDistance(ButtonObject a, ButtonObject b)
    {
        Point centerA = getCenter(a);
        Point centerB = getCenter(b);

        return Math.sqrt(
            (centerA.x-centerB.x) * (centerA.x-centerB.x) +
            (centerA.y-centerB.y) * (centerA.y-centerB.y)
        );
    }

    public void changeSelectedButton(Point move)
    {
        if (!(selectedObject instanceof ButtonObject selectedBtn))
            return;

        List<ButtonObject> buttons = new ArrayList<>();

        for (SceneObject obj: sceneObjects)
            if (obj instanceof ButtonObject button)
                buttons.add(button);

        if (buttons.size() == 0)
            return;

        //puts("DIR: " + move);

        Point center  = getCenter(selectedBtn);
        Point centerI;

        ButtonObject minButton = null;

        double minDistance = Double.POSITIVE_INFINITY;

        for (ButtonObject currentBtn : buttons) {
            if (currentBtn == selectedBtn)
                continue;

            centerI = getCenter(currentBtn);

            if (move.x != 0) {
                double xdiff = Math.abs(centerI.x - center.x);
                double ydiff = Math.abs(centerI.y - center.y);

                if (ydiff > xdiff * 0.8)
                    continue;

                //puts("OK 1");

                if (move.x == 1) {
                    if (centerI.x < center.x)
                        continue;
                } else {
                    if (centerI.x > center.x)
                        continue;
                }

                //puts("OK 2");
            }
            if (move.y != 0) {
                double xdiff = Math.abs(centerI.x - center.x);
                double ydiff = Math.abs(centerI.y - center.y);

                if (xdiff > ydiff * 0.8)
                    continue;

                if (move.y == 1) {
                    if (centerI.y < center.y)
                        continue;
                } else {
                    if (centerI.y > center.y)
                        continue;
                }
            }


            double distance = getDistance(selectedBtn, currentBtn);

            if (distance < minDistance) {
                minButton = currentBtn;
                minDistance = distance;
                //puts("OK 3");
            }
        }

        if (minButton != null)
            selectedObject = minButton;

            //puts("No Button found!");
    }

}
