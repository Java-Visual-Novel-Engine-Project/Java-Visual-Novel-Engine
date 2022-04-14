package com.marcel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Array;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.FontMetrics;

import static com.marcel.Util.puts;


public class VNWindow {

	static class Surface extends JPanel implements ActionListener {

		private final int DELAY = 20;
		private Timer timer;
		private JFrame frame;

		public Scene currentScene;

		/*
		private List<ImageData> imgs;

		private ImageData getImageData(String name)
		{
			for (ImageData temp : imgs)
				if (temp.name.equals(name))
					return temp;

			return null;
		}

		private int getIndexOfImageData(String name)
		{
			for (int i = 0; i < imgs.size(); i++)
				if (imgs.get(i).name.equals(name))
					return i;

			return -1;
		}
		*/

		public Surface(JFrame frame) {
			//imgs = new ArrayList<ImageData>();
			this.frame = frame;
			initTimer();
		}

		/*
		public void AddImage(String name, String path)
		{
			if (getImageData(name) != null)
				return;

			ImageData temp = new ImageData(name, new ImageIcon(path).getImage(), 0, 0);
			imgs.add(temp);

		}

		public void RemoveImage(String name)
		{
			int index = getIndexOfImageData(name);
			if (index != -1)
				imgs.remove(index);
		}

		public ImageData GetImage(String name)
		{
			return getImageData(name);
		}
		*/

		private void initTimer() {

			timer = new Timer(DELAY, this);
			timer.start();
		}

		public Timer getTimer() {

			return timer;
		}

		private void RenderObject(SceneObject object, Graphics2D g2d)
		{
			if (object instanceof ImageObject)
			{
				ImageObject obj = (ImageObject) object;

				g2d.drawImage(obj.image, obj.x, obj.y, obj.width, obj.height, null);
			}
			else if (object instanceof	ButtonObject)
			{
				ButtonObject obj = (ButtonObject) object;

				Font font = new Font("Courier New", Font.BOLD,(int)obj.textSize);
				g2d.setFont(font);
				FontMetrics metrics = getFontMetrics(font);
				int w = metrics.stringWidth(" " + obj.text + " ");
				int h = (int)(metrics.getHeight() * 1.5);

				g2d.setColor(obj.backgroundCol);
				g2d.fillRect(obj.x, obj.y, w, h);

				g2d.setColor(Color.black);
				g2d.drawRect(obj.x, obj.y, w, h);

				g2d.drawString(obj.text, obj.x + metrics.charWidth(' '), obj.y + metrics.getHeight());

			}
		}

		private void doDrawing(Graphics g) {

			Graphics2D g2d = (Graphics2D) g;

			g2d.setPaint(Color.blue);

			int w = getWidth();
			int h = getHeight();

			Random r = new Random();

			for (int i = 0; i < 1; i++) {

				int x = Math.abs(r.nextInt()) % w;
				int y = Math.abs(r.nextInt()) % h;
				g2d.drawLine(x, y, x, y);
			}

			// h / w
			// x = w

			if (currentScene == null)
			{
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0,0, w, h);
			}
			else
			{
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0,0, w, h);

				List<SceneObject> tempObjs = new ArrayList<>();
				tempObjs.addAll(currentScene.objects);

				while (tempObjs.size() > 0)
				{
					int index = 0;
					for (int i = 0; i < tempObjs.size(); i++)
						if (tempObjs.get(i).z < tempObjs.get(index).z)
							index = i;

					RenderObject(tempObjs.get(index), g2d);
					tempObjs.remove(index);
				}
			}

			/*
			for (int i = 0; i < imgs.size(); i++)
			{
				ImageData temp = imgs.get(i);

				//puts("X: " + temp.x);
				puts("Y: " + temp.y);
				puts("Screen X:" + w);
				puts("Screen Y:" + h);
				puts("new Y: " + (int) ((w*temp.y)/((double)temp.x)));
				puts("");*//*

				double newratio = w / (double)temp.image.getWidth(null);
				if ((h / (double)temp.image.getHeight(null)) > newratio)
					newratio = h / (double)temp.image.getHeight(null);

				g2d.drawImage(temp.image, temp.x, temp.y,(int)(temp.image.getWidth(null) * newratio), (int)(temp.image.getHeight(null) * newratio), null);
			}
			*/

		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			doDrawing(g);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println(e.toString());
			repaint();
		}
	}

	public static class MainWindow extends JFrame {

		public Surface surface;

		public MainWindow(String title, int x, int y) {

			initUI(title, x, y);
		}

		private void initUI(String title, int x, int y) {

			surface = new Surface(this);
			add(surface);


			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					Timer timer = surface.getTimer();
					timer.stop();
				}
			});

			setTitle(title);
			setSize(x, y);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
		}

	}

}
