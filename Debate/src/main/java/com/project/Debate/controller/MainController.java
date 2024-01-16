package com.project.Debate.controller;

import com.project.Debate.dto.*;
import com.project.Debate.service.DebateBoardService;
import com.project.Debate.service.DebateBookMarkService;
import com.project.Debate.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.juli.logging.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {


    private final DebateBoardService debateBoardService;
    private final MemberService memberService;

    private final DebateBookMarkService debateBookMarkService;

    @GetMapping("/")
    public String main(Model model){


        List<DebatePostPreviewDTO> debatePostPreviewDTOS =  debateBoardService.SelectDebatePostPreview();

        model.addAttribute("debatePostPreviewDTO",debatePostPreviewDTOS);

        return "index";
    }


    @GetMapping("/DebateBoard")
    public String DebateBoard(String DebateCategoryName, Model model, Principal principal){

        log.info("토론 게시판 접근 ");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            List<LatestDebateDTO> latestDebateDTOS =  debateBoardService.SelectLatestDebate(principal.getName());

            PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                    .size(5)
                    .page(1)
                    .build();


            List<DebateBookMarkDTO> debateBookMarkDTOS = debateBookMarkService.SelectDebateBookMArk(principal.getName(),pageRequestDTO);


//            DebateCombatStatsDTO debateCombatStatsDTO = memberService.SelectMemberWinRate(principal.getName());

            model.addAttribute("latestDebateDTO",latestDebateDTOS);
//            model.addAttribute("debateCombatStatsDTO",debateCombatStatsDTO);
            model.addAttribute("debateBookMarkDTOS",debateBookMarkDTOS);

        }

        List<PopularCategoryDebateDTO> popularCategoryDebateDTOS = debateBoardService.SelectPopularCategoryDebate(DebateCategoryName);
        List<PopularDebateDTO> popularDebateDTOS = debateBoardService.SelectPopularDebate();



        model.addAttribute("popularCategoryDebateDTO",popularCategoryDebateDTOS);
        model.addAttribute("popularDebateDTO",popularDebateDTOS);
        model.addAttribute("DebateCategoryName",DebateCategoryName);

        return "Board/DebateBoard";
    }






}
