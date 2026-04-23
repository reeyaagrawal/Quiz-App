package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class QuizApp extends JFrame implements ActionListener {
	String tableName;
	String title;

    JLabel header, timerLabel, questionLabel1, questionLabel2;

    JRadioButton[][] options = new JRadioButton[2][4];
    ButtonGroup[] groups = new ButtonGroup[2];

    JButton nextBtn, prevBtn, submitBtn;

    int currentQ = 0;
    int timeLeft = 15;
    Timer timer;


    ArrayList<String[]> questions = new ArrayList<>();

    int userAnswers[];

    JPanel center, questionPanel;

    public QuizApp(String tableName, String title ) {
    	this.tableName=tableName;
    	this.title=title;

        loadFromDatabase();
        userAnswers = new int[questions.size()];
        for (int i = 0; i < userAnswers.length; i++) {
            userAnswers[i] = -1;
        }

        setTitle(title);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        header = new JLabel(title, SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(new Color(0, 32, 96));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setPreferredSize(new Dimension(950, 120));
        header.setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);

        timerLabel = new JLabel("TIME: 15s");
        timerLabel.setForeground(Color.RED);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        center = new JPanel(new BorderLayout());
        center.setBackground(new Color(245, 247, 250));
        add(center, BorderLayout.CENTER);

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        timerPanel.setBackground(new Color(245, 247, 250));
        timerPanel.add(timerLabel);

        center.add(timerPanel, BorderLayout.NORTH);

        questionPanel = new JPanel();
        questionPanel.setBackground(new Color(245, 247, 250));

        center.add(questionPanel, BorderLayout.CENTER);

        for (int i = 0; i < 2; i++) groups[i] = new ButtonGroup();

        createUI();

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        prevBtn = new JButton("Previous");
        nextBtn = new JButton("Next");
        submitBtn = new JButton("Submit");

        styleButton(prevBtn);
        styleButton(nextBtn);
        styleButton(submitBtn);

        prevBtn.addActionListener(this);
        nextBtn.addActionListener(this);
        submitBtn.addActionListener(this);

        bottom.add(prevBtn);
        bottom.add(nextBtn);
        bottom.add(submitBtn);

        add(bottom, BorderLayout.SOUTH);


        loadQuestion();
        startTimer();

        setVisible(true);
    }

    
   
    void createUI() {

        questionPanel.removeAll();

        if (getWidth() > 1100) {
            questionPanel.setLayout(new GridLayout(2, 1, 15, 15));
        } else {
            questionPanel.setLayout(new GridLayout(1, 1));
        }

        questionPanel.add(createBlock(0));

        if (getWidth() > 1100) {
            questionPanel.add(createBlock(1));
        }

        questionPanel.revalidate();
        questionPanel.repaint();
    }

    JPanel createBlock(int index) {

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel q = new JLabel();
        q.setBounds(20, 20, 800, 25);
        q.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panel.add(q);

        int y = 60;

        for (int i = 0; i < 4; i++) {

            JRadioButton rb = new JRadioButton();
            rb.setBounds(20, y, 650, 25);
            rb.setBackground(Color.WHITE);

            int optionIndex = i;

            rb.addActionListener(e -> {
                int qIndex = currentQ + index;
                if (qIndex < questions.size()) {
                    userAnswers[qIndex] = optionIndex;
                }
            });
            groups[index].add(rb);
            panel.add(rb);

            options[index][i] = rb;

            y += 35;
        }
        if (index == 0) questionLabel1 = q;
        if (index == 1) questionLabel2 = q;


        return panel;
    }

    void loadFromDatabase() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quiz_app",
                "root",
                "reeyA@27"
            );

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT question, option_a, option_b, option_c, option_d FROM " + tableName
            );

            while(rs.next()) {
                String[] row = new String[5];

                row[0] = rs.getString("question");
                row[1] = rs.getString("option_a");
                row[2] = rs.getString("option_b");
                row[3] = rs.getString("option_c");
                row[4] = rs.getString("option_d");

                questions.add(row);
            }

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    void loadQuestion() {
        createUI();

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions found.");
            return;
        }

        prevBtn.setVisible(currentQ > 0);


        if (currentQ < questions.size()) {
            String[] q1 = questions.get(currentQ);
            if (questionLabel1 != null) {
                questionLabel1.setText("Q" + (currentQ + 1) + ": " + q1[0]);
            }
            groups[0].clearSelection();
            for (int i = 0; i < 4; i++) {
                if (options[0][i] != null) {  // ⭐ NULL CHECK
                    options[0][i].setText(q1[i + 1]);
                    options[0][i].setSelected(userAnswers[currentQ] == i);
                }
            }
        }

        int q2Index = currentQ + 1;
        if (getWidth() > 1100 && q2Index < questions.size() && questionLabel2 != null) {
            String[] q2 = questions.get(q2Index);
            questionLabel2.setText("Q" + (q2Index + 1) + ": " + q2[0]);
            groups[1].clearSelection();
            for (int i = 0; i < 4; i++) {
                if (options[1][i] != null) {  // ⭐ NULL CHECK
                    options[1][i].setText(q2[i + 1]);
                    options[1][i].setSelected(userAnswers[q2Index] == i);
                }
            }
        }
    }
    
    
    void startTimer() {

        if (timer != null) timer.stop();

        timeLeft = 15;

        timer = new Timer(1000, e -> {

            timeLeft--;
            timerLabel.setText("TIME: " + timeLeft + "s");

            if (timeLeft <= 0) next();
        });

        timer.start();
    }

    void next() {

    	int step = (getWidth() > 1100) ? 2 : 1;

        if (currentQ + step < questions.size()) {
            currentQ += step;
            loadQuestion();
            startTimer();
        }
    }

    void prev() {

        int step = (getWidth() > 1100) ? 2 : 1;

        if (currentQ - step >= 0) {
            currentQ -= step;
            loadQuestion();
            startTimer();
        }
    }
    
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == nextBtn) next();
        if (e.getSource() == prevBtn) prev();

        if (e.getSource() == submitBtn) {
            JOptionPane.showMessageDialog(this, "Quiz Submitted!");
            new Result(questions, userAnswers,tableName,title);
        }
    }

    void styleButton(JButton b) {
        b.setBackground(new Color(37, 99, 235));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
    }

}