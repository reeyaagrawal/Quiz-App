package application;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ReviewAnswers extends JFrame {

    JPanel mainPanel;
    Color green = new Color(34, 197, 94);
    Color red = new Color(239, 68, 68);
    Color primary = new Color(99, 102, 241);

    public ReviewAnswers(ArrayList<String[]> questions, int[] userAnswers, String tableName) {

        setTitle("Review Answers");
        setSize(750, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JLabel header = new JLabel("REVIEW ANSWERS", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI Black", Font.BOLD, 28));
        header.setOpaque(true);
        header.setBackground(primary);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(750, 80));
        add(header, BorderLayout.NORTH);


        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 247, 255));

        loadAnswers(questions, userAnswers, tableName);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    void loadAnswers(ArrayList<String[]> questions, int[] userAnswers, String tableName) {

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz_app",
                    "root",
                    "reeyA@27"
            );

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
            	    "SELECT question, option_a, option_b, option_c, option_d, answer FROM " + tableName
            	);

            int i = 0;

            while (rs.next() && i < questions.size()) {

            	String q = rs.getString("question");

            	String[] options = {
            	    rs.getString("option_a"),
            	    rs.getString("option_b"),
            	    rs.getString("option_c"),
            	    rs.getString("option_d")
            	};

            	String correctLetter = rs.getString("answer");
//            	System.out.println("Question " + i + " userAnswers = " + userAnswers[i]);
//            	String correctText = getOptionText(correctLetter, options);

            	String correctText;

            	if(correctLetter.equals("1"))
            	    correctText = options[0];
            	else if(correctLetter.equals("2"))
            	    correctText = options[1];
            	else if(correctLetter.equals("3"))
            	    correctText = options[2];
            	else if(correctLetter.equals("4"))
            	    correctText = options[3];
            	else
            	    correctText = getOptionText(correctLetter, options);
            	
            	
            	String userText;
            	if(userAnswers[i] >= 0 && userAnswers[i] < 4)
            	    userText = options[userAnswers[i]];
            	else
            	    userText = "Not Answered";

            	mainPanel.add(createCard(q, userText, correctText));
                mainPanel.add(Box.createVerticalStrut(15));

                i++;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    JPanel createCard(String q, String user, String correct) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        boolean isCorrect = user.equalsIgnoreCase(correct);

        JLabel qLabel = new JLabel("Q: " + q);
        qLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel userLabel = new JLabel("Your Answer: " + user);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        if(isCorrect)
            userLabel.setForeground(green);
        else
            userLabel.setForeground(red);

        JLabel correctLabel = new JLabel("Correct Answer: " + correct);
        correctLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        correctLabel.setForeground(green);

        card.add(qLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(correctLabel);

        return card;
    }

    String convert(int i) {
        switch(i) {
            case 1: return "A";
            case 2: return "B";
            case 3: return "C";
            case 4: return "D";
            default: return "Not Answered";
        }
    }
    String getOptionText(String letter, String[] options) {
        if(letter.equalsIgnoreCase("A")) return options[0];
        if(letter.equalsIgnoreCase("B")) return options[1];
        if(letter.equalsIgnoreCase("C")) return options[2];
        if(letter.equalsIgnoreCase("D")) return options[3];
        return "Not Answered";
    }
    
}