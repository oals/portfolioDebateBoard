package com.project.Debate.controller;

import com.project.Debate.dto.*;
import com.project.Debate.repository.RegisterRepository;
import com.project.Debate.service.DebateBoardService;
import com.project.Debate.service.DebateBookMarkService;
import com.project.Debate.service.MemberService;
import com.project.Debate.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final RegisterService registerService;

    private final MemberService memberService;

    private final DebateBoardService debateBoardService;

    private final DebateBookMarkService debateBookMarkService;

    @GetMapping("/user/login")
    public String loginPage(){

        return "login/LoginPage";
    }

    @GetMapping("/user/login/error")
    public String loginErrorPage(){

        return "login/error";
    }

    @PostMapping(value="/logout")
    public String logout(){
        log.info("logout... ");
        return "redirect :/user/login?logout";
    }

    @GetMapping("/registerPage")
    public String registerPage(){

        return "register/registerPage";
    }


    @GetMapping("/MyPage")
    public String MyPage(Principal principal,Model model){

        //회원 정보
        MemberDTO memberDTO = memberService.findByMember(principal.getName());

        //토론 참여 승률
        DebateCombatStatsDTO debateCombatStatsDTO = memberService.SelectMemberWinRate(principal.getName());


        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(8)
                .build();

        //참여 토론 리스트
        List<MyPageDebateListDTO> latestDebateDTOS = debateBoardService.SelectMyPageLatestDebate(principal.getName(),pageRequestDTO);

        //내가 작성한 글 리스트
        List<MyPageDebateListDTO> debatePostDTOList = debateBoardService.SelectMyPageDebatePost(principal.getName(),pageRequestDTO);

        //북마크 글 가져오기
        List<DebateBookMarkDTO> debateBookMarkDTOList = debateBookMarkService.SelectDebateBookMArk(principal.getName(),pageRequestDTO);




        model.addAttribute("memberDTO",memberDTO);
        model.addAttribute("debateCombatStatsDTO",debateCombatStatsDTO);
        model.addAttribute("latestDebateDTOS",latestDebateDTOS);
        model.addAttribute("debatePostDTOList",debatePostDTOList);
        model.addAttribute("debateBookMarkDTOList",debateBookMarkDTOList);



        
        return "Member/MyPage";
    }
    @PostMapping(value="/register")
    public ModelAndView register(MemberDTO memberDTO, Model model){


        log.info("가입 정보 : "+memberDTO);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String registerDate = now.format(formatter);

        memberDTO.setMemberDebateWinCount(0);
        memberDTO.setMemberDebateLoseCount(0);
        memberDTO.setMemberDebateDrawCount(0);
        memberDTO.setMemberWinningPercent(0.0);
        memberDTO.setMemberDate(registerDate);

        //회원 정보 저장
        registerService.register(memberDTO);


        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/");
        return MAV;

    }



}
