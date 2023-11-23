package ui;

import MVC.Controller;
import MVC.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OtherBoardUI extends JFrame {

    String otherUserID;
    String userID;
    private JPanel contentPane;
    private DefaultListModel listModel;
    private JList list;
    private JTextArea writeArea;
    private JTextArea readArea;
    private Controller controller;

    public OtherBoardUI(String otherUserID, String userID) {
        this.otherUserID = otherUserID;
        this.userID = userID;

        setSize(500, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));

        controller = new Controller();

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel userIdLabel = new JLabel(otherUserID + "님의 피드");
        userIdLabel.setFont(new Font("Nanum Gothic", Font.BOLD, 16));
        userIdLabel.setForeground(new Color(254, 255, 255));
        userIdLabel.setBounds(40, 130, 200, 30);
        contentPane.add(userIdLabel);

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

        JButton articleWriteButton = new JButton("글 작성하기");
        articleWriteButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleWriteButton.setBounds(210, 250, 100, 30);
        contentPane.add(articleWriteButton);

        articleWriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String article = writeArea.getText();
                int num = controller.getArticleNextNum();
                controller.insertPost(new Post(num, otherUserID, article));
                updatePostList();
            }
        });

        JButton articleReadButton = new JButton("새로고침");
        articleReadButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleReadButton.setBounds(100, 250, 90, 30);
        contentPane.add(articleReadButton);

        articleReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePostList();
            }
        });

        // Follow Button
        JButton followButton = createFollowButton(otherUserID);
        followButton.setBounds(250, 130, 90, 30);
        contentPane.add(followButton);

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
                MainBoardUI main = new MainBoardUI(userID);
                main.setTitle("Twitter");
                main.setVisible(true);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
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
                String post = res.getNum() + "   " + "(" + res.getId() + ")" + " \t " + res.getArticle() + "\n";
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
}
