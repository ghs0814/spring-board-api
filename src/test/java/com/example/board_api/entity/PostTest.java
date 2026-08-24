package com.example.board_api.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void 게시글을_수정할_수_있다() {

        // given
        Post post = new Post(
                "기존 제목",
                "기존 내용"
        );

        // when
        post.update(
                "수정된 제목",
                "수정된 내용"
        );

        // then
        assertThat(post.getTitle())
                .isEqualTo("수정된 제목");

        assertThat(post.getContent())
                .isEqualTo("수정된 내용");
    }
}