package application;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class LoginPage extends JFrame implements ActionListener {

    JTextField usernameField, emailField;
    JPasswordField passwordField;

    JButton loginBtn, signupBtn;

    JPanel card;

    Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

    public LoginPage() {

        setTitle("Login");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(new Color(15, 23, 42));

        card = new JPanel();
        card.setSize(400, 360);
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        add(card);

        JLabel title = new JLabel("Login");
        title.setBounds(170, 20, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(title);


        JLabel user = new JLabel("Username");
        user.setBounds(50, 60, 100, 20);
        user.setFont(labelFont);
        card.add(user);

        usernameField = new JTextField();
        usernameField.setBounds(50, 80, 300, 35);
        styleInput(usernameField);
        card.add(usernameField);


        JLabel email = new JLabel("Email");
        email.setBounds(50, 120, 100, 20);
        email.setFont(labelFont);
        card.add(email);

        emailField = new JTextField();
        emailField.setBounds(50, 140, 300, 35);
        styleInput(emailField);
        card.add(emailField);


        JLabel pass = new JLabel("Password");
        pass.setBounds(50, 180, 100, 20);
        pass.setFont(labelFont);
        card.add(pass);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 200, 300, 35);
        styleInput(passwordField);
        card.add(passwordField);


        loginBtn = new JButton("Login");
        loginBtn.setBounds(50, 250, 140, 35);
        stylePrimaryButton(loginBtn);
        card.add(loginBtn);


        signupBtn = new JButton("Create Account");
        signupBtn.setBounds(210, 250, 140, 35);
        styleSecondaryButton(signupBtn);
        card.add(signupBtn);

        addHover(loginBtn);
        addHover(signupBtn);

        loginBtn.addActionListener(this);
        signupBtn.addActionListener(this);

        add(card);

        centerCard();

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                centerCard();
            }
        });

        setVisible(true);
    }

    void loginUser() {

        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if(user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username/email and password");
            return;
        }

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz_app",
                    "root",
                    "reeyA@27"
            );

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE (username=? OR email=?) AND password=?"
            );

            ps.setString(1, user);
            ps.setString(2, user);
            ps.setString(3, pass);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "Login Successful");

                new QuizRules();
                dispose();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username/email or password");
            }

            con.close();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + e.getMessage());
        }
    }
    
    void styleInput(JComponent field) {

        field.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));

        field.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(37,99,235), 2));
            }
            public void mouseExited(MouseEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 1));
            }
        });

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(37,99,235), 2));
            }
        });
    }

    //  BUTTON STYLE 
    void stylePrimaryButton(JButton btn) {
        btn.setBackground(new Color(37, 99, 235));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    void styleSecondaryButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(37, 99, 235));
        btn.setBorder(BorderFactory.createLineBorder(new Color(37, 99, 235)));
        btn.setFocusPainted(false);
    }

    // HOVER 
    void addHover(JButton btn) {

        btn.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {

                if (btn == loginBtn) {
                    btn.setBackground(new Color(29, 78, 216));
                } else {
                    btn.setForeground(Color.WHITE); // ⭐ TEXT WHITE ON HOVER
                    btn.setBackground(new Color(37, 99, 235));
                }
            }

            public void mouseExited(MouseEvent e) {

                if (btn == loginBtn) {
                    btn.setBackground(new Color(37, 99, 235));
                } else {
                    btn.setForeground(new Color(37, 99, 235));
                    btn.setBackground(Color.WHITE);
                }
            }
        });
    }

    void centerCard() {
        int x = (getWidth() - card.getWidth()) / 2;
        int y = (getHeight() - card.getHeight()) / 2;
        card.setLocation(x, y);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == signupBtn) {
            new SignupPage();
            dispose();
        }else if(ae.getSource() == loginBtn) {
            loginUser();
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}