package com.example.social.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
@Getter
@Setter
@Builder
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    @Column(name = "name")
    private String name;
    @NotEmpty
    @Column(name = "surname")
    private String surname;
    @Column(name = "username")
    @NotEmpty
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "profile_photo")
    private String profilePhoto;
    @Column(name = "role")
    private String role;
    @Column(name = "joindate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;
    @Column(name = "intro")
    private String intro;
    @Column(name = "hometown")
    private String hometown;
    @Column(name = "workplace")
    private String workplace;

    //POSTS
    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> postsList;
    //---------------

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    @JsonIgnore
    @ManyToMany(mappedBy = "followerUsers", fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> followingUsers = new ArrayList<>();


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "follow_users",
            joinColumns = @JoinColumn(name = "followed_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followerUsers = new ArrayList<>();


    @JsonIgnore
    @ManyToMany(mappedBy = "likeList",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Post> likedPosts = new ArrayList<>();

    @Transient
    public String token = null;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int getFollowerCount() {

        return followerUsers.size();
    }

    public int getFollowingCount() {
        return followingUsers.size();
    }
}
