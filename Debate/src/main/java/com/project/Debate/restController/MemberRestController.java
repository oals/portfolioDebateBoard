package com.project.Debate.restController;


import com.project.Debate.dto.DebateBookMarkDTO;
import com.project.Debate.dto.MyPageDebateListDTO;
import com.project.Debate.dto.PageRequestDTO;
import com.project.Debate.dto.PageResponseDTO;
import com.project.Debate.service.DebateBoardService;
import com.project.Debate.service.DebateBookMarkService;
import com.project.Debate.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final DebateBoardService debateBoardService;

    private final DebateBookMarkService debateBookMarkService;
    @GetMapping("/memberIdCheck")
    public boolean memberIdCheck(String memberId){



        boolean chk = memberService.searchId(memberId);

        return chk;

    }


    @GetMapping("/myPageLatestDebate")
    public List<MyPageDebateListDTO> myPageLatestDebate(Principal principal, PageRequestDTO pageRequestDTO){


        pageRequestDTO.setSize(20);
        List<MyPageDebateListDTO> latestDebateDTOS = debateBoardService.SelectMyPageLatestDebate(principal.getName(),pageRequestDTO);

        return latestDebateDTOS;
    }

    @GetMapping("/myPageLatestDebatePaging")
    public PageResponseDTO<MyPageDebateListDTO>  myPageLatestDebatePaging(Principal principal, PageRequestDTO pageRequestDTO){

        pageRequestDTO.setSize(20);
        PageResponseDTO<MyPageDebateListDTO>  latestDebateDTOS = debateBoardService.SelectMyPageLatestDebatePaging(principal.getName(),pageRequestDTO);

        return latestDebateDTOS;
    }

    @GetMapping("/MyPageMyDebateList")
    public List<MyPageDebateListDTO> MyPageMyDebateList(Principal principal, PageRequestDTO pageRequestDTO){


        pageRequestDTO.setSize(20);
        List<MyPageDebateListDTO> debatePostDTOList = debateBoardService.SelectMyPageDebatePost(principal.getName(),pageRequestDTO);

        return debatePostDTOList;
    }

    @GetMapping("/MyPageMyDebateListPaging")
    public PageResponseDTO<MyPageDebateListDTO>  MyPageMyDebateListPaging(Principal principal, PageRequestDTO pageRequestDTO){

        pageRequestDTO.setSize(20);
        PageResponseDTO<MyPageDebateListDTO> debatePostDTOList = debateBoardService.SelectMyPageDebatePostPaging(principal.getName(),pageRequestDTO);

        return debatePostDTOList;
    }



    @GetMapping("/MyPageMyBookMarkList")
    public List<DebateBookMarkDTO>  MyPageMyBookMarkList(Principal principal, PageRequestDTO pageRequestDTO){

        pageRequestDTO.setSize(20);
        List<DebateBookMarkDTO> debateBookMarkDTOList = debateBookMarkService.SelectDebateBookMArk(principal.getName(),pageRequestDTO);

        return debateBookMarkDTOList;
    }



    @GetMapping("/MyPageMyBookMarkListPaging")
    public PageResponseDTO<DebateBookMarkDTO>  MyPageMyBookMarkListPaging(Principal principal, PageRequestDTO pageRequestDTO){

        pageRequestDTO.setSize(20);
        PageResponseDTO<DebateBookMarkDTO> debateBookMarkDTOList = debateBookMarkService.SelectDebateBookMarkPaging(principal.getName(),pageRequestDTO);

        return debateBookMarkDTOList;
    }

    @Transactional
    @DeleteMapping("/AllDeleteMyBookMark")
    public void AllDeleteMyBookMark(Principal principal){

        debateBookMarkService.AllDeleteBookMark(principal.getName());

    }

    @Transactional
    @DeleteMapping("/DeleteMyBookMark")
    public void DeleteMyBookMark(Principal principal,Long articleNo){

        debateBookMarkService.DeleteBookMark(articleNo);

    }

}
