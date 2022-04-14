package com.marcel;

import com.marcel.RACF.ConfigFile;

import java.awt.*;

import static com.marcel.Util.getPath;
import static com.marcel.Util.*;

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

		/*
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				VNWindow.MainWindow window = new VNWindow.MainWindow("VN Engine - Testing", 1080, 720);
				window.setVisible(true);
			}
		});
		*/

		VNWindow.MainWindow window = new VNWindow.MainWindow("VN Engine - Testing", 1080, 720);
		window.setVisible(true);
		System.out.println("A");

		Scene title = new Scene();
		window.surface.currentScene = title;

		{
			ImageObject image = new ImageObject("background", getPath(file.GetConfigValueString("Settings.Paths.Title_Background")), 0, 0, -1);
			title.objects.add(image);
		}

		{
			ImageObject image = new ImageObject("background-2", getPath(file.GetConfigValueString("Settings.Paths.Title_Background")), 10, 0, 3);
			title.objects.add(image);
			image.height = 100;
			image.width = 200;
		}

		{
			ImageObject image = new ImageObject("background-3", getPath(file.GetConfigValueString("Settings.Paths.Title_Background")), 70, 30, 1);
			title.objects.add(image);
			image.height = 200;
			image.width = 100;
		}

		{
			ButtonObject button = new ButtonObject("Play","This is a Button!\nNewline?", 20, Color.CYAN, 600, 300, 10, 100, 50, 2, Color.gray);
			title.objects.add(button);

			while (true)
			{
				button.x--;
				button.y--;
				// Make Button move with arrow keys
				// Maybe Make Buttons work with \n char to make multi line Button (if ya can do it)
				// Make Button map
				Thread.sleep(10);
			}
		}

		//window.surface.AddImage("background", getPath(file.GetConfigValueString("Settings.Paths.Title_Background")));

	}

}
