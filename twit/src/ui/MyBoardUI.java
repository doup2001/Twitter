package ui;

import MVC.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class MyBoardUI extends JFrame {

    String ID;
    private JPanel contentPane;
    private JTextArea writeArea;
    private JTextArea readArea;
    private JList list;
    private int articleNum;
    DefaultListModel listModel;
    private Controller controller;  // Controller 클래스의 인스턴스 추가

    public MyBoardUI(String ID) {
        this.ID = ID;
        this.controller = new Controller();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));

        JLabel userIdLabel = new JLabel(ID + "의 피드");
        userIdLabel.setFont(new Font("Nanum Gothic", Font.BOLD, 16));
        userIdLabel.setForeground(new Color(254, 255, 255));
        userIdLabel.setBounds(40, 130, 200, 30);
        contentPane.add(userIdLabel);

        JButton followerButton = new JButton("팔로워");
        followerButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));

        followerButton.setBounds(220, 130, 80, 30);
        contentPane.add(followerButton);

        JButton followingButton = new JButton("팔로잉");
        followingButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));

        followingButton.setBounds(300, 130, 80, 30);
        contentPane.add(followingButton);

        followerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> followerList = controller.getFollower(ID);
                showFollowList("내 팔로워 목록", followerList);
            }
        });

        followingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> followingList = controller.getFollowing(ID);
                showFollowList("내 팔로잉 목록", followingList);
            }
        });

        JButton btnNewButton = new JButton("로그아웃");
        btnNewButton.setForeground(new Color(220, 45, 59));
        btnNewButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        btnNewButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        JFrame frame = new JFrame("Logout Window");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Yes", "No"};
                int a = JOptionPane.showOptionDialog(frame, "Are you sure?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    LoginUI obj = new LoginUI();
                    obj.setTitle("User-Login");
                    obj.setVisible(true);
                }
            }
        });

        btnNewButton.setBounds(285, 20, 100, 30);
        contentPane.add(btnNewButton);

        JButton button = new JButton("개인정보 변경");
        button.setBackground(UIManager.getColor("Button.disabledForeground"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 기존 비밀번호 확인
                String oldPassword = JOptionPane.showInputDialog("기존 비밀번호를 입력하세요:");

                if (isOldPasswordCorrect(oldPassword)) {
                    // 비밀번호가 일치하는 경우
                    ChangeInfoUI infoUI = new ChangeInfoUI(ID);
                    infoUI.setTitle("Change Password");
                    infoUI.setVisible(true);
                } else {
                    // 비밀번호가 일치하지 않는 경우
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        button.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        button.setBounds(285, 47, 100, 30);
        contentPane.add(button);

        Controller controller = new Controller();

        setContentPane(contentPane);
        contentPane.setLayout(null);

        writeArea = new JTextArea("");
        writeArea.setBounds(40, 170, 320, 73);
        writeArea.setBorder(BorderFactory.createCompoundBorder(
                writeArea.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        contentPane.add(writeArea);

        readArea = new JTextArea("read");
        readArea.setBounds(0, 400, 350, 400);

        list = new JList();
        list.setBounds(40, 300, 320, 300);
        list.setForeground(new Color(254, 255, 255));
        list.setBackground(new Color(20, 20, 20));
        contentPane.add(list);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    String postText = (String) list.getSelectedValue();
                    if (postText != null) {
                        String[] strArr = postText.split(" ");
                        articleNum = Integer.parseInt(String.valueOf(strArr[0]));
                    }
                }
            }
        });

        ArrayList<Post> arr;
        arr = controller.readPost(ID);
        arr = controller.listSort(arr);

        if (arr.size() == 0 ) {
            listModel = new DefaultListModel();
            String info = ID + "님은 아직 트윗하지 않았습니다";
            listModel.addElement(info);
        }
        else {
            listModel = new DefaultListModel();
            for (Post res : arr) {
                String post = res.getNum() + "   " + "(" + res.getId() + ")" + " \t " + res.getArticle() + "\n";

                listModel.addElement(post);
            }
        }
        list.setModel(listModel);

        JButton articleWriteButton = new JButton("글 작성하기");
        articleWriteButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleWriteButton.setBounds(210, 250, 100, 30);
        contentPane.add(articleWriteButton);

        articleWriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String article = writeArea.getText();
                int num = controller.getArticleNextNum();
                controller.insertPost(new Post(num, ID, article));
                readArea.setText("");
                list.clearSelection(); // clearSelection() 호출 위치 변경

                ArrayList<Post> arr;
                arr = controller.readPost(ID);
                arr = controller.listSort(arr);

                if (arr.size() == 0) {
                    listModel = new DefaultListModel();
                    String info = ID + "님은 아직 트윗하지 않았습니다";
                    listModel.addElement(info);
                } else {
                    listModel = new DefaultListModel();
                    for (Post res : arr) {
                        String post = res.getNum() + "   " + "(" + res.getId() + ")" + " \t " + res.getArticle() + "\n";
                        listModel.addElement(post);
                    }
                }
                list.setModel(listModel);
            }
        });

        JButton articleReadButton = new JButton("새로고침");
        articleReadButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleReadButton.setBounds(100, 250, 90, 30);
        contentPane.add(articleReadButton);

        articleReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Post> arr = controller.readPost(ID);
                arr = controller.listSort(arr);

                if (arr.size() == 0) {
                    listModel = new DefaultListModel();
                    String info = ID + "님은 아직 트윗하지 않았습니다";
                    listModel.addElement(info);
                } else {
                    listModel = new DefaultListModel();
                    for (Post res : arr) {
                        String post =  "   " + "(" + res.getId() + ")" + " \t " + res.getArticle() + "\n";

                        listModel.addElement(post);
                    }
                }
                list.setModel(listModel);
            }
        });

        JButton articleDeleteButton = new JButton("글 삭제하기");
        articleDeleteButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleDeleteButton.setBounds(90, 630, 100, 30);
        contentPane.add(articleDeleteButton);

        articleDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.deletePost(articleNum);
                System.out.println(articleNum + "'s post deleted!");
            }
        });

        JButton articleUpdateButton = new JButton("글 수정하기");
        articleUpdateButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleUpdateButton.setBounds(210, 630, 100, 30);
        contentPane.add(articleUpdateButton);

        articleUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeArticleUI changePostUI = new ChangeArticleUI(articleNum);
                changePostUI.setTitle("Change Post");
                changePostUI.setVisible(true);
            }
        });

        JLabel MainLogo = new JLabel("");
        Image img = new ImageIcon(this.getClass().getResource("/img/mainLogo.png")).getImage();
        MainLogo.setIcon(new ImageIcon(img));
        MainLogo.setBounds(184, 25, 30, 30);
        contentPane.add(MainLogo);

        JButton BacktoMain = new JButton("back");
        BacktoMain.setBounds(30, 25, 70, 30);
        contentPane.add(BacktoMain);
        BacktoMain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // main으로 이동
                dispose();
                MainBoardUI main = new MainBoardUI(ID);
                main.setTitle("Twitter");
                main.setVisible(true);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }

    private boolean isOldPasswordCorrect(String inputPassword) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/twit", "root", "David100894@");
            PreparedStatement st = con.prepareStatement("SELECT password FROM account WHERE ID=?");
            st.setString(1, ID);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(inputPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    private void showFollowList(String title, ArrayList<String> userList) {
        JFrame followListFrame = new JFrame();
        JPanel jpMain = new JPanel();
        jpMain.setLayout(new BorderLayout());
        JPanel jp = new JPanel();

        followListFrame.setTitle(title);
        followListFrame.setSize(300, 300);
        followListFrame.setVisible(true);
        jp.setLayout(new GridLayout(userList.size(), 2, 10, 10));

        JLabel[] followUserId = new JLabel[1000];
        JButton[] followerBoardButton = new JButton[1000];

        for (int i = 0; i < userList.size(); i++) {
            String followerId = userList.get(i);
            followUserId[i] = new JLabel(followerId);
            followerBoardButton[i] = new JButton(followerId + "의 피드");
            followerBoardButton[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    System.out.println(followerId + "'s Board!");
                    OtherBoardUI otherBoardUI = new OtherBoardUI(followerId, ID);
                    otherBoardUI.setVisible(true);
                }
            });

            jp.add(followUserId[i]);
            jp.add(followerBoardButton[i]);
        }

        jpMain.add(jp, BorderLayout.NORTH);
        followListFrame.getContentPane().add(new JScrollPane(jpMain), BorderLayout.CENTER);
    }

}


