package com.example.board_api.service;

import com.example.board_api.dto.PostCreateRequest;
import com.example.board_api.dto.PostResponse;
import com.example.board_api.dto.PostUpdateRequest;
import com.example.board_api.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest //통합테스트 -> 실제로 스프링 빈 연결 -> autowired로 필드 주입.(final 안붙임)
@Transactional //테스트가 끝나면 자동으로 롤백되어 테스트 독립성을 지키는 역할도 한다.
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    void createPost() {

        // given
        PostCreateRequest request =
                new PostCreateRequest(
                        "Test title",
                        "Test content"
                );

        // when
        PostResponse response =
                postService.create(request);

        // then
        assertThat(response.getId())
                .isNotNull();

        assertThat(response.getTitle())
                .isEqualTo("Test title");

        assertThat(response.getContent())
                .isEqualTo("Test content");
    }

    @Test
    void findPostById() {

        // given
        PostCreateRequest request =
                new PostCreateRequest(
                        "Test title",
                        "Test content"
                );

        PostResponse createdPost =
                postService.create(request);

        // when
        PostResponse foundPost =
                postService.findById(
                        createdPost.getId()
                );

        // then
        assertThat(foundPost.getId())
                .isEqualTo(createdPost.getId());

        assertThat(foundPost.getTitle())
                .isEqualTo("Test title");

        assertThat(foundPost.getContent())
                .isEqualTo("Test content");
    }

    @Test
    void updatePost() {

        // given
        PostCreateRequest createRequest =
                new PostCreateRequest(
                        "Original title",
                        "Original content"
                );

        PostResponse createdPost =
                postService.create(createRequest);

        PostUpdateRequest updateRequest =
                new PostUpdateRequest(
                        "Updated title",
                        "Updated content"
                );

        // when
        PostResponse updatedPost =
                postService.update(
                        createdPost.getId(),
                        updateRequest
                );

        // then
        assertThat(updatedPost.getTitle())
                .isEqualTo("Updated title");

        assertThat(updatedPost.getContent())
                .isEqualTo("Updated content");
    }


    @Test
    void deletePost() {

        // given
        PostCreateRequest request =
                new PostCreateRequest(
                        "Delete title",
                        "Delete content"
                );

        PostResponse createdPost =
                postService.create(request);

        Long postId =
                createdPost.getId();

        // when
        postService.delete(postId);

        // then
        assertThat(
                postRepository.findById(postId) // 결과는 Optional<Post>
        ).isEmpty();
    }

    @Test
    void throwExceptionWhenPostNotFound() { // AssertJ

        // given
        Long postId = 999L;

        // when & then
        assertThatThrownBy( // 이 코드를 실행했을 때, 예외가 발생하는지 확인, assertJ에서 제공
                () -> postService.findById(postId)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글을 찾을 수 없습니다."); // 메시지까지 확인
    }

    @Test
    void throwExceptionWhenPostNotFound2() { // JUNIT

        // given
        Long postId = 999L;

        // when & then
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> postService.findById(postId)
                );

        assertEquals(
                "게시글을 찾을 수 없습니다.",
                exception.getMessage()
        );
    }

    @Test
    void throwExceptionWhenUpdatingNonExistingPost() {

        // given
        Long postId = 999L;

        PostUpdateRequest request =
                new PostUpdateRequest(
                        "Updated title",
                        "Updated content"
                );

        // when & then
        assertThatThrownBy(
                () -> postService.update(
                        postId,
                        request
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글을 찾을 수 없습니다.");
    }

    @Test
    void throwExceptionWhenDeletingNonExistingPost() {

        // given
        Long postId = 999L;

        // when & then
        assertThatThrownBy(
                () -> postService.delete(postId)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글을 찾을 수 없습니다.");
    }
}