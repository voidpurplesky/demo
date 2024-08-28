/*
7. 업로드 처리 - 1. 첨부파일과 @OneToMany - 파일 업로드를 위한 설정 - 업로드 처리를 위한 DTO p598
 */
package com.example.demo.dto.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO {

    private List<MultipartFile> files;
}
