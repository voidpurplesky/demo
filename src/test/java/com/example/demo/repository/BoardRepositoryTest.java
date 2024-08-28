package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import com.example.demo.domain.BoardImage;
import com.example.demo.dto.BoardListAllDTO;
import com.example.demo.dto.BoardListReplyCountDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.Board;

import lombok.extern.log4j.Log4j2;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ReplyRepository replyRepository; // 게시물과 첨부파일 삭제 p626
	
	@Test
	public void testInsert() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Board board = Board.builder().title("title").content("content").writer("user" + (i % 10)).build();
			Board result = boardRepository.save(board);
			
			log.info("BNO: " + result.getBno());
		});
	}
	
	/*
    insert 
    into
        board
        (content, moddate, regdate, title, writer) 
    values
        (?, ?, ?, ?, ?) 
    returning bno
	 */
	
	@Test
	public void select() {
		Long bno = 100L;
		Optional<Board> result = boardRepository.findById(bno);
		Board board = result.orElseThrow();
		log.info(board);
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
        b1_0.bno=?
	 */
	
	@Test
	public void update() {
		Long bno = 100L;
		Optional<Board> result = boardRepository.findById(bno);
		Board board = result.orElseThrow();
		board.change("update title", "update content");
		boardRepository.save(board);
		
	}
	
	
	@Test
	public void delete() {
		Long bno = 1L;
		boardRepository.deleteById(bno);
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
    delete 
    from
        board 
    where
        bno=?
	 */
	
	@Test
	public void paging() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		Page<Board> result = boardRepository.findAll(pageable);
		
		log.info("total count: " + result.getTotalElements());
		log.info("totla pages: " + result.getTotalPages());
		log.info("page number: " + result.getNumber());
		log.info("page size: " + result.getSize());
		List<Board> todoList = result.getContent();
		todoList.forEach(board -> log.info(board));
		//Board(bno=300, title=title, content=content, writer=user0)
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
    order by
        b1_0.bno desc 
    limit
        ?, ?
Hibernate: 
    select
        count(b1_0.bno) 
    from
        board b1_0
	 */
	
	@Test
	public void findKeyword() {
		//Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);
		//boardRepository.findByTitleContainingOrderByBnoDesc("up", );
		//@Query("select b from Board b where b.title like concat('%',:keyword,'%')")
		//Page<Board> findKeyword(String keyword, Pageable pageable);
		Pageable pageable = PageRequest.of(200, 200, Sort.by("bno").descending());
		Page<Board> result = boardRepository.findKeyword("up", pageable);
		List<Board> todoList = result.getContent();
		todoList.forEach(board -> log.info(board));
	}

	@Test
	public void search1() {
		Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
		boardRepository.search1(pageable);
	}

	@Test
	public void search11() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		//Page<Board> result = boardRepository.findAll(pageable);
		//Page<Board> result = boardRepository.search1(pageable);
		log.info(boardRepository.search11(pageable));
		//List<Board> todoList = result.getContent();
		//todoList.forEach(board -> log.info(board));
	}

	// Querydsl paging
	@Test
	public void searchTitle() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		Page<Board> result = boardRepository.searchTitle(pageable);

		log.info("total count: " + result.getTotalElements()); // 1
		log.info("result.getContent().size : " + result.getContent().size()); // 1
		List<Board> todoList = result.getContent();
		log.info("todoList.size="+todoList.size()); // 0
		todoList.forEach(board -> log.info(board));
	}


	@Test
	public void search2() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		Page<Board> result = boardRepository.search2(pageable);
		log.info("totalElements = " + result.getTotalElements()); // 0
		result.getContent().forEach(board -> log.info(board));
	}

	@Test
	public void searchAll() {
		String[] types = {"t","c","w"};
		String keyword = "1";
		Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
		Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
	}

	@Test
	public void searchAll2() {
		String[] types = {"t","c","w"};
		String keyword = "1";
		Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
		Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
		//total pages
		log.info(result.getTotalPages());
		//page size
		log.info(result.getSize());
		//pageNumber
		log.info(result.getNumber());
		//prev next
		log.info(result.hasPrevious()+" : "+ result.hasNext());
		result.getContent().forEach(board -> log.info(board));
	}

	// 6.2 다대일 Many To One 연관관계 실습 - 게시물 목록과 Projection - Projections.bean() p545
	@Test
	public void searchReplyCount() {
		String[] types = {"t","c","w"};
		String keyword = "1";
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
		log.info("total elements = " + result.getTotalElements());
		log.info("total pages = " + result.getTotalPages());
		log.info("has next = " + result.hasNext());
		result.getContent().forEach(board -> log.info(board));


	}
	/*
Hibernate:
    select
        b1_0.bno,
        b1_0.title,
        b1_0.writer,
        b1_0.regdate,
        count(r1_0.rno)
    from
        board b1_0
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    where
        (
            b1_0.title like ? escape '!'
            or b1_0.content like ? escape '!'
            or b1_0.writer like ? escape '!'
        )
        and b1_0.bno>?
    group by
        b1_0.bno
    order by
        b1_0.bno desc
    limit
        ?, ?
Hibernate:
    select
        count(distinct b1_0.bno)
    from
        board b1_0
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    where
        (
            b1_0.title like ? escape '!'
            or b1_0.content like ? escape '!'
            or b1_0.writer like ? escape '!'
        )
        and b1_0.bno>?
	 */

	@Test
	public void insertWithImages() {
		Board board = Board.builder()
				.title("Image Test")
				.content("file test")
				.writer("tester")
				.build();

		for (int i = 0; i < 3; i++) {
			board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
		}

		boardRepository.save(board);
	}
	/*
	insert 1 board 234 board_img
	 */

	@Test
	public void readWithImages() {
		Optional<Board> result = boardRepository.findByIdWithImage(101L);
		Board board = result.orElseThrow();
		log.info(board);
		log.info("-----------------------");
		for (BoardImage boardImage : board.getImageSet()) {
			log.info(boardImage);
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

	Board(bno=101, title=Image Test, content=file test, writer=tester)

	BoardImage(uuid=aba10885-92ee-4232-88a5-45717dd8d4e3, fileName=file1.jpg, ord=1)
	BoardImage(uuid=379a9a45-15ea-4413-9dc4-c2bb1df70465, fileName=file0.jpg, ord=0)
	BoardImage(uuid=794003a9-53b0-4d69-adbc-7b8dfe1ad8f9, fileName=file2.jpg, ord=2)
	 */

	@Transactional
	@Commit
	@Test
	public void modifyImages() {
		Optional<Board> result = boardRepository.findByIdWithImage(101L);
		Board board = result.orElseThrow();

		board.clearImages(); // 기존 첨부파일들 삭제

		for (int i = 0; i < 2; i++) {
			board.addImage(UUID.randomUUID().toString(), "updatefile"+i+".jpg");
		}
		boardRepository.save(board);
	}
	/*

1. sel board left join board_image
2. sel board_image left join board
3. "
4. insert 2
5. delete 3

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
Hibernate:
    select
        bi1_0.uuid,
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer,
        bi1_0.file_name,
        bi1_0.ord
    from
        board_image bi1_0
    left join
        board b1_0
            on b1_0.bno=bi1_0.board_bno
    where
        bi1_0.uuid=?
Hibernate:
    select
        bi1_0.uuid,
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer,
        bi1_0.file_name,
        bi1_0.ord
    from
        board_image bi1_0
    left join
        board b1_0
            on b1_0.bno=bi1_0.board_bno
    where
        bi1_0.uuid=?
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
	 */

	// 게시물과 첨부파일 삭제 p626
	@Test
	@Transactional
	@Commit
	public void removeWithReplies() {
		Long bno = 416L;

		replyRepository.deleteByBoard_Bno(bno);
		boardRepository.deleteById(bno);
	}

	// N+1문제와 @BatchSize p627
	@Test
	public void insertAll() {
		for (int i = 1; i <= 100; i++) {
			Board board = Board.builder()
					.title("Title.."+i)
					.content("Content.."+i)
					.writer("writer.."+i)
					.build();
			for (int j = 0; j < 3; j++) {
				if (i % 5 == 0) {
					continue;
				}
				board.addImage(UUID.randomUUID().toString(), i+"file"+j+".jpg");
			}
			boardRepository.save(board);
		}


	}

	// N+1문제 p629
	@Transactional
	@Test
	public void searchImageReplyCount() {
		Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

		// p636
		//Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);
		// p639
		String[] types = {"t","c","w"};
		Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, "0", pageable);
		log.info("---------------------");
		log.info(result.getTotalElements());
		result.getContent().forEach(log::info);

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
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    order by
        b1_0.bno desc
    limit
        ?, ?


2024-08-27T15:45:26.197+09:00  INFO 6520 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : 101
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

[BoardImage(uuid=faa10ccb-5445-4a12-b993-359eb480979c, fileName=updatefile0.jpg, ord=0), BoardImage(uuid=eaf2bd56-4619-4f5b-8461-37dc245eea6f, fileName=updatefile1.jpg, ord=1)]

2024-08-27T15:45:26.213+09:00  INFO 6520 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : ----------------------------
2024-08-27T15:45:26.213+09:00  INFO 6520 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : 100
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
2024-08-27T15:45:26.213+09:00  INFO 6520 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : []
2024-08-27T15:45:26.213+09:00  INFO 6520 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : ----------------------------


@BatchSize 추가후

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
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    order by
        b1_0.bno desc
    limit
        ?, ?
2024-08-27T15:51:32.686+09:00  INFO 7044 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : 101
Hibernate:
    select
        is1_0.board_bno,
        is1_0.uuid,
        is1_0.file_name,
        is1_0.ord
    from
        board_image is1_0
    where
        is1_0.board_bno in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) // 10
2024-08-27T15:51:32.701+09:00  INFO 7044 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : [BoardImage(uuid=faa10ccb-5445-4a12-b993-359eb480979c, fileName=updatefile0.jpg, ord=0), BoardImage(uuid=eaf2bd56-4619-4f5b-8461-37dc245eea6f, fileName=updatefile1.jpg, ord=1)]
2024-08-27T15:51:32.701+09:00  INFO 7044 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : ----------------------------
2024-08-27T15:51:32.701+09:00  INFO 7044 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : 100
2024-08-27T15:51:32.701+09:00  INFO 7044 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : []
2024-08-27T15:51:32.701+09:00  INFO 7044 --- [demo] [           main] c.e.d.repository.search.BoardSearchImpl  : ----------------------------


p636

Hibernate:
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer,
        count(distinct r1_0.rno)
    from
        board b1_0
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    group by
        b1_0.bno
    order by
        b1_0.bno desc
    limit
        ?, ?
Hibernate:
    select
        count(distinct b1_0.bno)
    from
        board b1_0
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
2024-08-27T16:28:22.543+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : ---------------------
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : 101
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=101, title=Image Test, writer=tester, regDate=2024-08-27T15:16:52.676322, replyCount=0, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=100, title=Title..100, writer=writer..100, regDate=2024-08-26T16:15:57.781552, replyCount=162, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=99, title=Title..99, writer=writer..99, regDate=2024-08-26T16:15:57.776552, replyCount=0, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=98, title=Title..98, writer=writer..98, regDate=2024-08-26T16:15:57.772553, replyCount=0, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=97, title=Title..97, writer=writer..97, regDate=2024-08-26T16:15:57.768555, replyCount=0, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=96, title=Title..96, writer=writer..96, regDate=2024-08-26T16:15:57.763553, replyCount=0, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=95, title=Title..95, writer=writer..95, regDate=2024-08-26T16:15:57.760552, replyCount=0, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=94, title=Title..94, writer=writer..94, regDate=2024-08-26T16:15:57.756552, replyCount=0, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=93, title=Title..93, writer=writer..93, regDate=2024-08-26T16:15:57.752554, replyCount=0, boardImages=null)
2024-08-27T16:28:22.559+09:00  INFO 12492 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=92, title=Title..92, writer=writer..92, regDate=2024-08-26T16:15:57.747553, replyCount=0, boardImages=null)

