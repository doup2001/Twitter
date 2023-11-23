package ui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;


public class RegistrationUI extends JFrame {



    private JPanel contentPane;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField id;
    private JPasswordField passwordField;
    private JButton btnNewButton1;
    private JButton btnNewButton2;

    private JRadioButton rdbtnMale;
    private JRadioButton rdbtnFemale;
    private JTextField email;
    private JTextField birthdate;

    //버튼 디자인
    public class RoundedButton extends JButton {
        public RoundedButton() { super(); decorate(); }
        public RoundedButton(String text) { super(text); decorate(); }
        public RoundedButton(Action action) { super(action); decorate(); }
        public RoundedButton(Icon icon) { super(icon); decorate(); }
        public RoundedButton(String text, Icon icon) { super(text, icon); decorate(); }
        protected void decorate() { setBorderPainted(false); setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Color c=new Color(239,243,244); //배경색 결정
            Color o=new Color(15,20,25); //글자색 결정
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isArmed()) { graphics.setColor(c.darker()); }
            else if (getModel().isRollover()) { graphics.setColor(c.brighter()); }
            else { graphics.setColor(c); }
            graphics.fillRoundRect(0, 0, width, height, 40, 40);
            FontMetrics fontMetrics = graphics.getFontMetrics();
            Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();
            int textX = (width - stringBounds.width) / 2;
            int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();
            graphics.setColor(o);
            graphics.setFont(getFont());
            graphics.drawString(getText(), textX, textY);
            graphics.dispose();
            super.paintComponent(g);
        }
    }

    public RegistrationUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewUserRegister = new JLabel("지금 트위터에 가입하세요");
        lblNewUserRegister.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewUserRegister.setFont(new Font("Nanum Gothic", Font.BOLD, 20));
        lblNewUserRegister.setForeground(new Color(221, 228, 232));
        lblNewUserRegister.setBounds(14, 50, 373, 50);
        contentPane.add(lblNewUserRegister);

        JLabel lblName = new JLabel("이름 / First Name");
        lblName.setForeground(new Color(254, 255, 255));
        lblName.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblName.setBounds(72, 120, 100, 27);
        contentPane.add(lblName);

        JLabel lblNewLabel = new JLabel("성 / Last Name");
        lblNewLabel.setForeground(new Color(254, 255, 255));
        lblNewLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblNewLabel.setBounds(72, 170, 100, 27);
        contentPane.add(lblNewLabel);

        firstname = new JTextField();
        firstname.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        firstname.setBounds(69, 140, 262, 33);
        contentPane.add(firstname);
        firstname.setColumns(10);

        lastname = new JTextField();
        lastname.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        lastname.setBounds(69, 190, 262, 33);
        contentPane.add(lastname);
        lastname.setColumns(10);

        id = new JTextField();
        id.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        id.setBounds(69, 240, 262, 33);
        contentPane.add(id);
        id.setColumns(10);

        JLabel lblUsername = new JLabel("아이디 / ID");
        lblUsername.setForeground(new Color(254, 255, 255));
        lblUsername.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblUsername.setBounds(72, 220, 78, 27);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("비밀번호 / Password");
        lblPassword.setForeground(new Color(254, 255, 255));
        lblPassword.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblPassword.setBounds(72, 270, 130, 27);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        passwordField.setBounds(69, 290, 262, 33);
        contentPane.add(passwordField);

// 성별 선택 라디오 버튼
        JLabel sexLabel = new JLabel("성별 선택");
        sexLabel.setForeground(new Color(254, 255, 255));
        sexLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        sexLabel.setBounds(72, 320, 130, 27);
        contentPane.add(sexLabel);

        rdbtnMale = new JRadioButton("남성");
        rdbtnFemale = new JRadioButton("여성");
        ButtonGroup genderGroup = new ButtonGroup();
        contentPane.add(sexLabel);
        genderGroup.add(rdbtnMale);
        genderGroup.add(rdbtnFemale);

        rdbtnMale.setForeground(new Color(254, 255, 255));
        rdbtnMale.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        rdbtnMale.setBounds(69, 345, 65, 23);
        contentPane.add(rdbtnMale);

        rdbtnFemale.setForeground(new Color(254, 255, 255));
        rdbtnFemale.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        rdbtnFemale.setBounds(146, 345, 65, 23);
        contentPane.add(rdbtnFemale);

// 이메일
        email = new JTextField();
        email.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        email.setBounds(69, 390, 262, 33);
        contentPane.add(email);
        email.setColumns(10);

        JLabel lblEmail = new JLabel("이메일 / Email");
        lblEmail.setForeground(new Color(254, 255, 255));
        lblEmail.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEmail.setBounds(72, 370, 100, 27);
        contentPane.add(lblEmail);

