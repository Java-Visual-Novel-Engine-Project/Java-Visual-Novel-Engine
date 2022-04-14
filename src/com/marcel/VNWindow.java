package com.marcel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class VNWindow {

    static class Surface extends JPanel implements ActionListener {

        private final int DELAY = 200;
        private Timer timer;
        private JFrame frame;

        public Surface(JFrame frame) {

            this.frame = frame;
            initTimer();
        }

        private void initTimer() {

            timer = new Timer(DELAY, this);
            timer.start();
        }

        public Timer getTimer() {

            return timer;
        }

        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;

            g2d.setPaint(Color.blue);

            int w = getWidth();
            int h = getHeight();

            Random r = new Random();

            for (int i = 0; i < 200000; i++) {

                int x = Math.abs(r.nextInt()) % w;
                int y = Math.abs(r.nextInt()) % h;
                g2d.drawLine(x, y, x, y);
            }
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

    public static class PointsEx extends JFrame {

        public PointsEx(String title, int x, int y) {

            initUI(title, x, y);
        }

        private void initUI(String title, int x, int y) {

            final Surface surface = new Surface(this);
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
        }

    }



}
