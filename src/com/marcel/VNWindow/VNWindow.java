package com.marcel.VNWindow;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.FontMetrics;

// Java code to demonstrate
// getTime() function of Date class



import static com.marcel.Util.parseHexBinary;


public class VNWindow {

	public static class Surface extends JPanel implements ActionListener {

		private final JFrame frame;

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

		private final long startTime;
		private long tempTime;
		private int frameCount;
		private double deltaTime;
		private double fps;

		public Surface(JFrame frame) {
			//imgs = new ArrayList<ImageData>();
			this.frame = frame;
			frameCount = 0;
			fps = 0;
			startTime = System.currentTimeMillis();
			tempTime = System.currentTimeMillis();
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


		private FontMetrics SetFontAndGetMetrics(
				Graphics2D g2d, String fontName, int fontSize, boolean isBold, boolean isItalic
		) {
				int fontSetting = 0;

				if (isBold) fontSetting |= Font.BOLD;
				if (isItalic) fontSetting |= Font.ITALIC;

				Font font = new Font(fontName, fontSetting, fontSize);



				g2d.setFont(font);

			return getFontMetrics(font);
		}

		private int getCharHeight(Graphics2D g2, char chr)
		{
			FontRenderContext frc = g2.getFontRenderContext();
			GlyphVector gv = g2.getFont().createGlyphVector(frc, chr+"");
			return gv.getPixelBounds(null, 0, 0).height;
		}

		public void RenderString(
				String text, String fontName, int fontSize, Color textColor,
				Point topLeftText, Rectangle boundary,
				FontMetrics metrics, Graphics2D g2d,
				double speed, double maxTime
		) throws Exception
		{
			double elapsedTime = 0;
			g2d.setColor(textColor);

			boolean bold = false;
			boolean italic = false;

			int start_x = topLeftText.x;
			int start_y = topLeftText.y;

			int x = start_x;
			int y = start_y;

			for (int i = 0; i < text.length() && elapsedTime <= maxTime; i++)
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
						if (boundary == null)
							g2d.drawString(chr + "", x, y);
						else
						{
							if ((x + metrics.charWidth(chr)) <= boundary.br.x && y <= boundary.br.y && x >= boundary.tl.x && (y-metrics.getHeight()) >= boundary.tl.y)
								g2d.drawString(chr + "", x, y);
						}
						x += metrics.charWidth(chr);
						elapsedTime += (1 / speed);
					}
					i++;
				}
				else if (chr == '<')
				{
					int closingBracket = text.indexOf('>', i);
					if (closingBracket == -1)
						throw new Exception("Thing doesn't close: " + text);

					String inside = text.substring(i + 1, closingBracket);

					String property, value;
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

						if (tmp.startsWith("'") && tmp.endsWith("'"))
							value = tmp.substring(1, tmp.length()-1);
						else
							value = tmp;
						//throw new Exception("Property values must be in single quotes");
					}

					if (property.equals("b") || property.equals("bold"))
						bold = value.equals("true");

					if (property.equals("i") || property.equals("italic"))
						italic = value.equals("true");

					if (property.equals("font-size") || property.equals("size"))
						fontSize = Integer.parseInt(value);

					if (property.equals("font-name") || property.equals("font"))
						fontName = value;

					if (property.equals("speed"))
						speed = Double.parseDouble(value);

					if (property.equals("color"))
					{
						if (value.startsWith("#"))
						{
							if (value.length() == 7)
							{
								g2d.setColor(Color.decode(value));
							}
							else if (value.length() == 9)
							{
								byte[] data_ = parseHexBinary(value.substring(1));
								int[] data = new int[4];
								for (int ii = 0; ii < 4; ii++)
									data[ii] = data_[ii] & 0xff;
								//puts("AA: " + data[0] + " " + data[1] + " "+ data[2] + " "+ data[3]);
								g2d.setColor(new Color(data[0],data[1],data[2],data[3]));
							}
							else
								throw new Exception("Unknown HEX Format (either #RRGGBB or #RRGGBBAA!");
						}
						else
						{
							try {
								Field field = Class.forName("java.awt.Color").getField(value);
								g2d.setColor((Color)field.get(null));
							} catch (Exception e) {
								throw new Exception("Unknown Number \"" + value + "\"!");
							}
						}

					}

