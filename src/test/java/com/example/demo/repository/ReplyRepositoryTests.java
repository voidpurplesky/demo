/*
6.2 다대일 Many To One 연관관계 실습 - ReplyRepository 생성과 테스트 - 테스트를 통한 insert 확인 p535
 */
package com.example.demo.repository;

import com.example.demo.domain.Board;
import com.example.demo.domain.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    // 1. insert p535
    @Test
    public void insert() {
        Long bno = 406L; // 실제 DB에 있는 bno
        Board board = Board.builder().bno(bno).build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("replyText")
                .replyer("replyer1")
                .build();

        replyRepository.save(reply);
    }
    /*
Hibernate:
    insert
    into
        reply
        (board_bno, moddate, regdate, reply_text, replyer)
    values
        (?, ?, ?, ?, ?)
    returning rno
     */

    // 테스트 코드와 fetch 속성 p538
    @Test
    public void listOfBoard() {
        Long bno = 407L;
        Pageable pageable = PageRequest.of(0,10, Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        result.getContent().forEach(reply -> {
            log.info(reply);
        });
    }
    /*
org.springframework.dao.InvalidDataAccessApiUsageException: For queries with named parameters you need to provide names for method parameters;
Use @Param for query method parameters, or when on Java 8+ use the javac flag -parameters

Hibernate:
    select
        r1_0.rno,
        r1_0.board_bno,
        r1_0.moddate,
        r1_0.regdate,
        r1_0.reply_text,
        r1_0.replyer
    from
        reply r1_0
    where
        r1_0.board_bno=?
    order by
        r1_0.rno desc
    limit
        ?

2024-08-14T15:37:34.150+09:00  INFO 12140 --- [demo] [           main] c.e.d.repository.ReplyRepositoryTests    : Reply(rno=2, replyText=replyText, replyer=replyer1)
2024-08-14T15:37:34.150+09:00  INFO 12140 --- [demo] [           main] c.e.d.repository.ReplyRepositoryTests    : Reply(rno=1, replyText=replyText, replyer=replyer1)
FetchType.LAZY - 지연 로딩. 필요한 순간까지 디비와 연결하지 않는 방식
     */
}
