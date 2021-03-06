package com.marcel;

import com.marcel.RACF.ConfigFile;
import com.marcel.RACF.ConfigTokens;
import com.marcel.VNWindow.*;
import com.marcel.VNWindow.Point;


import java.awt.*;

import static com.marcel.Util.getPath;
import static com.marcel.Util.*;

import static com.marcel.VNWindow.ButtonObject.ButtonParams.*;

public class Main {

	public static void main(String[] args)
	{
		try
		{
			realMain();
		}
		catch (Exception e)
		{
			puts("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void realMain() throws Exception
	{
		puts("Starting...");

		ConfigFile file = new ConfigFile(getPath("config.racf"));

		file.DisplayTokens();

		puts("TESTING: " + file.GetConfigValueString("Settings.test"));

		//puts("TESTING 2: " + file.GetConfigValueString("Settings.Test[$Settings.Test[0][1]][a]"));

		//puts("TESTING 2: " + file.GetConfigValueString("Settings.test2[bruh_2]"));

		/*
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				VNWindow.MainWindow window = new VNWindow.MainWindow("VN Engine - Testing", 1080, 720);
				window.setVisible(true);
			}
		});
		*/

		VNWindow.MainWindow window = new VNWindow.MainWindow(
			"VN Engine - Testing", 1080, 720
		);

		window.setVisible(true);

		puts("A");

		Scene title = new Scene();

		window.surface.currentScene = title;

		/*
		{
			ImageObject image = new ImageObject(
				"background",
				getPath(
					file.GetConfigValueString("Settings.Paths.Title_Background")
				),
				0, 0, -1
			);
			title.sceneObjects.add(image);
		}

		{
			ImageObject image = new ImageObject(
				"background-2",
				getPath(
					file.GetConfigValueString("Settings.Paths.Title_Background")
				),
				10, 0, 3
			);
			title.sceneObjects.add(image);
			image.height = 100;
			image.width = 200;
		}

		{
			ImageObject image = new ImageObject(
				"background-3",
				getPath(
					file.GetConfigValueString("Settings.Paths.Title_Background")
				),
				70, 30, 1
			);
			title.objects.add(image);
			image.height = 200;
			image.width = 100;
		}

		{
			ButtonObject button = new ButtonObject(
				"Play","This is a Button!\nNewline?",
				20, Color.CYAN, 600, 300,
				10, 100, 50, 2, Color.gray
			);
			title.sceneObjects.add(button);
		}
		*/
		{
			ButtonObject button = new ButtonObject(
					name("Play 1"),
					topLeftPos(new Point(300, 100)),
					size(new Size(100, 50)),
					layerOrder(2),
					label(
							((ConfigTokens.ConfigVariableString)file.GetConfigValue("Settings.test_string")).GetOriginalString()
					),
					bgColor(Color.RED),
					borderColor(Color.GREEN),
					fontSize(30),
					textColor(Color.black),
					thickness(4)
			);

			title.sceneObjects.add(button);
		}

		{
			ButtonObject button = new ButtonObject(
					name("Play 2"),
					topLeftPos(new Point(300, 300)),
					size(new Size(146, 70)),
					layerOrder(2),
					label("B r u h\n2221"),
					bgColor(Color.BLACK),
					fontSize(40),
					textColor(Color.white),
					borderColor(Color.yellow),
					enforceDimensions(false),
					thickness(5)
			);

			title.sceneObjects.add(button);
		}

		{
			ImageObject image = new ImageObject("IMAGEHHHHHHH",getPath(file.GetConfigValueString("Settings.Paths.Title_Background")), new Point(0, 0), -1);

			title.sceneObjects.add(image);
		}


		{
			ButtonObject button = new ButtonObject(
				name("Play"),
				topLeftPos(new Point(600, 300)),
				size(new Size(100, 50)),
				layerOrder(2),
				label("This is a Button!\nNewline?"),
				bgColor(Color.CYAN),
				fontSize(20),
				textColor(new Color(75, 0, 198, 168)), //#FFFFFF1
				thickness(3)
			);

			title.sceneObjects.add(button);

			title.selectedObject = button;

/*			while (true)
			{
				//button.topLeftPos.x--;
				//button.topLeftPos.y--;

				// Make Button move with arrow keys
				// Maybe Make Buttons work with \n char to make multi line Button (if ya can do it)
				// Make Button map
				Thread.sleep(10);
			}*/
		}

		//window.surface.AddImage("background", getPath(file.GetConfigValueString("Settings.Paths.Title_Background")));




		while (true)
		{
			window.surface.repaint();
			Thread.sleep(20);
		}


	}

}
