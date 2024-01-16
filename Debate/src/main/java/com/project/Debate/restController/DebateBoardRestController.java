package com.project.Debate.restController;

import com.project.Debate.dto.*;
import com.project.Debate.entity.DebatePost;
import com.project.Debate.service.DebateBoardService;
import com.project.Debate.service.DebateBookMarkService;
import com.project.Debate.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class DebateBoardRestController {

    @Value("${itemImgLocation}")  //application-properties속성의 변수 값 가져오기
    private String itemImgLocation;  //blogNo 넘겨서 폴더만들기 후 경로 지정

    @Value("${uploadPath}")  //application-properties속성의 변수 값 가져오기
    private String uploadPath;  //blogNo 넘겨서 폴더만들기 후 경로 지정

    private final DebateBoardService debateBoardService;

    private final DebateBookMarkService bookMarkService;

    private final FileService fileService;


    @GetMapping("/SelectDebatePost")
    public List<DebatePostDTO> SelectDebatePost(String DebateCategoryName,String debateResult, PageRequestDTO pageRequestDTO){
        pageRequestDTO.setSize(8);

        log.info(pageRequestDTO.getPage());
        log.info(debateResult);

        List<DebatePostDTO>  debateCommentDTOS =  debateBoardService.SelectDebatePost(DebateCategoryName,debateResult,pageRequestDTO);



        return debateCommentDTOS;

    }


    @GetMapping("/SelectDebatePostPaging")
    public PageResponseDTO<DebatePostDTO> SelectDebatePostPaging(String DebateCategoryName,String debateResult, PageRequestDTO pageRequestDTO){
        pageRequestDTO.setSize(8);

        log.info(pageRequestDTO.getPage());
        log.info(debateResult);

        PageResponseDTO<DebatePostDTO>  debateCommentDTOS =  debateBoardService.SelectDebatePostPaging(DebateCategoryName,debateResult,pageRequestDTO);



        return debateCommentDTOS;

    }




    @GetMapping("/ProsAndConsCheck")
    public boolean ProsAndConsCheck(Long debateArticleNo,Principal principal){

        boolean chk = false;


        chk = debateBoardService.ProsAndConsCheck(debateArticleNo,principal.getName());
        log.info("검사 결과 : " + chk);

        return chk;
    }

    @PostMapping("/ProsAndCons")
    public void ProsAndCons(Long debateArticleNo,String DebateVoteState , Principal principal){

        debateBoardService.ProsAndCons(debateArticleNo,DebateVoteState,principal.getName());

    }

    @GetMapping("/SelectSearchDebatePost")
    public List<DebatePostDTO> SelectSearchDebatePost(String debateSearchOpt,String debateSearchData,
                                                                 String debateCategoryName,PageRequestDTO pageRequestDTO){

        pageRequestDTO.setSize(8);

        List<DebatePostDTO>debatePostDTOList = debateBoardService.SelectSearchDebatePost(debateSearchOpt,debateSearchData,debateCategoryName,pageRequestDTO);


        return debatePostDTOList;
    }


    @GetMapping("/SelectSearchDebatePostPaging")
    public PageResponseDTO<DebatePostDTO> SelectSearchDebatePostPaging(String debateSearchOpt,String debateSearchData,
                                                                 String debateCategoryName,PageRequestDTO pageRequestDTO){

        pageRequestDTO.setSize(8);

        PageResponseDTO<DebatePostDTO >debatePostDTOList = debateBoardService.SelectSearchDebatePostPaging(debateSearchOpt,debateSearchData,debateCategoryName,pageRequestDTO);


        return debatePostDTOList;
    }

    @DeleteMapping("deleteDebatePost")
    public void deleteDebatePost(Long debateArticleNo){


        //db 데이터 삭제
        debateBoardService.deleteDebatePost(debateArticleNo);

        //폴더 삭제
        String delPath =  itemImgLocation  + "/" + debateArticleNo;
        fileService.deleteFolder(delPath);

    }

    @GetMapping("/debatePostBookMarkChk")
    public Boolean debatePostBookMarkChk(Principal principal,Long debateArticleNo){

        boolean chk = bookMarkService.debatePostBookMarkChk(principal.getName(),debateArticleNo);

        return chk;
    }


    @PostMapping("/debatePostBookMark")
    public void debatePostBookMark(Principal principal,Long debateArticleNo){

        bookMarkService.debatePostBookMark(principal.getName(),debateArticleNo);

    }




}
