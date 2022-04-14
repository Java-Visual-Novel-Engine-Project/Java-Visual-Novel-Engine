package com.marcel;

import java.util.ArrayList;
import java.util.List;

import static com.marcel.Util.puts;

public class Scene {

    public List<SceneObject> objects;
    public SceneObject selectedObject;

    public Scene()
    {
        objects = new ArrayList<>();
        selectedObject = null;
    }

    private double getDis(ButtonObject a, ButtonObject b)
    {
        Point centerA = new Point(a.topLeftPos.x + a.center.x, a.topLeftPos.y + a.center.y);
        Point centerB = new Point(b.topLeftPos.x + b.center.x, b.topLeftPos.y + b.center.y);

        return Math.sqrt((centerA.x-centerB.x)*(centerA.x-centerB.x) + (centerA.y-centerB.y)*(centerA.y-centerB.y));
    }

    public void changeSelectedButton(Point move)
    {
        if (!(selectedObject instanceof ButtonObject selButton))
            return;

        List<ButtonObject> buttons = new ArrayList<>();
        for (SceneObject obj: objects)
            if (obj instanceof  ButtonObject button)
                buttons.add(button);

        if (buttons.size() == 0)
            return;

        //puts("DIR: " + move);

        Point center = new Point(selButton.topLeftPos.x + selButton.center.x, selButton.topLeftPos.y + selButton.center.y);
        Point centerI = new Point(0,0);


        ButtonObject minButton = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < buttons.size(); i++)
        {
            ButtonObject currentButton = buttons.get(i);

            if (currentButton == selButton)
                continue;

            centerI.x = currentButton.center.x + currentButton.topLeftPos.x;
            centerI.y = currentButton.center.y + currentButton.topLeftPos.y;
            if (move.x != 0)
            {
                double xdiff = Math.abs(centerI.x - center.x);
                double ydiff = Math.abs(centerI.y - center.y);

                if (ydiff > xdiff * 0.5)
                    continue;

                //puts("OK 1");

                if (move.x == 1)
                {
                    if (centerI.x < center.x)
                        continue;
                }
                else
                {
                    if (centerI.x > center.x)
                        continue;
                }

                //puts("OK 2");


                double dis =  getDis(selButton, currentButton);
                if (dis < minDistance)
                {
                    minButton = currentButton;
                    minDistance = dis;
                    //puts("OK 3");
                }
            }
            if (move.y != 0)
            {
                double xdiff = Math.abs(centerI.x - center.x);
                double ydiff = Math.abs(centerI.y - center.y);

                if (xdiff > ydiff * 0.5)
                    continue;

                if (move.y == 1)
                {
                    if (centerI.y < center.y)
                        continue;
                }
                else
                {
                    if (centerI.y > center.y)
                        continue;
                }


                double dis =  getDis(selButton, currentButton);
                if (dis < minDistance)
                {
                    minButton = currentButton;
                    minDistance = dis;
                }
            }

        }

        if (minButton != null)
        {
            selectedObject = minButton;
        }
        else
        {
            puts("No Button found!");
        }
    }

}
