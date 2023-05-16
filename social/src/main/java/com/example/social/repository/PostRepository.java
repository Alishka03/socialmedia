package com.example.social.repository;

import com.example.social.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post , Integer> {
    List<Post> findAllByOrderByDateCreatedAsc();
    List<Post> findByAuthorIdOrderByDateCreatedDesc(Integer id);
    List<Post> findByAuthorId(Integer id);
}