				  	metrics = SetFontAndGetMetrics(g2d, fontName, fontSize, bold, italic);

					//puts("INSIDE: " + property + " - " + value);

					i = closingBracket;
				}
				else
				{
					if (boundary == null)
						g2d.drawString(chr + "", x, y);
					else
					{
						if ((x + metrics.charWidth(chr)) <= boundary.br.x && y <= boundary.br.y && x >= boundary.tl.x && (y-getCharHeight(g2d, chr)) >= boundary.tl.y)
							g2d.drawString(chr + "", x, y);
					}
					elapsedTime += (1 / speed);
/*					g2d.setColor(Color.BLUE);

					{
						int x2 = x + metrics.charWidth(chr);
						int y2 = y - getCharHeight(g2d, chr);

						g2d.drawLine(x,y,x,y2);
						g2d.drawLine(x,y2,x2,y2);
						g2d.drawLine(x2,y2,x2,y);
						g2d.drawLine(x2,y,x,y);
						g2d.drawLine(x,y,x2,y2);
						g2d.drawLine(x2,y2,x,y);
					}

					g2d.setColor(textColor);*/

					x += metrics.charWidth(chr);
				}
			}
		}

		private void RenderObject(SceneObject object, Graphics2D g2d) throws Exception
		{
			if (object instanceof ImageObject obj)
			{

				g2d.drawImage(obj.image, obj.topLeftPos.x, obj.topLeftPos.y, obj.size.width, obj.size.height, null);
			}
			else if (object instanceof ButtonObject obj)
			{
				FontMetrics metrics = SetFontAndGetMetrics(g2d, obj.fontName, obj.fontSize, false, false);

				//int w = metrics.stringWidth("  "); // metrics.stringWidth(" " + obj.label + " ");
				//int h = (int) (metrics.getHeight() * 0.5);

				Rectangle boundaries = new Rectangle(new Point(obj.topLeftPos.x, obj.topLeftPos.y), new Point(obj.topLeftPos.x, obj.topLeftPos.y));

				Point tp = new Point(
						obj.topLeftPos.x,// + metrics.charWidth(' '),
						obj.topLeftPos.y
				);

				if (obj.enforceDimensions)
				{
					boundaries.br.x = boundaries.tl.x + obj.size.width;
					boundaries.br.y = boundaries.tl.y + obj.size.height;
				}
				else
				{
					boolean bold = false, italic = false;
					double elapsedTime = 0;
					double maxTime = obj.textDisp.passedTime;
					double speed = obj.textDisp.speed;
					String fontName = obj.fontName;
					int fontSize = obj.fontSize;

					int startX = boundaries.tl.x;
					int startY = boundaries.tl.y;

					int x = startX;
					int y = startY;

					String text_ = obj.textDisp.text;

					for (int i = 0; i < text_.length() && elapsedTime <= maxTime; i++)
					{
						char chr = text_.charAt(i);

						if (chr == '\n')
						{
							if (x > boundaries.br.x)
								boundaries.br.x = x;
							x = startX;
							y += metrics.getHeight();
						}
						else if (chr == '\\')
						{
							if (i + 1 >= text_.length())
								throw new Exception("Escape Sequence out of bounds!");

							chr = text_.charAt(i+1);
							if (chr == 'n')
							{
								if (x > boundaries.br.x)
									boundaries.br.x = x;
								x = startX;
								y += metrics.getHeight();
							}
							else {
								x += metrics.charWidth(chr);
								if (y - getCharHeight(g2d, chr) < boundaries.tl.y)
									boundaries.tl.y = y - getCharHeight(g2d, chr);
								if (y > boundaries.br.y)
									boundaries.br.y = y;
								elapsedTime += (1 / speed);
							}
							i++;
						}
						else if (chr == '<')
						{
							int closingBracket = text_.indexOf('>', i);
							if (closingBracket == -1)
								throw new Exception("Thing doesn't close: " + text_);

							String inside = text_.substring(i + 1, closingBracket);

							String property, value;
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

								if (tmp.startsWith("'") && tmp.endsWith("'"))
									value = tmp.substring(1, tmp.length()-1);
								else
									value = tmp;
								//throw new Exception("Property values must be in single quotes");


							}

							if (property.equals("b") || property.equals("bold"))
								bold = value.equals("true");

							if (property.equals("i") || property.equals("italic"))
								italic = value.equals("true");

							if (property.equals("font-size") || property.equals("size"))
								fontSize = Integer.parseInt(value);

							if (property.equals("font-name") || property.equals("font"))
								fontName = value;

							if (property.equals("speed"))
								speed = Double.parseDouble(value);

							metrics = SetFontAndGetMetrics(g2d, fontName, fontSize, bold, italic);

							//puts("INSIDE: " + property + " - " + value);

							i = closingBracket;
						}
						else
						{
							x += metrics.charWidth(chr);
							if (y - getCharHeight(g2d, chr) < boundaries.tl.y)
								boundaries.tl.y = y - getCharHeight(g2d, chr);
							if (y > boundaries.br.y)
								boundaries.br.y = y;
							elapsedTime += (1 / speed);
						}
					}
					if (x > boundaries.br.x)
						boundaries.br.x = x;




/*					{
						y_diff = -min_y;
						h += y_diff;
						realTopLeft.y -= y_diff*1;
					}*/
					boundaries.tl.x -= 10;
					boundaries.tl.y -= 10;
					boundaries.br.x += 10;
					boundaries.br.y += 10;
				}

				{
					Point diff = new Point(
							obj.topLeftPos.x - boundaries.tl.x,
							obj.topLeftPos.y - boundaries.tl.y
					);

					boundaries.tl.x += diff.x;
					boundaries.tl.y += diff.y;
					boundaries.br.x += diff.x;
					boundaries.br.y += diff.y;

					tp.x += diff.x;
					tp.y += diff.y;

				}





				obj.center.x = (boundaries.tl.x + boundaries.br.x)/2;
				obj.center.y = (boundaries.tl.y + boundaries.br.y)/2;

				g2d.setColor(obj.bgColor);

				int w = (boundaries.br.x - boundaries.tl.x);
				int h = (boundaries.br.y - boundaries.tl.y);

				g2d.fillRect(boundaries.tl.x, boundaries.tl.y, w, h);

				if (currentScene.selectedObject ==  obj)
					g2d.setColor(obj.selectedBorderColor);
				else
					g2d.setColor(obj.borderColor);

				{
					int x1 = boundaries.tl.x;
					int y1 = boundaries.tl.y;
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



				if (obj.enforceDimensions)
					RenderString(obj.textDisp.text, obj.fontName, obj.fontSize, obj.textColor, tp, boundaries, metrics, g2d, obj.textDisp.speed, obj.textDisp.passedTime);
				else
					RenderString(obj.textDisp.text, obj.fontName, obj.fontSize, obj.textColor, tp, null, metrics, g2d, obj.textDisp.speed, obj.textDisp.passedTime);

				obj.textDisp.UpdateTime(deltaTime);
			}
		}


		private void UpdateTime()
		{
			long tempTime2 = System.currentTimeMillis();
			deltaTime = (tempTime2 - tempTime) / 1000.0;
			tempTime = tempTime2;
			frameCount++;
			//fps = frameCount / ((tempTime2 - startTime) / 1000.0);
			fps = 1 /deltaTime;
		}


		private void doDrawing(Graphics g) throws Exception {

			Graphics2D g2d = (Graphics2D) g;

			UpdateTime();

			int w = getWidth();
			int h = getHeight();

			System.out.print("\r                                               \r" +
							   "FPS: " + (Math.round(fps * 100) / 100.0)
			);

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

				List<SceneObject> tempObjs = new ArrayList<>(currentScene.sceneObjects);

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
