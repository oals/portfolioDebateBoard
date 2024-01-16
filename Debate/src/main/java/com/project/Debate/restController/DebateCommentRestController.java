package com.project.Debate.restController;

import com.project.Debate.dto.DebateCommentDTO;
import com.project.Debate.dto.PageRequestDTO;
import com.project.Debate.dto.PageResponseDTO;
import com.project.Debate.service.DebateCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class DebateCommentRestController {


    private final DebateCommentService debateCommentService;

    @PostMapping("/InsertDebateComment")
    public void InsertDebateComment(@RequestBody DebateCommentDTO debateCommentDTO, Principal principal){


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String createCommentTime = now.format(formatter);

        debateCommentDTO.setMemberId(principal.getName());
        debateCommentDTO.setDebatePostCommentDate(createCommentTime);

        //댓글 저장
        debateCommentService.InsertDebateComment(debateCommentDTO);


    }
    @GetMapping("/SelectDebateComment")
    public List<DebateCommentDTO> SelectDebateComment(Long debateArticleNo, PageRequestDTO pageRequestDTO){
        pageRequestDTO.setSize(20);

        log.info(pageRequestDTO.getPage());

        //댓글 가져오기
        List<DebateCommentDTO>  debateCommentDTOS =  debateCommentService.SelectDebateComment(debateArticleNo,pageRequestDTO);


        return debateCommentDTOS;

    }

    @GetMapping("/SelectDebateCommentPaging")
    public PageResponseDTO<DebateCommentDTO> SelectDebateCommentPaging(Long debateArticleNo, PageRequestDTO pageRequestDTO){
        pageRequestDTO.setSize(20);

        log.info(pageRequestDTO.getPage());

        //댓글 가져오기
        PageResponseDTO<DebateCommentDTO>  debateCommentDTOS =  debateCommentService.SelectDebateCommentPaging(debateArticleNo,pageRequestDTO);



        return debateCommentDTOS;

    }
    @PostMapping("/updateCommentLike")
    public boolean updateCommentLike(Long debateCommentNo,Principal principal){


        boolean chk = debateCommentService.updateCommentLike(debateCommentNo,principal.getName());

        return chk;
    }


    @DeleteMapping("/deleteComment")
    public void deleteComment(Long debateCommentNo){



        debateCommentService.deleteComment(debateCommentNo);

    }




}
