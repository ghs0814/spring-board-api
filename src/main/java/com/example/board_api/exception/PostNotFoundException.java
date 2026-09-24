package com.example.board_api.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super("게시글을 찾을 수 없습니다."); //부모의 생성자 호출
    }
}