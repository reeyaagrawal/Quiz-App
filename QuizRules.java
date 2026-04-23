package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class QuizRules extends JFrame implements ActionListener {

    JButton next, back;

    public QuizRules() {

        setTitle("Quiz Rules");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setMinimumSize(new Dimension(1000, 650));

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel(new BorderLayout(20, 20));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 40));

        add(leftPanel, BorderLayout.CENTER);

        JLabel heading = new JLabel(
                "<html>" +
                        "<span style='font-size:22px; color:#2563eb;'>Welcome to</span><br>" +
                        "<span style='font-size:38px; font-weight:bold;'>IntelliQuiz Challenge</span>" +
                        "</html>"
        );

        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 41, 59));

        leftPanel.add(heading, BorderLayout.NORTH);

        JLabel rules = new JLabel();

        rules.setText(
                "<html>" +
                        "<h2 style='color:#111827;'> Quiz Instructions</h2>" +

                        "✔ Each quiz contains multiple-choice questions.<br><br>" +
                        "✔ Select the most appropriate answer for each question.<br><br>" +
                        "✔ There is no negative marking.<br><br>" +
                        "✔ Once answered, you cannot change your response.<br><br>" +
                        "✔ Do not refresh or switch tabs during the quiz.<br><br>" +
                        "✔ Click NEXT to proceed to the next question.<br><br>" +
                        "✔ Final score will be displayed at the end.<br><br>" +

                        "<b style='color:#16a34a;'>Good luck & stay focused!</b>" +
                "</html>"
        );

        rules.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        rules.setForeground(new Color(55, 65, 81));

        leftPanel.add(rules, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        next = createButton("Next");
        back = createButton("Back");

        buttonPanel.add(back);
        buttonPanel.add(next);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);


        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("application/rule.jpg"));

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        int size = Math.min(screen.width, screen.height) / 3;

        Image scaled = img.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);

        JLabel image = new JLabel(new ImageIcon(scaled));
        image.setHorizontalAlignment(JLabel.CENTER);

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(image);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        mainPanel.setBackground(Color.WHITE);


        mainPanel.add(leftPanel);


        mainPanel.add(imagePanel);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    JButton createButton(String text) {

        JButton btn = new JButton(text);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));

        Color normal = new Color(37, 99, 235);
        Color hover = new Color(59, 130, 246);

        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        btn.setPreferredSize(new Dimension(130, 40));

        btn.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(normal);
            }
        });

        btn.addActionListener(this);

        return btn;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == next) {
        	 new Field();
            dispose();
           
        } else if (e.getSource() == back) {

            new LoginPage();
        }
    }

    public static void main(String[] args) {
        new QuizRules();
    }
}