// 생년월일
        birthdate = new JTextField();
        birthdate.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        birthdate.setBounds(69, 440, 262, 33);
        contentPane.add(birthdate);
        birthdate.setColumns(10);

        JLabel lblBirthdate = new JLabel("생년월일 / Birthdate");
        lblBirthdate.setForeground(new Color(254, 255, 255));
        lblBirthdate.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblBirthdate.setBounds(72, 420, 130, 27);
        contentPane.add(lblBirthdate);

        // 가입하기 버튼
        btnNewButton1 = new RoundedButton("가입하기");
        btnNewButton1.setBackground(new Color(197, 225, 255));
        btnNewButton1.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        btnNewButton1.setBounds(69, 510, 262, 40);
        contentPane.add(btnNewButton1);

        // 이미 계정이 있으신가요 버튼
        btnNewButton2 = new RoundedButton("이미 계정이 있으신가요?");
        btnNewButton2.setBackground(new Color(197, 225, 255));
        btnNewButton2.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        btnNewButton2.setBounds(69, 570, 262, 40);
        contentPane.add(btnNewButton2);


        // 로고
        JLabel RegiLogo = new JLabel("");
        Image img = new ImageIcon(this.getClass().getResource("/img/registrationIcon.png")).getImage();
        RegiLogo.setIcon(new ImageIcon(img));
        RegiLogo.setBounds(175, 10, 50, 50);
        contentPane.add(RegiLogo);

        btnNewButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstName = firstname.getText();
                String lastName = lastname.getText();
                String ID = id.getText();
                String password = passwordField.getText();
                String gender = rdbtnMale.isSelected() ? "남성" : "여성";
                String userEmail = email.getText();
                String userBirthdate = birthdate.getText();

                int firstName_len = firstName.length();
                int lastName_len = lastName.length();
                int ID_len = ID.length();
                int password_len = password.length();

                String msg = "" + ID;
                msg += " \n";

                if (firstName_len == 0) {
                    JOptionPane.showMessageDialog(btnNewButton1, "이름을 입력해주세요.", "Invalid First Name", JOptionPane.PLAIN_MESSAGE);
                } else if (lastName_len == 0) {
                    JOptionPane.showMessageDialog(btnNewButton1, "성(Last Name)을 입력해주세요.", "Invalid Last Name", JOptionPane.PLAIN_MESSAGE);
                } else if (ID_len < 5) {
                    JOptionPane.showMessageDialog(btnNewButton1, "아이디는 최소 5글자 이상이어야 합니다.", "Invalid Username", JOptionPane.PLAIN_MESSAGE);
                } else if (password_len < 5) {
                    JOptionPane.showMessageDialog(btnNewButton1, "비밀번호는 최소 5글자 이상이어야 합니다.", "Invalid Password", JOptionPane.PLAIN_MESSAGE);
                } else if (!userEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(btnNewButton1, "올바른 이메일 양식을 입력해주세요.", "Invalid Email", JOptionPane.PLAIN_MESSAGE);
                // 생년월일 형식 검증
                } else if (!userBirthdate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    JOptionPane.showMessageDialog(btnNewButton1, "올바른 생년월일 양식을 입력해주세요. (올바른 양식 : HHHH - MM -DD)", "Invalid Birthdate", JOptionPane.PLAIN_MESSAGE);
                } else {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/twit", "root", "David100894@");

                        // 이미 존재하는 계정인지 확인
                        String checkQuery = "SELECT * FROM account WHERE id = '" + ID + "'";
                        try (Statement checkStatement = connection.createStatement();
                             ResultSet resultSet = checkStatement.executeQuery(checkQuery)) {
                            if (resultSet.next()) {
                                JOptionPane.showMessageDialog(btnNewButton1, "이미 존재하는 계정입니다.");
                            } else {
                                // 계정 추가
                                String insertQuery = "INSERT INTO account (firstname, lastname, id, password, birthdate, email, gender) VALUES ('" + firstName + "','" + lastName + "','" + ID + "','" + password + "','" + userBirthdate + "','" + userEmail + "','" + gender + "');";
                                try (Statement sta = connection.createStatement()) {
                                    int x = sta.executeUpdate(insertQuery);

                                    if (x > 0) {
                                        JOptionPane.showMessageDialog(btnNewButton1, "반갑습니다, " + msg + "계정이 성공적으로 생성되었습니다.");
                                        dispose();
                                        LoginUI ul = new LoginUI();
                                        ul.setTitle("Login");
                                        ul.setVisible(true);
                                    }
                                }
                            }
                        }
                        connection.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        btnNewButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginUI ul = new LoginUI();
                ul.setTitle("Login");
                ul.setVisible(true);
            }
        });

        contentPane.add(btnNewButton2);

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }

}
