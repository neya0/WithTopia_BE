package com.four.withtopia.api.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.four.withtopia.config.error.ErrorCode;
import com.four.withtopia.config.expection.PrivateException;
import com.four.withtopia.db.domain.Member;
import com.four.withtopia.db.domain.Post;
import com.four.withtopia.db.domain.PostImage;
import com.four.withtopia.db.repository.PostImageRepository;
import com.four.withtopia.db.repository.PostRepository;
import com.four.withtopia.dto.request.PostRequestDto;
import com.four.withtopia.dto.response.PostListResponseDto;
import com.four.withtopia.dto.response.PostResponseDto;
import com.four.withtopia.util.MemberCheckUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final MemberCheckUtils memberCheckUtils;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponseDto> getPostList(){
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostListResponseDto> responseDtoList = new ArrayList<>();

        for (Post post : postList) {
            PostListResponseDto responseDto = PostListResponseDto.createPostListResponseDto(post);
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    //게시글 상세 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId){
        Post post = postRepository.findByPostId(postId);
        return PostResponseDto.createPostResponseDto(post);
    }

    // 게시글 작성
    @Transactional
    public String createPost(HttpServletRequest request, PostRequestDto requestDto) throws IOException {
        // 토큰 검증
        Member member = memberCheckUtils.checkMember(request);

        // post 저장
        Post post = Post.builder()
                .postTitle(requestDto.getTitle())
                .nickname(member.getNickName())
                .content(requestDto.getContent())
                .build();
        postRepository.save(post);

        // 받은 이미지 저장
        List<MultipartFile> fileList = requestDto.getImageList();
        if(fileList != null){
            for(MultipartFile file : fileList){
                String url = postImage(file);
                PostImage postImage = PostImage.builder()
                        .postImage(url)
                        .postId(post.getPostId())
                        .build();
                postImageRepository.save(postImage);
            }
        }

        return "게시글 작성을 완료했습니다.";
    }

    //게시글 수정

    //게시글 삭제

    // 이미지 넣기
    public String postImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null | multipartFile.isEmpty()){
            throw new PrivateException(new ErrorCode(HttpStatus.NOT_FOUND,"400","이미지 파일이 없습니다"));
        }
        String fileName = multipartFile.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();

        return imgurl;
    }
}
