/*
6.2 다대일 Many To One 연관관계 실습 - 게시물 목록과 Projection p541
 */
package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private Long replyCount;
}
