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

public class MainBoardUI extends JFrame {

    String ID;
    private JPanel contentPane;
    private DefaultListModel listModel;
    private JList list;
    private JButton articleReadButton;
    private final JButton lowBarFollowingBoardButton;
    private final JButton lowBarAllUserButton;
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

        articleReadButton = new JButton(new ImageIcon(getClass().getResource("/img/refreshPage.png")));
        articleReadButton.setBounds(310, 120, 30, 30);
        contentPane.add(articleReadButton);

        searchButton = new JButton("Search");  // 추가된 부분
        searchButton.setBounds(200, 620, 80, 40);  // 추가된 부분
        searchButton.setForeground(new Color(254, 255, 255));
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

        createListModel(arr);

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

                createListModel(arr);
            }
        });

        lowBarFollowingBoardButton = new JButton("MainBoard");
        lowBarFollowingBoardButton.setBounds(30, 620, 80, 40);
        lowBarFollowingBoardButton.setForeground(new Color(254, 255, 255));
        lowBarFollowingBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("MainBoard!");
            }
        });

        lowBarAllUserButton = new JButton("All User");
        lowBarAllUserButton.setBounds(115, 620, 80, 40);
        lowBarAllUserButton.setForeground(new Color(254, 255, 255));
        lowBarAllUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("AllUser!");
                AllUserUI followUI = new AllUserUI(ID);
                followUI.setVisible(true);
            }
        });

        lowBarHisBoard = new JButton("MY Home");
        lowBarHisBoard.setBounds(290, 620, 80, 40); // 우측 아래로 이동
        lowBarHisBoard.setForeground(new Color(254, 255, 255));
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
        contentPane.add(lowBarAllUserButton);
        contentPane.add(lowBarHisBoard);
        contentPane.add(searchButton);  // 추가된 부분

        ImageIcon mainLogoIcon = new ImageIcon(getClass().getResource("/img/mainLogo.png"));
        JLabel mainLogo = new JLabel(mainLogoIcon);
        mainLogo.setBounds(184, 25, mainLogoIcon.getIconWidth(), mainLogoIcon.getIconHeight());
        contentPane.add(mainLogo);

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }

    private DefaultListModel createListModel(ArrayList<Post> posts) {
        DefaultListModel model = new DefaultListModel();

        if (posts.size() == 0) {
            String info = "불러올 글이 없습니다";
            model.addElement(info);
        } else {
            for (Post res : posts) {
                // 게시물의 num을 이용하여 데이터베이스에서 시간 정보를 가져오기
                LocalDateTime createdAt = getCreatedAtFromDatabase(res.getNum());
                String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));

                String post = "[ (" + res.getId() + ") " + formattedTime + " ] " + res.getArticle() + "\n";
                model.addElement(post);
            }
        }

        list.setModel(model); // JList에 모델을 설정
        return model;
    }

    // num을 이용하여 데이터베이스에서 시간 정보를 가져오는 메서드
    private LocalDateTime getCreatedAtFromDatabase(int num) {
        LocalDateTime createdAt = null;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/twit", "root", "David100894@");
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

}
