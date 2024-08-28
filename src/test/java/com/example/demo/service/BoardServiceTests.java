/*
5.4 게시물 관리 완성하기 - 서비스 계층과 DTO의 구현 - CRUD 작업처리 - 등록작업 처리 p463
 */
package com.example.demo.service;

import com.example.demo.dto.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void register() {
        log.info(boardService.getClass().getName());
        //com.example.demo.service.BoardServiceImpl$$SpringCGLIB$$0
        // BoardServiceImpl을 감싸서 만든 클래스 정보가 출력됨
        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);
        log.info("bno: {}", bno);

    }
    /*
    insert
    into
        board
        (content, moddate, regdate, title, writer)
    values
        (?, ?, ?, ?, ?)
    returning bno

    c.e.demo.service.BoardServiceTests       : bno: 401
     */

    @Test
    public void modify() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(401L)
                .title("Updated...401")
                .content("Updated content 401...")
                .build();

        boardService.modify(boardDTO);
    }
    /*
Hibernate:
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
        b1_0.bno=?
Hibernate:
    update
        board
    set
        content=?,
        moddate=?,
        title=?,
        writer=?
    where
        bno=?
     */
    @Test
    public void list() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
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

PageResponseDTO(page=1, size=10, total=40, start=1, end=4, prev=false, next=false, dtoList=[BoardDTO(bno=401, title=Updated...401, content=Updated content 401
     */

    // p642
    @Test
    public void registerFile() {
        log.info(boardService.getClass().getName());
        //com.example.demo.service.BoardServiceImpl$$SpringCGLIB$$0
        // BoardServiceImpl을 감싸서 만든 클래스 정보가 출력됨
        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();
        // 파일 업로드 p642
        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_ccc.jpg"
                )
        );
        Long bno = boardService.register(boardDTO);
        log.info("bno: {}", bno);

    }

    /*
Hibernate:
    insert
    into
        board
        (content, moddate, regdate, title, writer)
    values
        (?, ?, ?, ?, ?)
    returning bno
Hibernate:
    insert
    into
        board_image
        (board_bno, file_name, ord, uuid)
    values
        (?, ?, ?, ?)
Hibernate:
    insert
    into
        board_image
        (board_bno, file_name, ord, uuid)
    values
        (?, ?, ?, ?)
Hibernate:
    insert
    into
        board_image
        (board_bno, file_name, ord, uuid)
    values
        (?, ?, ?, ?)
     */

    @Test
    public void readAll() {
        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        }
    }

    /*
Hibernate:
    select
        b1_0.bno,
        b1_0.content,
        is1_0.board_bno,
        is1_0.uuid,
        is1_0.file_name,
        is1_0.ord,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer
    from
        board b1_0
    left join
        board_image is1_0
            on b1_0.bno=is1_0.board_bno
    where
        b1_0.bno=?
2024-08-27T17:24:03.177+09:00  INFO 2748 --- [demo] [           main] c.e.demo.service.BoardServiceTests       : BoardDTO(bno=101, title=Image Test, content=file test, writer=tester, regDate=2024-08-27T15:16:52.676322, modDate=2024-08-27T15:16:52.676322, fileNames=[faa10ccb-5445-4a12-b993-359eb480979c_updatefile0.jpg, eaf2bd56-4619-4f5b-8461-37dc245eea6f_updatefile1.jpg])
2024-08-27T17:24:03.177+09:00  INFO 2748 --- [demo] [           main] c.e.demo.service.BoardServiceTests       : faa10ccb-5445-4a12-b993-359eb480979c_updatefile0.jpg
2024-08-27T17:24:03.177+09:00  INFO 2748 --- [demo] [           main] c.e.demo.service.BoardServiceTests       : eaf2bd56-4619-4f5b-8461-37dc245eea6f_updatefile1.jpg

     */

    @Test
    public void modifyFile() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated...101")
                .content("Updated content 101...")
                .build();

        // 첨부파일을 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));

        boardService.modify(boardDTO);
    }

    @Test
    public void removeAll() {
        Long bno = 34L;
        boardService.remove(bno);
    }
    /*
Hibernate:
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
        b1_0.bno=?
Hibernate:
    select
        is1_0.board_bno,
        is1_0.uuid,
        is1_0.file_name,
        is1_0.ord
    from
        board_image is1_0
    where
        is1_0.board_bno=?
Hibernate:
    delete
    from
        board_image
    where
        uuid=?
Hibernate:
    delete
    from
        board_image
    where
        uuid=?
Hibernate:
    delete
    from
        board_image
    where
        uuid=?
Hibernate:
    delete
    from
        board
    where
        bno=?
     */

//p648
    @Test
    public void listWithAll() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());

            if (boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
                    log.info(boardImage);
                }
            }
            log.info("----------------------------------");
        });
    }

}
