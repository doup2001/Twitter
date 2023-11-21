package ui;

import MVC.Controller;
import ui.OtherBoardUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FollowUI extends JFrame {

    private String id;
    private Controller controller = new Controller();

    public FollowUI(String id) {
        this.id = id;
        setBounds(500, 120, 400, 300);
        setTitle("팔로워 / 팔로잉 목록");
        getContentPane().setLayout(new BorderLayout());
        setResizable(false);


        JPanel jpMain = new JPanel();
        jpMain.setLayout(new BorderLayout());

        ArrayList<String> allUserList = controller.getAllUser(id);

        JPanel jp3 = createUserPanel(allUserList);

        jpMain.add(jp3, BorderLayout.NORTH);
        getContentPane().add(jpMain, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createButtonPanel(String buttonText, String frameTitle, ArrayList<String> userList) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 0, 0));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(id);
        label.setForeground(new Color(254, 255, 255));
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFollowList(frameTitle, userList);
            }
        });
        buttonPanel.add(label);
        buttonPanel.add(button);
        return buttonPanel;
    }

    private JPanel createUserPanel(ArrayList<String> userList) {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(userList.size(), 3, 10, 10));

        JLabel[] userId = new JLabel[1000];
        JButton[] userBoardButton = new JButton[1000];
        JButton[] followButton = new JButton[1000];

        for (int i = 0; i < userList.size(); i++) {
            String userid = userList.get(i);
            String followState = controller.setFollowButton(id, userid);
            userId[i] = new JLabel("         " + userid);
            userBoardButton[i] = createBoardButton(userid);
            followButton[i] = createFollowButton(userid, followState);

            userPanel.add(userId[i]);
            userPanel.add(userBoardButton[i]);
            userPanel.add(followButton[i]);
        }

        return userPanel;
    }

    private JButton createBoardButton(String userid) {
        JButton button = new JButton(userid + "의 피드");
        setupUserBoardButton(button, userid);
        return button;
    }

    private JButton createFollowButton(String userid, String followState) {
        JButton button = new JButton(followState);
        setupFollowButton(button, userid);
        return button;
    }

    private void setupUserBoardButton(JButton button, String userid) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.out.println(userid + "'s Board!");
                OtherBoardUI otherBoardUI = new OtherBoardUI(userid, id);
                otherBoardUI.setVisible(true);
            }
        });
    }

    private void setupFollowButton(JButton button, String userid) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String followState = controller.setFollowButton(id, userid);
                if (followState.equals("follow")) {
                    controller.updateFollow(id, userid);
                    button.setText("unfollow");
                } else if (followState.equals("unfollow")) {
                    controller.updateFollow(id, userid);
                    button.setText("follow");
                }
            }
        });
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
            followerBoardButton[i] = createBoardButton(followerId);

            jp.add(followUserId[i]);
            jp.add(followerBoardButton[i]);
        }

        jpMain.add(jp, BorderLayout.NORTH);
        followListFrame.getContentPane().add(new JScrollPane(jpMain), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FollowUI("exampleUser");
            }
        });
    }
}
