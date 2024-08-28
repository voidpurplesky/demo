package com.example.demo.repository.search;

import com.example.demo.domain.Board;
import com.example.demo.domain.QBoard;
import com.example.demo.domain.QReply;
import com.example.demo.dto.BoardImageDTO;
import com.example.demo.dto.BoardListAllDTO;
import com.example.demo.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board; //Q도메인 객체
        JPQLQuery<Board> query = from(board); // select .. from board
        query.where(board.title.contains("1")); // where title like ..
        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch(); // 쿼리실행

        list.forEach(b -> log.info(b));

        long count = query.fetchCount();
        return null;
    }
    @Override
    public Long search11(Pageable pageable) {
        QBoard board = QBoard.board; //Q도메인 객체
        JPQLQuery<Board> query = from(board); // select .. from board
        query.where(board.title.contains("%")); // where title like ..
        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch(); // 쿼리실행

        list.forEach(b -> log.info(b));

        long count = query.fetchCount();
        return count;
    }

    @Override
    public Page<Board> searchTitle(Pageable pageable) {
        QBoard board = QBoard.board; //Q도메인 객체
        JPQLQuery<Board> query = from(board); // select .. from board
        query.where(board.content.contains("%")); // where title like ..
        //paging
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);
    }

    /*
     select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer
    from
        board b1_0
    where
        b1_0.title like ? escape '!'

        p453 Pageable 처리하기 + limit
            select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer
    from
        board b1_0
    where
        b1_0.title like ? escape '!'
    order by
        b1_0.bno desc
    limit
        ?, ?
     */
    @Override
    public Page<Board> search2(Pageable pageable) {
        QBoard board = QBoard.board; //Q도메인 객체
        JPQLQuery<Board> query = from(board); // select .. from board
        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
        booleanBuilder.or(board.title.contains("11")); // title like ..
        booleanBuilder.or(board.content.contains("11")); // content like ..

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);

    }
    /*
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer
    from
        board b1_0
    where
        (
            b1_0.title like ? escape '!'
            or b1_0.content like ? escape '!'
        )
        and b1_0.bno>?
     */

    // 검색을 위한 메소드 선언과 테스트 p456
    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
        // 검색조건과 키워드가 있다면
        if ( (types != null && types.length > 0) && keyword != null) {
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } //end for
            query.where(booleanBuilder);
        }//end if

        // bno > 0
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);
    }


    /*
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer
    from
        board b1_0
    where
        (
            b1_0.title like ? escape '!'
            or b1_0.content like ? escape '!'
            or b1_0.writer like ? escape '!'
        )
        and b1_0.bno>?
    order by
        b1_0.bno desc
    limit
        ?, ?
     */

    // 6.2 다대일 Many To One 연관관계 실습 - 게시물 목록과 Projection - LEFT(OUTER) JOIN 처리 p542
    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);
        // applyPagigination() p544
        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } // end for
            query.where(booleanBuilder);
        }

        query.where(board.bno.gt(0L)); // bno > 0

        // Projections.bean() p543
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")
        ));

        // applyPagigination()-2 p545
        this.getQuerydsl().applyPagination(pageable, dtoQuery);
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();
        return new PageImpl<>(dtoList, pageable, count);
    }

    // N+1 문제 p629
    /*
    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); // left join

        getQuerydsl().applyPagination(pageable, boardJPQLQuery); // paging

        List<Board> boardList = boardJPQLQuery.fetch();

        boardList.forEach(board1 -> {
            log.info(board1.getBno());
            log.info(board1.getImageSet());
            log.info("----------------------------");
        });

        return null;
    }
*/
    // Querydsl의 튜플 처리 p635
    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); // left join

        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } // end for
            boardJPQLQuery.where(booleanBuilder);
        }
        boardJPQLQuery.groupBy(board);

        getQuerydsl().applyPagination(pageable, boardJPQLQuery); // paging

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();
        
        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            
           Board board1 = (Board) tuple.get(board);
           long replyCount = tuple.get(1, Long.class);

           BoardListAllDTO dto = BoardListAllDTO.builder()
                   .bno(board1.getBno())
                   .title(board1.getTitle())
                   .writer(board1.getWriter())
                   .regDate(board1.getRegDate())
                   .replyCount(replyCount)
                   .build();
           // BoardImage를 BoardImageDTO 처리할 부분 p637
           List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                   .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()
                   ).toList();
           
           dto.setBoardImages(imageDTOS); // 처리된 BoardImageDTO들을 추가
           
           return dto;
        }).toList();

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}
