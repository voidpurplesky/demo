package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bno;
	
	@Column(length = 500, nullable = false)
	private String title;
	
	@Column(length = 2000, nullable = false)
	private String content;
	
	@Column(length = 50, nullable = false)
	private String writer;
	
	public void change(String title, String content) {
		this.title = title;
		this.content = content;
	}

	// p617
	// orphanRemoval = true 하위 엔티티의 참조가 더이상 없는 상태가 되면 삭제
	@OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true) // BoardImage의 board 변수
	@Builder.Default
	@BatchSize(size = 10)
	private Set<BoardImage> imageSet = new HashSet<>();

	public void addImage(String uuid, String fileName) {

		BoardImage boardImage = BoardImage.builder()
				.uuid(uuid)
				.fileName(fileName)
				.board(this)
				.ord(imageSet.size())
				.build();
		imageSet.add(boardImage);
	}

	public void clearImages() {
		imageSet.forEach(boardImage -> boardImage.changeBoard(null));
		this.imageSet.clear();
	}
}
