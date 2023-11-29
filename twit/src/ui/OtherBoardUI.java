package ui;

import MVC.Controller;
import MVC.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OtherBoardUI extends JFrame {

    String otherUserID;
    String userID;
    private JPanel contentPane;
    private DefaultListModel listModel;
    private JList list;
    private JTextArea writeArea;

    private Controller controller;

    public OtherBoardUI(String otherUserID, String userID) {
        this.otherUserID = otherUserID;
        this.userID = userID;
        controller = new Controller();

        //기본 사이즈
        setSize(500, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);

        // 패널 구성
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 피드 주인 표시
        JLabel userIdLabel = new JLabel(otherUserID + "님의 피드");
        userIdLabel.setFont(new Font("Nanum Gothic", Font.BOLD, 16));
        userIdLabel.setForeground(new Color(254, 255, 255));
        userIdLabel.setBounds(40, 130, 200, 30);
        contentPane.add(userIdLabel);

        // 글 쓰는 곳
        writeArea = new JTextArea("");
        writeArea.setBounds(40, 170, 320, 73);
        writeArea.setBorder(BorderFactory.createCompoundBorder(
                writeArea.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        contentPane.add(writeArea);

        list = new JList();
        list.setBounds(40, 300, 320, 300);
        list.setForeground(new Color(254, 255, 255));
        list.setBackground(new Color(20, 20, 20));
        contentPane.add(list);

        // 댓글 작성
        JButton commentWriteButton = new JButton("댓글 작성하기");
        commentWriteButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        commentWriteButton.setBounds(145, 250, 100, 30);
        commentWriteButton.setForeground(new Color(254, 255, 255));
        contentPane.add(commentWriteButton);

        commentWriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeComment();
            }
        });

        // 댓글 보기
        JButton commentReadButton = new JButton("댓글 보기");
        commentReadButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        commentReadButton.setBounds(240, 250, 100, 30);
        commentReadButton.setForeground(new Color(254, 255, 255));
        contentPane.add(commentReadButton);

        commentReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readComments();
            }
        });


        // 새로고침
        JButton articleReadButton = new JButton("새로고침");
        articleReadButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleReadButton.setBounds(60, 250, 90, 30);
        articleReadButton.setForeground(new Color(254, 255, 255));
        contentPane.add(articleReadButton);

        articleReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePostList();
            }
        });

        // 팔로우 버튼
        JButton followButton = createFollowButton(otherUserID);
        followButton.setBounds(275, 130, 90, 30);
        followButton.setForeground(new Color(254, 255, 255));
        contentPane.add(followButton);


        //로고 사진
        JLabel MainLogo = new JLabel("");
        Image img = new ImageIcon(this.getClass().getResource("/img/mainLogo.png")).getImage();
        MainLogo.setIcon(new ImageIcon(img));
        MainLogo.setBounds(184, 25, 30, 30);
        contentPane.add(MainLogo);


        // 뒤로가기 myhome으로
        JButton BacktoMy = new JButton(new ImageIcon(getClass().getResource("/img/backArrow.png")));
        BacktoMy.setBounds(30, 25, 30, 30);
        contentPane.add(BacktoMy);
        BacktoMy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // main으로 이동
                dispose();
                MainBoardUI mainBoardUI = new MainBoardUI(userID);
                mainBoardUI.setTitle("MyHome");
                mainBoardUI.setVisible(true);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
        updatePostList();
    }

    private void updatePostList() {
        ArrayList<Post> arr = controller.readPost(otherUserID);
        arr = controller.listSort(arr);

        if (arr.size() == 0) {
            listModel = new DefaultListModel();
            String info = otherUserID + "님은 아직 트윗하지 않았습니다";
            listModel.addElement(info);
        } else {
            listModel = new DefaultListModel();
            for (Post res : arr) {
                LocalDateTime createdAt = getCreatedAtFromDatabase(res.getNum());
                String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
                String post = res.getNum() + " " +"(" + res.getId() + ") " + formattedTime + " ] " + res.getArticle() + "\n";
                listModel.addElement(post);
            }
        }
        list.setModel(listModel);
    }

    private JButton createFollowButton(String userid) {
        JButton button = new JButton(controller.setFollowButton(userID, userid));
        setupFollowButton(button, userid);
        return button;
    }

    // 팔로우 활성화의 여부에 따른 글자
    private void setupFollowButton(JButton button, String userid) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String followState = controller.setFollowButton(userID, userid);
                if (followState.equals("follow")) {
                    controller.updateFollow(userID, userid);
                    button.setText("unfollow");
                } else if (followState.equals("unfollow")) {
                    controller.updateFollow(userID, userid);
                    button.setText("follow");
                }
            }
        });
    }

    // 글 시간 가져오기
    private LocalDateTime getCreatedAtFromDatabase(int num) {
        LocalDateTime createdAt = null;
        try (Connection con = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD)) {
            PreparedStatement st = con.prepareStatement("SELECT createdAt FROM article WHERE num = ?");
            st.setInt(1, num);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return createdAt;
    }

    private void writeComment() {
        String postText = (String) list.getSelectedValue();
        if (postText != null && !postText.isEmpty()) {
            String[] strArr = postText.split(" ");
            int articleNum = Integer.parseInt(String.valueOf(strArr[0]));

            String commentText = writeArea.getText();
            if (!commentText.isEmpty()) {
                // 댓글 작성 로직 구현
                controller.writeComment(userID, otherUserID, articleNum, commentText);
                // 댓글 작성 후 목록 업데이트
                updatePostList();
                // 댓글 작성 후 입력 영역 초기화
                writeArea.setText("");
            }
        }
    }

    private void readComments() {
        String postText = (String) list.getSelectedValue();
        if (postText != null && !postText.isEmpty()) {
            String[] strArr = postText.split(" ");
            int articleNum = Integer.parseInt(String.valueOf(strArr[0]));

            // 댓글 조회 및 표시 로직 구현
            ArrayList<String> comments = controller.readComments(articleNum);
            if (!comments.isEmpty()) {
                StringBuilder commentText = new StringBuilder("댓글 목록:\n");
                for (String comment : comments) {
                    commentText.append(comment).append("\n");
                }
                JOptionPane.showMessageDialog(this, commentText.toString(), "댓글 목록", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "댓글이 없습니다.", "댓글 목록", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
