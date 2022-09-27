package com.four.withtopia.dto.response;

import com.four.withtopia.db.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostListResponseDto {
    private Long postId;
    private String postTitle;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostListResponseDto createPostListResponseDto(Post post){
        return PostListResponseDto.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .nickname(post.getNickname())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }
}
