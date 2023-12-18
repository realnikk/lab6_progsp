import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
public class SeaBattle extends JFrame {
    static final Color background = new Color(148, 201, 210);
    static final Color buttonBackground = new Color(189, 189, 189);
    static final Color buttonRedBackground = new Color(201, 73, 73);
    static final Color buttonActive = new Color(160, 160, 160);
    static final Color buttonRedActive = new Color(169, 67, 67);
    private Thread pirateShipThread, cannonballThread, sinkingShipThread;
    private Cannonball cannonball;
    private BufferedImage rocketSystemImg, piratesShipImg, cannonballImg;
    private int piratesShipSpeed, cannonballSpeed;
    private int piratesShipWidth = 100, piratesShipHeight = 80, piratesShipX = 10, piratesShipY = 20;
    private int rocketSystemWidth = 50, rocketSystemHeight = 100, rocketSystemX = 375, rocketSystemY = 400;
    private int cannonballWidth = 20, cannonballHeight = 20;
    private int cannonballX = rocketSystemX+rocketSystemWidth/2-cannonballWidth/2, cannonballY = rocketSystemY;
    private Random random;

    public SeaBattle() {
        setTitle("Sea battle");
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setVisible(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton startButton = new JButton("Start game");
        startButton.setFocusPainted(false);
        startButton.setBackground(buttonBackground);
        startButton.setBounds(600, 50, 150,25);

        JButton stopButton = new JButton("Stop game");
        stopButton.setFocusPainted(false);
        stopButton.setBackground(buttonBackground);
        stopButton.setBounds(600, 85, 150,25);

        JButton fireButton = new JButton("FIRE!");
        fireButton.setFocusPainted(false);
        fireButton.setBackground(buttonRedBackground);
        fireButton.setBounds(600, 120, 150,50);

        RocketSystem rocketSystem = new RocketSystem();
        rocketSystem.setBounds(375, 400, 50, 100);

        PiratesShip piratesShip = new PiratesShip();
        piratesShip.setBounds(0, 0, 560, 120);
        piratesShip.setBackground(background);

        cannonball = new Cannonball();
        cannonball.setBounds(0, 0, 410, 390);
        cannonball.setVisible(false);

        add(startButton);
        add(stopButton);
        add(fireButton);
        add(piratesShip);
        add(cannonball);
        add(rocketSystem);

        startButton.addMouseListener(new ButtonMouseAdapter(startButton));
        stopButton.addMouseListener(new ButtonMouseAdapter(stopButton));
        fireButton.addMouseListener(new RedButtonMouseAdapter(fireButton));

        startButton.addActionListener(new StartActionListener());
        stopButton.addActionListener(new StopActionListener());
        fireButton.addActionListener(new FireActionListener());
    }

    private class RocketSystem extends JPanel {
        public RocketSystem() {
            setPreferredSize(new Dimension(50, 100));
            try {
                rocketSystemImg = ImageIO.read(new File("D:\\ISITvE\\5sem\\ПрогСП\\lab6_progsp\\src\\rocketSystem.jpg"));
            } catch (IOException exc) {
            };
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.drawImage(rocketSystemImg, rocketSystemX, rocketSystemY, rocketSystemWidth, rocketSystemHeight, this);
        }
    }

    private class PiratesShip extends JPanel {
        public PiratesShip() {
            setPreferredSize(new Dimension(100, 80));
            try {
                piratesShipImg = ImageIO.read(new File("D:\\ISITvE\\5sem\\ПрогСП\\lab6_progsp\\src\\piratesShip.jpg"));
            } catch (IOException exc) {
            };
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.drawImage(piratesShipImg, piratesShipX, piratesShipY, piratesShipWidth, piratesShipHeight, this);
        }
    }

    private class Cannonball extends JPanel {
        public Cannonball() {
            setPreferredSize(new Dimension(20, 20));
            try {
                cannonballImg = ImageIO.read(new File("D:\\ISITvE\\5sem\\ПрогСП\\lab6_progsp\\src\\cannonball.jpg"));
            } catch (IOException exc) {
            };
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.drawImage(cannonballImg, cannonballX, cannonballY, cannonballWidth, cannonballHeight, this);
        }
    }

    public class StartActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            piratesShipX = 0;
            piratesShipY = 20;
            random = new Random();
            piratesShipSpeed = 1+random.nextInt(5);
            pirateShipThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (piratesShipX != 450) {
                        piratesShipX += piratesShipSpeed;
                        repaint();
                        try {
                            Thread.sleep(20);
                        } catch (Exception exc) {
                        };
                    }
                }
            });
            pirateShipThread.start();
        }
    }

    public class StopActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pirateShipThread.stop();
            cannonballThread.stop();
        }
    }

    public class FireActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cannonball.setVisible(true);
            cannonballY = rocketSystemY-20;
            random = new Random();
            cannonballSpeed = 1+random.nextInt(5);
            cannonballThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (cannonballY != 0) {
                        cannonballY -= cannonballSpeed;
                        if(cannonballY == 100 && (piratesShipX >= 300 && piratesShipX <= 400)){
                            sinkingShipThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (piratesShipY != 120) {
                                        piratesShipY++;
                                        repaint();
                                        try {
                                            Thread.sleep(20);
                                        } catch (Exception exc) {
                                        };
                                    }
                                }
                            });
                            //cannonballThread.stop();
                            //pirateShipThread.stop();
                            sinkingShipThread.start();
                        }
                        repaint();
                        try {
                            Thread.sleep(20);
                        } catch (Exception exc) {
                        };
                    }
                    cannonball.setVisible(false);
                }
            });
            cannonballThread.start();
        }
    }

    public class ButtonMouseAdapter extends MouseAdapter {
        JButton button;
        public ButtonMouseAdapter(JButton button){
            this.button = button;
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(buttonActive);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(buttonBackground);
        }
    }

    public class RedButtonMouseAdapter extends MouseAdapter {
        JButton button;
        public RedButtonMouseAdapter(JButton button){
            this.button = button;
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(buttonRedActive);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(buttonRedBackground);
        }
    }

        public static void main(String[] args) {
            new SeaBattle().setVisible(true);
        }
    }
