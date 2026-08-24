package com.example.board_api.repository;

import com.example.board_api.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // 기본적으로 테스트마다 트랜잭션을 사용하고, 테스트가 끝나면 롤백되는 방식으로 동작
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void savePost() {

        // given
        Post post = new Post(
                "Test title",
                "Test content"
        );

        // when
        Post savedPost =
                postRepository.save(post);

        // then
        assertThat(savedPost.getId())
                .isNotNull();

        assertThat(savedPost.getTitle())
                .isEqualTo("Test title");

        assertThat(savedPost.getContent())
                .isEqualTo("Test content");
    }

    @Test
    void findPostById() {

        // given
        Post post = new Post(
                "Test title",
                "Test content"
        );

        Post savedPost =
                postRepository.save(post);

        // when
        Optional<Post> foundPost =
                postRepository.findById(
                        savedPost.getId()
                );

        // then
        assertThat(foundPost)
                .isPresent();

        assertThat(foundPost.get().getTitle())
                .isEqualTo("Test title");
    }

    @Test
    void findAllPostsInDescendingOrder() {

        // given
        Post firstPost = postRepository.save(
                new Post(
                        "First title",
                        "First content"
                )
        );

        Post secondPost = postRepository.save(
                new Post(
                        "Second title",
                        "Second content"
                )
        );

        // when
        List<Post> posts =
                postRepository
                        .findAllByOrderByIdDesc();

        // then
        assertThat(posts)
                .hasSize(2);

        assertThat(posts.get(0).getId())
                .isEqualTo(secondPost.getId());

        assertThat(posts.get(1).getId())
                .isEqualTo(firstPost.getId());
    }
}