package org.koreait.yumyum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userPhone;

    @Column(nullable = false, unique = true)
    private String userBusinessNumber;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean privacyPolicyAgreed;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean marketingAgreed;

    @Column(nullable = false, length = 5, columnDefinition = "VARCHAR(5) COMMENT '가입 경로 (HOME, KAKAO, NAVER)'")
    private String joinPath;

    @Column(nullable = true, columnDefinition = "VARCHAR(255) COMMENT 'OAuth2 사용자 아이디'")
    private String snsId;

    @PrePersist
    private void setDefaultValues() {
        if (this.joinPath == null) {
            this.joinPath = "HOME"; // 기본값 설정
        }
    }

    public void setEncodedPassword(String encodedPassword) {
        this.userPw = encodedPassword;
    }

}
