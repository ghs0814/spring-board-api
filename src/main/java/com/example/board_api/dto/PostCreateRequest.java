package com.example.board_api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.") // 엔티티, dto 양쪽에서 검증
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    public PostCreateRequest(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }

}
