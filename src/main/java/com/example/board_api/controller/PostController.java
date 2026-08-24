package com.example.board_api.controller;

import com.example.board_api.dto.PostCreateRequest;
import com.example.board_api.dto.PostResponse;
import com.example.board_api.dto.PostUpdateRequest;
import com.example.board_api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //메서드 반환 객체가 dto -> json(jackson에 의해)
@RequiredArgsConstructor
@RequestMapping("/api/posts") //모든 api앞에 /api/posts가 붙는다. ex) POST /api/posts, GET /api/posts/{postId}
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostCreateRequest request
    ) {

        PostResponse response =
                postService.create(request);

        return ResponseEntity //status, header, body
                .status(HttpStatus.CREATED)
                .body(response); // json
    }


    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {

        List<PostResponse> responses =
                postService.findAll();

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findById(
            @PathVariable Long postId
    ) {

        PostResponse response =
                postService.findById(postId);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{postId}") // put : 전체 수정/교체, patch : 일부 필드 수정
    public ResponseEntity<PostResponse> update(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {

        PostResponse response =
                postService.update(postId, request);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long postId
    ) {

        postService.delete(postId);

        return ResponseEntity
                .noContent()
                .build();
    }

}
