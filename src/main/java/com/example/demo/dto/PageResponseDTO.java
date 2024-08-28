/*
5.4 게시물 관리 완성하기 - 서비스 계층과 DTO의 구현 - 목록/검색 처리 - PageResponseDTO p470
화면에 DTO의 목록과 시작 페이지/끝 페이지 등에 대한 처리를 담당
 */
package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;
    private int size;
    private int total;
    private int start; // 시작 페이지 번호
    private int end; // 끝 페이지 번호
    private boolean prev; // 이전 페이지 존재 여부
    private boolean next; // 다음 페이지 존재 여부

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
        if (total <= 0) {
            return;
        }

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10; // 화면에서의 마지막 번호
        this.start = this.end - 9; // 화면에서의 시작 번호

        int last = (int)(Math.ceil((total/(double)size))); // 데이터의 개수를 계산한 마지막 페이지 번호
        this.end = Math.min(end, last);
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

    }
}
