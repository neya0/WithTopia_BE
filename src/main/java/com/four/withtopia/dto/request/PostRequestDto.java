package com.four.withtopia.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private List<MultipartFile> imageList;
}
