package com.four.withtopia.dto.response;

import com.four.withtopia.db.domain.Post;
import com.four.withtopia.db.domain.PostImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String nickname;
    private String postTitle;
    private String content;
    private List<PostImage> postImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostResponseDto createPostResponseDto(Post post){
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .nickname(post.getNickname())
                .postTitle(post.getPostTitle())
                .postImage(post.getPostImageList())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();

    }
}
