import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
/ Edited by TweeKLG | Yann Berlemont
*/


public class CookieClicker extends JFrame {
    // non graphical variables
    private int cookies = 0;
    private int clicker = 1;
    private int clickerPrice = 20;

    // graphical variables
    int numberOfColumns = 5;

    Container container;

    JLabel cookieLabel;
    JButton increaseCookiesButton;

    JLabel clickerLabel;
    JButton increaseClickerButton;

    // buildings
    Building bakery;
    boolean bakeryUnlocked;

    Building robot;
    boolean robotUnlocked;

    Building factory;
    boolean factoryUnlocked;

    public CookieClicker() {
        container = getContentPane();
        container.setLayout(new GridLayout(5, 1));

        bakery = new Building("Bakery", 0, 1, 20);
        bakeryUnlocked = false;

        robot = new Building("Robot", 0, 5, 100);
        robotUnlocked = false;

        factory = new Building("Factory", 0, 10, 200);
        factoryUnlocked = false;

        // produce cookies by hand
        cookieLabel = new JLabel("Cookies: " + cookies);
        increaseCookiesButton = new JButton("Increase Cookies");
        increaseCookiesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cookies += clicker;
            }
        });

        // improve clicking production rate
        clickerLabel = new JLabel("Clicker Level: " + clicker);
        increaseClickerButton = new JButton("Improve Clicker (Costs: " + clickerPrice + ")");
        increaseClickerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                increaseClicker();
            }

            private void increaseClicker() {
                if(cookies >= clickerPrice) {
                    clicker++;
                    cookies -= clickerPrice;
                    clickerPrice *= 2;
                    JOptionPane.showMessageDialog(null, "You have improved your clicker!");
                } else {
                    JOptionPane.showMessageDialog(null, "You have not enough money!");
                }
            }
        });

        java.util.Timer actualizeProgress = new java.util.Timer();
        actualizeProgress.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cookieLabel.setText("Cookies: " + cookies);
                clickerLabel.setText("Clicker Level: " + clicker);
                increaseClickerButton.setText("Improve Clicker (Costs: " + clickerPrice + ")");
            }
        }, 0, 25);

        java.util.Timer getMoreBuildings = new java.util.Timer(); 
        getMoreBuildings.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (bakeryUnlocked == false && clicker >= 2) {
                    bakery.unlock();
                    bakeryUnlocked = true;
                }
                if (robotUnlocked == false && bakery.getLevel() >= 2) {
                    robot.unlock();
                    robotUnlocked = true;
                }         
                if (factoryUnlocked == false && robot.getLevel() >= 2) {
                    factory.unlock();
                    factoryUnlocked = true;
                }
            }
        }, 0, 2000);

        java.util.Timer produceWithBuildings = new java.util.Timer();
        produceWithBuildings.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cookies += bakery.getProductionRate() + robot.getProductionRate() + factory.getProductionRate();
            }
        }, 0, 1000);

        container.add(cookieLabel);
        container.add(increaseCookiesButton);
        container.add(new JLabel("")); // blank label
        container.add(clickerLabel);
        container.add(increaseClickerButton);
    }

    public class Building {
        // non graphical variables
        private String name;
        private int level;
        private int productionRate;
        private int costs;

        // graphical variables
        JLabel label;
        JButton button;

        public Building(String name, int level, int productionRate, int costs) {
            // non graphical variables
            this.name = name;
            this.level = level;
            this.productionRate = productionRate;
            this.costs = costs;

            // graphical variables
            label = new JLabel();
            button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    improve();
                }
            });
        }

        public int getLevel() {
            return level;
        }

        public void unlock() {
            numberOfColumns += 3;
            container.setLayout(new GridLayout(numberOfColumns, 1));
            container.add(new JLabel(""));
            container.add(label);
            container.add(button);
            setSize(210, getHeight() + 120);
            actualize();
        }

        public void improve() {
            if(cookies >= costs) {
                level++;
                cookies -= costs;
                costs *= 2;
                JOptionPane.showMessageDialog(null, "You have improved the " + name + "!");
            } else {
                JOptionPane.showMessageDialog(null, "You have not enough money!");
            }
            actualize();
        }

        public int getProductionRate() {
            return productionRate * level;
        }

        public void actualize() {
            label.setText(name + " Prod. Rate: " + getProductionRate());
            button.setText("Improve (costs: " + costs + ")");
        }
    }

    public static void main(String[] args) {
        CookieClicker cookieClicker = new CookieClicker();
        cookieClicker.setTitle("Cookie Clicker");
        cookieClicker.setSize(210, 200);
        cookieClicker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cookieClicker.setVisible(true);
    }
}
