/*
5.4 게시물 관리 완성하기 - 서비스 계층과 DTO의 구현 - CRUD 작업처리 - 등록작업 처리 p462
 */
package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.dto.*;

import java.util.List;

public interface BoardService {
    Long register(BoardDTO boardDTO); // 등록
    BoardDTO readOne(Long bno); // 조회 p465
    void modify(BoardDTO boardDTO); // 수정 p466
    void remove(Long bno); // 삭제 p467

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO); // 검색 p471
    // 6.2 다대일 Many To One 연관관계 실습 - 다대일 연관관계의 구현 - 게시물 목록 화면 처리 p547
    // 댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
    //게시글의 이미지와 댓글의 숫자까지 처리 p634
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    // DTO를 엔티티로 변환하기 p641
    default Board dtoToEntity(BoardDTO boardDTO) {
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if (boardDTO.getFileNames() != null) {
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }

        return board;
    }

    // 게시물 조회 처리 p643
    default BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid()+"_"+boardImage.getFileName()).toList();

        boardDTO.setFileNames(fileNames);
        return boardDTO;
    }
}