//p638
페이징, 이미지, 카운트

Hibernate: 
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer,
        count(distinct r1_0.rno) 
    from
        board b1_0 
    left join
        reply r1_0 
            on r1_0.board_bno=b1_0.bno 
    group by
        b1_0.bno 
    order by
        b1_0.bno desc 
    limit
        ?, ?
Hibernate: 
    select
        is1_0.board_bno,
        is1_0.uuid,
        is1_0.file_name,
        is1_0.ord 
    from
        board_image is1_0 
    where
        is1_0.board_bno in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
Hibernate: 
    select
        count(distinct b1_0.bno) 
    from
        board b1_0 
    left join
        reply r1_0 
            on r1_0.board_bno=b1_0.bno


 BoardListAllDTO(bno=101, title=Image Test, writer=tester, regDate=2024-08-27T15:16:52.676322, replyCount=0, boardImages=[BoardImageDTO(uuid=faa10ccb-5445-4a12-b993-359eb480979c, fileName=updatefile0.jpg, ord=0), BoardImageDTO(uuid=eaf2bd56-4619-4f5b-8461-37dc245eea6f, fileName=updatefile1.jpg, ord=1)])
2024-08-27T16:34:12.459+09:00  INFO 10312 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=100, title=Title..100, writer=writer..100, regDate=2024-08-26T16:15:57.781552, replyCount=162, boardImages=[])
2024-08-27T16:34:12.459+09:00  INFO 10312 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=99, title=Title..99, writer=writer..99, regDate=2024-08-26T16:15:57.776552, replyCount=0, boardImages=[BoardImageDTO(uuid=315e6e39-e292-4583-87ed-ce722b6e7c9e, fileName=99file0.jpg, ord=0), BoardImageDTO(uuid=4e71078f-9aeb-45d5-919f-7e6e3ba40d52, fileName=99file1.jpg, ord=1), BoardImageDTO(uuid=d0918593-34b1-4caa-a590-6b0d5926e6b3, fileName=99file2.jpg, ord=2)])
2024-08-27T16:34:12.459+09:00  INFO 10312 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=98, title=Title..98, writer=writer..98, regDate=2024-08-26T16:15:57.772553, replyCount=0, boardImages=[BoardImageDTO(uuid=52c2d1a1-2b6d-4f18-9108-9c4e03c67051, fileName=98file0.jpg, ord=0), BoardImageDTO(uuid=782305d3-7f87-4507-89e5-97b422b429df, fileName=98file1.jpg, ord=1), BoardImageDTO(uuid=7540df34-21fa-4343-92c8-afaf2e671061, fileName=98file2.jpg, ord=2)])
2024-08-27T16:34:12.459+09:00  INFO 10312 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=97, title=Title..97, writer=writer..97, regDate=2024-08-26T16:15:57.768555, replyCount=0, boardImages=[BoardImageDTO(uuid=bbd15060-196a-43d6-a9fd-8930e5a64348, fileName=97file0.jpg, ord=0), BoardImageDTO(uuid=543cb700-bf9c-408a-9661-3ba894343d31, fileName=97file1.jpg, ord=1), BoardImageDTO(uuid=ac72cd2e-b61d-49a3-b2c7-e93c169a79e4, fileName=97file2.jpg, ord=2)])
2024-08-27T16:34:12.459+09:00  INFO 10312 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=96, title=Title..96, writer=writer..96, regDate=2024-08-26T16:15:57.763553, replyCount=0, boardImages=[BoardImageDTO(uuid=a1ae6f3f-f186-4722-9a3b-32ee7826bf75, fileName=96file0.jpg, ord=0), BoardImageDTO(uuid=664edd1f-718d-4aa3-9da9-3e50420601b2, fileName=96file1.jpg, ord=1), BoardImageDTO(uuid=e011da60-9314-4fbb-917d-127965db68a1, fileName=96file2.jpg, ord=2)])
2024-08-27T16:34:12.459+09:00  INFO 10312 --- [demo] [           main] c.e.demo.repository.BoardRepositoryTest  : BoardListAllDTO(bno=95, title=Title..95, writer=writer..95, regDate=2024-08-26T16:15:57.760552, replyCount=0, boardImages=[])


p639 search

Hibernate:
    select
        b1_0.bno,
        b1_0.content,
        b1_0.moddate,
        b1_0.regdate,
        b1_0.title,
        b1_0.writer,
        count(distinct r1_0.rno)
    from
        board b1_0
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    where
        b1_0.title like ? escape '!'
        or b1_0.content like ? escape '!'
        or b1_0.writer like ? escape '!'
    group by
        b1_0.bno
    order by
        b1_0.bno desc
    limit
        ?, ?
Hibernate:
    select
        is1_0.board_bno,
        is1_0.uuid,
        is1_0.file_name,
        is1_0.ord
    from
        board_image is1_0
    where
        is1_0.board_bno in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
Hibernate:
    select
        count(distinct b1_0.bno)
    from
        board b1_0
    left join
        reply r1_0
            on r1_0.board_bno=b1_0.bno
    where
        b1_0.title like ? escape '!'
        or b1_0.content like ? escape '!'
        or b1_0.writer like ? escape '!'
 */


}
