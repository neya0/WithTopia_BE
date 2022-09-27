package com.four.withtopia.api.controller;

import com.four.withtopia.api.service.PostService;
import com.four.withtopia.dto.request.PostRequestDto;
import com.four.withtopia.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity<?> getPostList (){
        return new ResponseUtil<>().forSuccess(postService.getPostList());
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<?> createPost (HttpServletRequest request, @ModelAttribute PostRequestDto requestDto) throws IOException {
        System.out.println("requestDto = " + requestDto.getImageList());
        System.out.println("requestDto = " + requestDto.getTitle());
        return new ResponseUtil<>().forSuccess(postService.createPost(request, requestDto));
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.GET)
    public ResponseEntity<?> getPost (@PathVariable Long postId){
        return new ResponseUtil<>().forSuccess(postService.getPost(postId));
    }
}
