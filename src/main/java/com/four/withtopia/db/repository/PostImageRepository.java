package com.four.withtopia.db.repository;

import com.four.withtopia.db.domain.Post;
import com.four.withtopia.db.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostId(Long postId);
}
