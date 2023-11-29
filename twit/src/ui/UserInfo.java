package ui;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfo {
    private String firstname;
    private String lastname;
    private String email;
    private String gender;

    // 생성자
    public UserInfo(ResultSet rs) throws SQLException {
        // 예시: 만약 SQL 결과에서 firstname, lastname, email, gender 순서로 값을 가져온다면
        this.firstname = rs.getString("firstname");
        this.lastname = rs.getString("lastname");
        this.email = rs.getString("email");
        this.gender = rs.getString("gender");
    }

    // 각 필드에 대한 getter 메서드
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }
}

