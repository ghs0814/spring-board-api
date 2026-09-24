package com.example.board_api.exception;

public record ErrorResponse(
        String code,
        String message
) {
}