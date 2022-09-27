package com.four.withtopia.db.repository;

import com.four.withtopia.db.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    Post findByPostId(Long postId);
}
