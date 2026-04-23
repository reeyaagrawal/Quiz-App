package application;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

public class SignupPage extends JFrame implements ActionListener {

    JTextField usernameField, emailField, dobField;
    JPasswordField passwordField, confirmPasswordField;

    JButton createBtn, loginBtn;

    JPanel card;

    public SignupPage() {
    	
        setTitle("Signup");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(new Color(15, 23, 42));

        card = new JPanel();
        card.setSize(420, 450);
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        add(card);

        JLabel title = new JLabel("Create Account");
        title.setBounds(120, 20, 250, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        card.add(title);

        usernameField = new JTextField();
        emailField = new JTextField();
        dobField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        addField("Username", usernameField, 60);
        addField("Email", emailField, 110);
        addField("DOB", dobField, 160);
        addField("Password", passwordField, 210);
        addField("Confirm Password", confirmPasswordField, 260);

        createBtn = new JButton("Create Account");
        loginBtn = new JButton("Back to Login");

        createBtn.setBounds(50, 340, 140, 35);
        loginBtn.setBounds(210, 340, 140, 35);

        stylePrimary(createBtn);
        styleSecondary(loginBtn);

        addHover(createBtn);
        addHover(loginBtn);

        createBtn.addActionListener(this);
        loginBtn.addActionListener(this);

        card.add(createBtn);
        card.add(loginBtn);

        add(card);

        centerCard();

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                centerCard();
            }
        });

        setVisible(true);
    }
    
    Connection getConnection() throws SQLException {
	    return DriverManager.getConnection(
	        "jdbc:mysql://localhost:3306/quiz_app",
	        "root",
	        "reeyA@27"
	    );
	}
    
    void registerUser() {

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String dob = dobField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if(username.isEmpty() || email.isEmpty() || dob.isEmpty()
                || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        if(!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match");
            return;
        }

        try {
            Connection con = getConnection();


            PreparedStatement check = con.prepareStatement(
                "SELECT * FROM users WHERE email=? OR username=?"
            );
            check.setString(1, email);
            check.setString(2, username);

            ResultSet rs = check.executeQuery();

            if(rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "Username or Email already exists");
                con.close();
                return;
            }

            // insert user
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users(username,email,dob,password) VALUES(?,?,?,?)"
            );

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, dob);
            ps.setString(4, password);

            int x = ps.executeUpdate();

            if(x > 0) {
                JOptionPane.showMessageDialog(this, "Account created successfully");
                new LoginPage();
                dispose();
            }

            con.close();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + e.getMessage());
        }
    }

    void addField(String label, JComponent field, int y) {

        JLabel l = new JLabel(label);
        l.setBounds(50, y, 120, 20);
        card.add(l);

        field.setBounds(50, y + 20, 300, 30);

        field.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));

        field.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(37,99,235), 2));
            }
            public void mouseExited(MouseEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 1));
            }
        });

        card.add(field);
    }

    void stylePrimary(JButton btn) {
        btn.setBackground(new Color(37, 99, 235));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    void styleSecondary(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(37, 99, 235));
        btn.setBorder(BorderFactory.createLineBorder(new Color(37, 99, 235)));
        btn.setFocusPainted(false);
    }

    void addHover(JButton btn) {

        btn.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                if (btn == createBtn) {
                    btn.setBackground(new Color(29, 78, 216));
                } else {
                    btn.setForeground(Color.WHITE); // ⭐ TEXT WHITE ON HOVER
                    btn.setBackground(new Color(37, 99, 235));
                }
            }

            public void mouseExited(MouseEvent e) {
                if (btn == createBtn) {
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

        if (ae.getSource() == loginBtn) {
        	
            new LoginPage();
            dispose();
        }

        if (ae.getSource() == createBtn) {
            registerUser();
        }
    
    }

    public static void main(String[] args) {
        new SignupPage();
    }
}