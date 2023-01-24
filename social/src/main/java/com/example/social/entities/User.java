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
    @Column(name = "role")
    private String role;
    @NotEmpty
    @Column(name = "email")
    private String email;
    @Column(name = "joindate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;
    //POSTS
    @JsonManagedReference
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> postsList;
    //---------------

    @JsonManagedReference
    @OneToMany(mappedBy = "author"  ,fetch = FetchType.LAZY)
    private List<Comment> comments;

    @JsonIgnore
    @ManyToMany(mappedBy = "followerUsers", fetch = FetchType.LAZY)
    private List<User> followingUsers;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "follow_users",
            joinColumns = @JoinColumn(name = "followed_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followerUsers = new ArrayList<>();


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", joinDate=" + joinDate +
                ", postsList=" + postsList+
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
