package com.example.board_api.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt; //자동 스네이크 형식 변환

    public Post(String title, String content){
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    //update하면 jpa dirty checking에 의해 변경 감지에 의해 자동으로 save()될 예정임.
    public void update(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }


}
