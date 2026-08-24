package com.example.board_api.repository;

import com.example.board_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //최신 게시물부터 조회(id내림차순)
    List<Post> findAllByOrderByIdDesc();
}
