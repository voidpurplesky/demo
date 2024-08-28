/*
6.2 다대일 Many To One 연관관계 실습 - ReplyRepository 생성과 테스트 p534
 */
package com.example.demo.repository;

import com.example.demo.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // 특정 게시물의 댓글 조회와 인덱스 p537
    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(@Param("bno") Long bno, Pageable pageable);

    // 게시물과 첨부파일 삭제 p625
    void deleteByBoard_Bno(Long bno);
}
