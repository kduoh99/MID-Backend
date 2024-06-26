package com.skhu.mid_skhu.app.entity.student;

import com.skhu.mid_skhu.app.entity.interest.InterestCategory;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Student {

    @JsonIgnore
    @Id
    @Column(name = "STUDENT_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "STUDENT_NO", nullable = false, unique = true)
    @Size(min = 9, max = 9)
    private String studentNo;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "STUDENT_NAME", length = 10, nullable = false)
    private String name;

    @Column(name = "STUDENT_DEPARTMENT", length = 20, nullable = false)
    private String department;

    @JsonIgnore
    @Column(name = "PHONE_NUMBER", length = 14, nullable = false)
    private String phoneNumber;

    @Column(name = "FCM_TOKEN")
    private String fcmToken;

    @Column(name = "ROLE_TYPE", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "INTEREST_CATEGORY", nullable = false)
    private List<InterestCategory> category;

    public void updateDetails(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}