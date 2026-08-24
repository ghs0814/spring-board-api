package com.example.board_api.dto;

import com.example.board_api.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter //jackson이 사용하기 위해
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    //entity 받아서 dto로 바꿈
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
    }
}

// 객체 <-> json 사이 변환은 jackson이라는 spring web에 포함된 기능이 담당.
// 엔티티  = db와 1대1대응, 완전한 구조, 내부 구조
// dto = 원하는 기능, 역할별로 존재, 일부 구조, 외부에 공개할 api 구조