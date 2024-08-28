/*
6.1 REST 방식의 서비스 - REST 방식의 댓글 처리 준비 - ReplyController 준비 p522
6.2 다대일 Many To One 연관관계 실습 - 컨트롤러 계층 구현 p555
 */
package com.example.demo.controller;

import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponseDTO;
import com.example.demo.dto.ReplyDTO;
import com.example.demo.service.ReplyService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;
/*
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록") // swagger
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> register(@RequestBody ReplyDTO replyDTO) {
        log.info(replyDTO);
        Map<String, Long> resultMap = Map.of("rno", 111L);
        return ResponseEntity.ok(resultMap);
    }

 */
    // 6.2 다대일 Many To One 연관관계 실습 - 컨트롤러 계층 구현 - 등록 기능 확인 p555
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록") // swagger
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException {
        log.info(replyDTO);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        // 등록 기능 확인 p555
        Long rno = replyService.register(replyDTO);
        resultMap.put("rno", rno);

        return resultMap;
    }
    /*
{
    "replyText": "NotEmpty",
    "bno": "NotNull",
    "replyer": "NotEmpty"
}
     */

    // 특정 게시물의 댓글 목록 p560
    @ApiOperation(value = "Replies of Board", notes = "Get 방식으로 특정 게시물의 댓글 목록")
    @GetMapping("/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
        return responseDTO;
    }

    // 특정 댓글 조회 p561
    @ApiOperation(value = "Read Reply", notes = "Get 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {
        return replyService.read(rno);
    }

    // 특정 댓글 삭제 p563
    @ApiOperation(value = "Delete Reply", notes = "Delete 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno) {
        replyService.remove(rno);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {
        replyDTO.setRno(rno);
        replyService.modify(replyDTO);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }
}
