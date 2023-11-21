package ui;

import MVC.Controller;
import MVC.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainBoardUI extends JFrame {

    String ID;
    private JPanel contentPane;
    private DefaultListModel listModel;
    private JList list;
    private JButton articleReadButton;
    private final JButton lowBarFollowingBoardButton;
    private final JButton lowBarFollowButton;
    private final JButton lowBarHisBoard;
    private final JButton searchButton;  // 추가된 부분

    public MainBoardUI(String ID) {
        this.ID = ID;
        Controller controller = new Controller();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0)); // 하늘색 배경

        setContentPane(contentPane);
        contentPane.setLayout(null);

        list = new JList();
        list.setBounds(40, 200, 320, 400);
        list.setBackground(new Color(20, 20, 20));
        list.setForeground(new Color(254, 255, 255));
        contentPane.add(list);

        articleReadButton = new JButton("Refresh");
        articleReadButton.setBounds(310, 120, 80, 30);
        contentPane.add(articleReadButton);

        searchButton = new JButton("Search");  // 추가된 부분
        searchButton.setBounds(200, 620, 80, 40);  // 추가된 부분
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Search 버튼을 누를 때 SearchUI를 생성하여 표시
                SearchUI searchUI = new SearchUI(ID);
                searchUI.setVisible(true);
            }
        });

        ArrayList<Post> arr = controller.readPost(ID);
        ArrayList<String> followArr = controller.getFollowing(ID);
        for (String s : followArr) {
            ArrayList<Post> tempPost = controller.readPost(s);
            arr.addAll(tempPost);
        }
        arr = controller.listSort(arr);

        if (arr.size() == 0) {
            listModel = new DefaultListModel();
            String info = "불러올 글이 없습니다";
            listModel.addElement(info);
        } else {
            listModel = new DefaultListModel();
            for (Post res : arr) {
                String post = res.getNum() + "   " + "(" + res.getId() + ")" + " \t " + res.getArticle() + "\n";
                listModel.addElement(post);
            }
        }
        list.setModel(listModel);

        articleReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Post> arr = controller.readPost(ID);
                ArrayList<String> followArr = controller.getFollowing(ID);
                for (String s : followArr) {
                    ArrayList<Post> tempPost = controller.readPost(s);
                    arr.addAll(tempPost);
                }
                arr = controller.listSort(arr);

                if (arr.size() == 0) {
                    listModel = new DefaultListModel();
                    String info = "불러올 글이 없습니다";
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

        lowBarFollowingBoardButton = new JButton("MainBoard");
        lowBarFollowingBoardButton.setBounds(30, 620, 80, 40);
        lowBarFollowingBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("MainBoard!");
            }
        });

        lowBarFollowButton = new JButton("All User");
        lowBarFollowButton.setBounds(115, 620, 80, 40);
        lowBarFollowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("AllUser!");
                AllUserUI followUI = new AllUserUI(ID);
                followUI.setVisible(true);
            }
        });

        lowBarHisBoard = new JButton("MY Home");
        lowBarHisBoard.setBounds(290, 620, 80, 40); // 우측 아래로 이동
        lowBarHisBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.out.println("Your Board!");
                MyBoardUI myBoardUI = new MyBoardUI(ID);
                myBoardUI.setVisible(true);
            }
        });

        contentPane.add(lowBarFollowingBoardButton);
        contentPane.add(lowBarFollowButton);
        contentPane.add(lowBarHisBoard);
        contentPane.add(searchButton);  // 추가된 부분

        ImageIcon mainLogoIcon = new ImageIcon(getClass().getResource("/img/mainLogo.png"));
        JLabel mainLogo = new JLabel(mainLogoIcon);
        mainLogo.setBounds(184, 25, mainLogoIcon.getIconWidth(), mainLogoIcon.getIconHeight());
        contentPane.add(mainLogo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainBoardUI mainBoardUI = new MainBoardUI("testUser");
                mainBoardUI.setVisible(true);
            }
        });
    }
}
