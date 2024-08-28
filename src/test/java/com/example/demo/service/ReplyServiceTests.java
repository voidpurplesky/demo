/*
6.2 다대일 Many To One 연관관계 실습 - 댓글 서비스 계층의 구현 - 댓글 등록 처리 p551
 */
package com.example.demo.service;

import com.example.demo.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    // 댓글 등록 테스트 p551
    @Test
    public void register() {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .bno(407L)
                .replyText("ReplyDTO Text")
                .replyer("replyer")
                .build();
        log.info(replyService.register(replyDTO));
    }
}
