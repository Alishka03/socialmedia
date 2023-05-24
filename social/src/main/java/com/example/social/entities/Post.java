package com.example.social.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "content")
    private String content;
    @Column(name = "postphoto")
    private String postPhoto;
    @Column(name = "dateCreated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;
    //USER -> AUTHOR
    @ManyToOne( cascade = {CascadeType.ALL})
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @JsonManagedReference
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Comment> comments;
    @JsonIgnore
    @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "liker_id")
    )
    private List<User> likeList = new ArrayList<>();
    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "comment_count")
    private int commentCount;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", postPhoto='" + postPhoto + '\'' +
                ", dateCreated=" + dateCreated +
                ", author=" + author +
                ", comments=" + comments +
                ", likeList=" + likeList +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                '}';
    }
}
