package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Result extends JFrame implements ActionListener {

    int score = 0;
    int total = 0;
    ArrayList<String> correctAnswers = new ArrayList<>();
    ArrayList<String[]> questions;
    int[] userAnswers;
    String tableName,  quizTitle;
    JPanel answerPanel;

    JButton retry ,newCat,review;

    
    Color primary = new Color(99, 102, 241);
    Color cyan = new Color(6, 182, 212);
    Color green = new Color(34, 197, 94);

    
    	public Result(ArrayList<String[]> questions, int[] userAnswers, String tableName, String quizTitle) {
    	    this.questions = questions;
    	    this.userAnswers = userAnswers;
    	    this.tableName = tableName;
    	    this.quizTitle = quizTitle;
        
        setTitle("Quiz Result");
        setSize(800, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.WHITE);

        calculateScore();


        JLabel header = new JLabel("QUIZ RESULT", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        header.setOpaque(true);
        header.setBackground(primary);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(800, 90));

        add(header, BorderLayout.NORTH);


        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(new Color(245, 247, 255));

        center.add(Box.createVerticalStrut(20));

        JLabel scoreLabel = new JLabel(String.valueOf(score), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 65));
        scoreLabel.setForeground(primary);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreText = new JLabel("YOUR SCORE");
        scoreText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreText.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(scoreLabel);
        center.add(scoreText);

        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("application/done.png"));
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);

            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            center.add(Box.createVerticalStrut(15));
            center.add(imgLabel);

        } catch (Exception e) {
            JLabel fallback = new JLabel("Image not found");
            fallback.setAlignmentX(Component.CENTER_ALIGNMENT);
            center.add(fallback);
        }


        JLabel note = new JLabel();
        note.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        note.setForeground(new Color(75, 85, 99));

        int percent = 0;

        if (total > 0) {
            percent = (score * 100) / total;
        }
        
        
        if (percent >= 80) {
            note.setText(" Excellent! You're mastering this topic!");
        } else if (percent >= 50) {
            note.setText(" Good job! Keep practicing to improve more.");
        } else {
            note.setText(" Don't worry! Try again and you'll improve.");
        }

        center.add(Box.createVerticalStrut(10));
        center.add(note);
        
        // IMAGE 
        try {
            ImageIcon icon = new ImageIcon("option.jpeg");
            Image img = icon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            center.add(Box.createVerticalStrut(15));
            center.add(imgLabel);

        } catch (Exception e) {
            JLabel fallback = new JLabel("Image not found");
            fallback.setAlignmentX(Component.CENTER_ALIGNMENT);
            center.add(fallback);
        }


        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 247, 255));

         retry = createButton("Retry Quiz", primary);
         newCat = createButton("New Category", cyan);
         review = createButton("Review Answers", green);
         
         retry.addActionListener(this);
         newCat.addActionListener(this);
         review.addActionListener(this);

        btnPanel.add(retry);
        btnPanel.add(newCat);
        btnPanel.add(review);

        center.add(Box.createVerticalStrut(25));
        center.add(btnPanel);

        add(center, BorderLayout.CENTER);



        setVisible(true);
    }

    	
    void calculateScore() {

        total = questions.size();

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz_app",
                    "root",
                    "reeyA@27"
            );

            Statement st = con.createStatement();
            if (tableName == null) return;
            ResultSet rs = st.executeQuery("SELECT answer FROM " + tableName);



            correctAnswers.clear();

            while (rs.next()) {
            	correctAnswers.add(rs.getString("answer"));
            }

            con.close();

            for (int i = 0; i < total && i < correctAnswers.size(); i++) {

                if (userAnswers[i] == -1) continue;

                String correct = correctAnswers.get(i);
                String user = convert(userAnswers[i]);

                if(correct.equals("1")) correct = "A";
                else if(correct.equals("2")) correct = "B";
                else if(correct.equals("3")) correct = "C";
                else if(correct.equals("4")) correct = "D";

                if (correct.equalsIgnoreCase(user)) {
                    score++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void loadAnswerDetails() {

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz_app",
                    "root",
                    "reeyA@27"
            );

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT question, answer FROM "+tableName);

            int i = 0;

            while (rs.next() && i < questions.size()) {

                answerPanel.add(createBox(
                        rs.getString("question"),
                        convert(userAnswers[i]),
                        rs.getString("answer")
                ));

                i++;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    JPanel createBox(String q, String user, String correct) {

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);

        boolean ok = user.equalsIgnoreCase(correct);

        JLabel ql = new JLabel("Q: " + q);
        JLabel ul = new JLabel("Your Answer: " + user);
        JLabel cl = new JLabel("Correct: " + correct);

        cl.setForeground(ok ? green : Color.RED);

        p.add(ql);
        p.add(ul);
        p.add(cl);
        p.add(new JSeparator());

        return p;
    }


    JButton createButton(String text, Color c) {

        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(170, 45));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.setBackground(c);
        b.setForeground(Color.WHITE);

        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(c.darker());
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(c);
            }
        });

        return b;
    }

    String convert(int i) {
        if(i == 0) return "A";
        if(i == 1) return "B";
        if(i == 2) return "C";
        if(i == 3) return "D";
        return "Not Answered";
    }

    public static void main(String[] args) {

        ArrayList<String[]> q = new ArrayList<>();
        int[] ans = new int[10];
        String tablename = null,quizTitle = null;

        new Result(q, ans, tablename, quizTitle);
    }


	public void actionPerformed(ActionEvent e) {


		if(e.getSource()==newCat) {
			new Field();
			dispose();
		}else if(e.getSource()==retry) {
			new QuizApp(tableName, quizTitle);
			dispose();
		}else if(e.getSource()==review){
		    setVisible(false);
		    new ReviewAnswers(questions, userAnswers, tableName);
		}
		
	}
}