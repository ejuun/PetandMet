package com.ssafy.petandmet.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "center")
@Builder
public class User {

    @Id
    @Column(name = "user_uuid")
    private String uuid;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Center center;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "point_id")
    private Point point;

    @Column(name = "user_id")
    private String id;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_phone")
    private String phone;

    @Column(name = "user_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private int attendance;

    @Enumerated(EnumType.STRING)
    private DonateGradeType donateGrade;

    @Enumerated(EnumType.STRING)
    private WalkGradeType walkGrade;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Donate> donates = new ArrayList<>();

    @Column(name = "user_photo_url")
    private String photoUrl;

    //==연관관계 메서드==//
    public void addCenter(Center center) {
        this.center = center;
        center.setUser(this);
    }
}
