package com.example.board_api.controller;

import com.example.board_api.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.board_api.entity.Post;
import com.example.board_api.repository.PostRepository;


@SpringBootTest
@AutoConfigureMockMvc //MockMvc를 스프링이 준비
@Transactional // 롤백 위해
class PostControllerTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc; // postman에서 했던 작업을 그대로 코드로 구현
    //Controller 메서드 하나만 호출하는 게 아니라 HTTP 요청부터 DB까지 전체 흐름을 상당 부분 확인하는 통합 테스트

    @Test
    void createPost() throws Exception {
    //MockMvc의 perform() 같은 테스트 API가 예외를 던질 수 있기 때문에 테스트에서는 이렇게 throws Exception으로
        // 간단하게 처리, MockMvc 테스트에서는
        //메서드 뒤에 throws Exception이 자주 붙는다


        String requestJson = """
                {
                  "title": "Test title",
                  "content": "Test content"
                }
                """;

        mockMvc.perform(
                        post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title")
                        .value("Test title"))
                .andExpect(jsonPath("$.content")
                        .value("Test content"));
    }

    @Test
    void failToCreatePostWhenTitleIsBlank() throws Exception {

        String requestJson = """
            {
              "title": "",
              "content": "Test content"
            }
            """;

        mockMvc.perform(
                        post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPostById() throws Exception {

        // given
        Post post = postRepository.save(
                new Post(
                        "Test title",
                        "Test content"
                )
        );

        // when & then
        mockMvc.perform(
                        get("/api/posts/{postId}", post.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(post.getId()))
                .andExpect(jsonPath("$.title")
                        .value("Test title"))
                .andExpect(jsonPath("$.content")
                        .value("Test content"));
    }

    @Test
    void getAllPosts() throws Exception {

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

        // when & then
        mockMvc.perform(
                        get("/api/posts")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()")
                        .value(2))
                .andExpect(jsonPath("$[0].id")
                        .value(secondPost.getId()))
                .andExpect(jsonPath("$[0].title")
                        .value("Second title"))
                .andExpect(jsonPath("$[1].id")
                        .value(firstPost.getId()));
    }
    @Test
    void updatePost() throws Exception {

        // given
        Post post = postRepository.save(
                new Post(
                        "Original title",
                        "Original content"
                )
        );

        String requestJson = """
            {
              "title": "Updated title",
              "content": "Updated content"
            }
            """;

        // when & then
        mockMvc.perform(
                        patch(
                                "/api/posts/{postId}",
                                post.getId()
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(post.getId()))
                .andExpect(jsonPath("$.title")
                        .value("Updated title"))
                .andExpect(jsonPath("$.content")
                        .value("Updated content"));
    }

    @Test
    void deletePost() throws Exception {

        // given
        Post post = postRepository.save(
                new Post(
                        "Delete title",
                        "Delete content"
                )
        );

        Long postId = post.getId();

        // when & then
        mockMvc.perform(
                        delete(
                                "/api/posts/{postId}",
                                postId
                        )
                )
                .andExpect(status().isNoContent());

        assertThat(
                postRepository.findById(postId)
        ).isEmpty();
    }
    @Test
    void failToGetPostWhenPostDoesNotExist() throws Exception {

        mockMvc.perform(
                        get("/api/posts/{postId}", 999999L)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("POST_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("게시글을 찾을 수 없습니다."));
    }


}