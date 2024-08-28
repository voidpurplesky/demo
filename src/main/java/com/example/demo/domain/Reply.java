/*
6.2 다대일 Many To One 연관관계 실습 - 다대일 연관관계의 구현 p533
 - @ToString을 할때 참조하는 객체를 사용하지 않도록 반드시 exclude 속성값을 지정한다.
 - @ManyToOne과 같이 연관관계를 나타낼 때에는 반드시 fetch 속성은 LAZY로 지정한다.
 */
package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Reply", indexes = {@Index(name = "idx_reply_board_bno", columnList = "board_bno")})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String replyText;

    private String replyer;

    // 댓글 수정
    public void changeText(String text) {
        replyText = text;
    }

}
/*
Hibernate:
    create table reply (
        rno bigint not null auto_increment,
        moddate datetime(6),
        regdate datetime(6),
        reply_text varchar(255),
        replyer varchar(255),
        board_bno bigint,
        primary key (rno)
    ) engine=InnoDB
Hibernate:
    alter table if exists reply
       add constraint FKr1bmblqir7dalmh47ngwo7mcs
       foreign key (board_bno)
       references board (bno)
 */