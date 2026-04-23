package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class RoundedPanel extends JPanel {

    private int radius;

    RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}
public class Field extends JFrame{

    JPanel programming, cn, os, bc, ai, gk;
    JPanel[] cards;

    public Field() {

        setTitle("Quiz Domain Selection");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel(new BorderLayout(20, 20));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 20));

        add(leftPanel, BorderLayout.CENTER);

        JLabel heading = new JLabel(
                "<html>Choose a domain.<br>Test your knowledge.<br>Master the unknown.</html>"
        );

        heading.setFont(new Font("Segoe UI Black", Font.BOLD, 34));
        heading.setForeground(new Color(30, 41, 59));

        leftPanel.add(heading, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setBackground(Color.WHITE);

        programming = createCard("Programming");
        cn = createCard("Computer Network");
        os = createCard("Operating System");
        bc = createCard("Basics of Computer");
        ai = createCard("Artificial Intelligence");
        gk = createCard("General Knowledge");

        cards = new JPanel[]{programming, cn, os, bc, ai, gk};

        grid.add(programming);
        grid.add(cn);
        grid.add(os);
        grid.add(bc);
        grid.add(ai);
        grid.add(gk);

        leftPanel.add(grid, BorderLayout.CENTER);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("application/option.jpeg"));

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int imgW = (int)(screen.width * 0.35);
        int imgH = (int)(screen.height * 0.55);

        Image i2 = i1.getImage().getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);

        JLabel image = new JLabel(new ImageIcon(i2));

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(Color.WHITE);

        imagePanel.add(image);

        add(imagePanel, BorderLayout.EAST);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                adjustCards();
            }
        });

        setVisible(true);
    }

    JPanel createCard(String text) {

        JPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(37, 99, 235));

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.WHITE);

        card.add(label, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(59, 130, 246));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(37, 99, 235));
            }

            public void mouseClicked(MouseEvent e) {

                switch(text) {
                    case "Programming":
                        new QuizApp("programming_quiz", "Programming Quiz");
                        break;

                    case "Computer Network":
                        new QuizApp("cn_quiz", "Computer Network Quiz");
                        break;

                    case "Operating System":
                        new QuizApp("os_quiz", "Operating System Quiz");
                        break;

                    case "Basics of Computer":
                        new QuizApp("basics_mcq", "Basics of Computer Quiz");
                        break;

                    case "Artificial Intelligence":
                        new QuizApp("ai_mcq", "Artificial Intelligence Quiz");
                        break;

                    case "General Knowledge":
                        new QuizApp("gk_mcq", "General Knowledge Quiz");
                        break;
                }

                dispose();
            }
        });

        return card;
    }
    void adjustCards() {

        int w = getWidth();

        int cardWidth;
        int cardHeight;

        if (w < 1200) {
            cardWidth = 220;   
            cardHeight = 45;   
        } else {
            cardWidth = 160;
            cardHeight = 70;
        }

        for (JPanel card : cards) {
            card.setPreferredSize(new Dimension(cardWidth, cardHeight));
            card.revalidate();
            card.repaint();
        }
    }
    
    public static void main(String[] args) {
        new Field();
    }
}