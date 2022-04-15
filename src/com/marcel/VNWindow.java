package com.marcel;

import java.awt.*;
import java.awt.event.*;
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

		public void RenderString(
				String text, Point topLeft, Point bottomRight,
				FontMetrics metrics, Graphics2D g2d
		) throws Exception
		{
			int start_x = topLeft.x;
			int start_y = topLeft.y;

			int x = start_x;
			int y = start_y;

			for (int i = 0; i < text.length(); i++)
			{
				char chr = text.charAt(i);

				if (chr == '\n')
				{
					x = start_x;
					y += metrics.getHeight();
				}
				else if (chr == '\\')
				{
					if (i + 1 >= text.length())
						throw new Exception("Escape Sequence out of bounds!");

					chr = text.charAt(i+1);

					if (chr == 'n')
					{
						x = start_x;
						y += metrics.getHeight();
					}
					else
					{
						if (bottomRight == null)
							g2d.drawString(chr + "", x, y);
						else
						{
							if ((x + metrics.charWidth(chr)) <= bottomRight.x && y <= bottomRight.y)
								g2d.drawString(chr + "", x, y);
						}
						x += metrics.charWidth(chr);
					}
					i++;
					continue;
				}
				else if (chr == '<')
				{
					int closingBracket = text.indexOf('>', i);
					if (closingBracket == -1)
						throw new Exception("Thing doesn't close: " + text);

					String inside = text.substring(i + 1, closingBracket);

					String property = "", value = "";
					//["b", "bold", ]

					// Simple text (i.e. <b>, </i>)
					if (inside.indexOf(':') == -1) {

						// value has to be false
						if (inside.startsWith("/")) {
							property = inside.substring(1);
							value = "false";

						// value has to be true
						} else {
							property = inside;
							value = "true";
						}

					// Adv text
					} else {

						property = inside.substring(0, inside.indexOf(":")).trim();

						String tmp = inside.substring(inside.indexOf(":") + 1).trim();

						if (!tmp.startsWith("'") || !tmp.endsWith("'"))
							throw new Exception("Property values must be in single quotes");

						value = tmp.substring(1, tmp.length()-1);
					}

					puts("INSIDE: " + property + " - " + value);

					i = closingBracket;
				}
				else
				{
					if (bottomRight == null)
						g2d.drawString(chr + "", x, y);
					else
					{
						if ((x + metrics.charWidth(chr)) <= bottomRight.x && y <= bottomRight.y)
							g2d.drawString(chr + "", x, y);
					}
					x += metrics.charWidth(chr);
				}
			}
		}


		private void RenderObject(SceneObject object, Graphics2D g2d) throws Exception
		{
			if (object instanceof ImageObject)
			{
				ImageObject obj = (ImageObject) object;

				g2d.drawImage(obj.image, obj.topLeftPos.x, obj.topLeftPos.y, obj.size.width, obj.size.height, null);
			}
			else if (object instanceof	ButtonObject obj)
			{
				Font font = new Font(obj.fontName, 0, (int) obj.textSize);

				g2d.setFont(font);

				FontMetrics metrics = getFontMetrics(font);

				int w = metrics.stringWidth("  "); // metrics.stringWidth(" " + obj.label + " ");
				int h = (int) (metrics.getHeight() * 1.5);

				if (obj.enforceDimensions)
				{
					w = obj.size.width;
					h = obj.size.height;
				}
				else
				{
					int x = 0;
					int max_x = 0;
					for (int i = 0; i < obj.label.length(); i++)
					{
						char chr = obj.label.charAt(i);

						if (chr == '\n')
						{
							if (x > max_x)
								max_x = x;
							x = 0;
							h += metrics.getHeight();
						}
						else if (chr == '\\')
						{
							if (i + 1 >= obj.label.length())
								throw new Exception("Escape Sequence out of bounds!");

							chr = obj.label.charAt(i+1);
							if (chr == 'n')
							{
								if (x > max_x)
									max_x = x;
								x = 0;
								h += metrics.getHeight();
							}
							else
								x += metrics.charWidth(chr);
							i++;
							continue;
						}
						else if (chr == '<')
						{
							int closingBracket = obj.label.indexOf('>', i);
							if (closingBracket == -1)
								throw new Exception("Thing doesn't close: " + obj.label);

							String inside = obj.label.substring(i + 1, closingBracket);
							//puts("INSIDE: " + inside);

							i = closingBracket;
						}
						else
						{
							x += metrics.charWidth(chr);
						}
					}
					if (x > max_x)
						max_x = x;
					w += max_x;
				}


				obj.center.x = w/2;
				obj.center.y = h/2;


				g2d.setColor(obj.bgColor);
				g2d.fillRect(obj.topLeftPos.x, obj.topLeftPos.y, w, h);

				if (currentScene.selectedObject ==  obj)
					g2d.setColor(obj.selectedBorderColor);
				else
					g2d.setColor(obj.borderColor);

				{
					int x1 = obj.topLeftPos.x;
					int y1 = obj.topLeftPos.y;
					int x2 = w;
					int y2 = h;

					for (int i = 0; i < obj.thickness; i++)
					{
						g2d.drawRect(x1,y1, x2, y2);
						x1++;
						y1++;
						x2 -= 2;
						y2 -= 2;
					}
				}

				//g2d.drawString(obj.label, obj.topLeftPos.x + metrics.charWidth(' '), obj.topLeftPos.y + metrics.getHeight());

				g2d.setColor(obj.textColor);

				Point tl = new Point(
					obj.topLeftPos.x + metrics.charWidth(' '),
					obj.topLeftPos.y + metrics.getHeight()
				);

				Point br = new Point(
					obj.topLeftPos.x + (w - metrics.charWidth(' ')),
					obj.topLeftPos.y + (h - (int)(0.5 * metrics.getHeight()))
				);

				if (obj.enforceDimensions)
					RenderString(obj.label, tl, br, metrics, g2d);
				else
					RenderString(obj.label, tl, null, metrics, g2d);


			}
		}

		private void doDrawing(Graphics g) throws Exception {

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

				tempObjs.addAll(currentScene.sceneObjects);

				while (tempObjs.size() > 0)
				{
					int index = 0;
					for (int i = 0; i < tempObjs.size(); i++)
						if (tempObjs.get(i).layerOrder < tempObjs.get(index).layerOrder)
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
			try {
				doDrawing(g);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

		private void HandleKeyEvent(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_UP)
				surface.currentScene.changeSelectedButton(new Point(0, -1));
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				surface.currentScene.changeSelectedButton(new Point(0, 1));
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
				surface.currentScene.changeSelectedButton(new Point(-1, 0));
			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				surface.currentScene.changeSelectedButton(new Point(1, 0));
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

			addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e){
					HandleKeyEvent(e);
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
