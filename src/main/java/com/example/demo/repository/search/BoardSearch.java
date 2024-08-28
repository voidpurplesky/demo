/*
5.3 Spring Data JPA - 기존의 Repository와 Querydsl 연동하기 p449
 */
package com.example.demo.repository.search;

import com.example.demo.domain.Board;
import com.example.demo.dto.BoardListAllDTO;
import com.example.demo.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);
    Long search11(Pageable pageable);
    Page<Board> searchTitle(Pageable pageable);
    Page<Board> search2(Pageable pageable);
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);
    // N+1문제 p628
    //Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable);
    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);
}
