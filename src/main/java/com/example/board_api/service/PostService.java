package com.example.board_api.service;

import com.example.board_api.dto.PostCreateRequest;
import com.example.board_api.dto.PostResponse;
import com.example.board_api.dto.PostUpdateRequest;
import com.example.board_api.entity.Post;
import com.example.board_api.exception.PostNotFoundException;
import com.example.board_api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // di를 위해, 생성자 자동 생성
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request) {

        Post post = new Post(
                request.getTitle(),
                request.getContent()
        );

        Post savedPost = postRepository.save(post);

        return new PostResponse(savedPost);
    }

    public List<PostResponse> findAll() {

        return postRepository
                .findAllByOrderByIdDesc()
                .stream()
                .map(PostResponse::new)
                .toList();
    }

    public PostResponse findById(Long postId) {

        Post post = postRepository
                .findById(postId)
                .orElseThrow(
                        PostNotFoundException::new // 커스텀 예외 생성
                );

        return new PostResponse(post); //돌려주는건 전부 dto로
    }

    @Transactional // 따로 붙여서, readonly = false로
    public PostResponse update(
            Long postId,
            PostUpdateRequest request
    ) {

        Post post = postRepository
                .findById(postId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "게시글을 찾을 수 없습니다."
                        )
                );

        post.update(
                request.getTitle(),
                request.getContent()
        );
    // hibernate가 dirty check
        return new PostResponse(post);
    }

    @Transactional
    public void delete(Long postId) {

        Post post = postRepository
                .findById(postId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "게시글을 찾을 수 없습니다."
                        )
                );

        postRepository.delete(post);
    }
}