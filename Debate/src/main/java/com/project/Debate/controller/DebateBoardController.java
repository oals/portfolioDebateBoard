package com.project.Debate.controller;


import com.project.Debate.dto.*;
import com.project.Debate.entity.DebatePost;
import com.project.Debate.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor

public class DebateBoardController {

    private final DebateBoardService debateBoardService;
    private final CreateDirectoryService createDirectoryService;

    private final FileService fileService;

    private final DebateBookMarkService debateBookMarkService;

    @Value("${itemImgLocation}")  //application-properties속성의 변수 값 가져오기
    private String itemImgLocation;  //blogNo 넘겨서 폴더만들기 후 경로 지정

    @Value("${uploadPath}")  //application-properties속성의 변수 값 가져오기
    private String uploadPath;  //blogNo 넘겨서 폴더만들기 후 경로 지정


    @GetMapping("/DebatePostCreatePage")
    public String DebatePostCreatePage(Model model,String DebateCategoryName){

        model.addAttribute("DebateCategoryName",DebateCategoryName);

        return "/Board/DebatePostCreatePage";
    }



    @GetMapping("/DebatePostPage")
    public String DebatePostPage(Long DebateArticleNo,Model model,Principal principal){



        debateBoardService.UpdateDebatePostViewCount(DebateArticleNo);



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                    .size(5)
                    .page(1)
                    .build();

            List<LatestDebateDTO> latestDebateDTOS =  debateBoardService.SelectLatestDebate(principal.getName());
            List<DebateBookMarkDTO> debateBookMarkDTOS = debateBookMarkService.SelectDebateBookMArk(principal.getName(),pageRequestDTO);


            model.addAttribute("latestDebateDTO",latestDebateDTOS);
            model.addAttribute("memberId",userDetails.getUsername());
            model.addAttribute("debateBookMarkDTOS",debateBookMarkDTOS);

        }else{
            model.addAttribute("memberId",null);
        }


        List<PopularDebateDTO> popularDebateDTOS = debateBoardService.SelectPopularDebate();
        DebatePostDTO debatePostDTO =  debateBoardService.GetSelectDebatePost(DebateArticleNo);


        model.addAttribute("popularDebateDTO",popularDebateDTOS);
        model.addAttribute("debatePostDTO",debatePostDTO);
        model.addAttribute("DebateCategoryName",debatePostDTO.getDebateCategory());

        return "/Board/DebatePostPage";
    }








    @PostMapping("/DebatePostCreate")
    public ModelAndView DebatePostCreate(DebatePostDTO debatePostDTO, Principal principal) throws IOException {



        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String createPostTime = now.format(formatter);

        debatePostDTO.setMemberId(principal.getName());
        debatePostDTO.setDebatePostDate(createPostTime);
        debatePostDTO.setDebateResult("진행중");
        debatePostDTO.setDebateState(true);


       Long articleNo =  debateBoardService.GetDebateBoardArticleNo();


       List<MultipartFile> imageFileList = new ArrayList<>();
       imageFileList.add(debatePostDTO.getDebateContentVideoData());
       imageFileList.add(debatePostDTO.getDebateContentImageData());
       imageFileList.add(debatePostDTO.getDebateContentImageData2());
       imageFileList.add(debatePostDTO.getDebateContentImageData3());


       List<String> imageFileUrlList = new ArrayList<>();


        if(imageFileList.size() != 0) {   //해당 글 내용에 이미지가 있으면

            //이미지 저장 폴더 경로
            String path = itemImgLocation + "/" + articleNo;  //  글 번호 추가

            //게시글 폴더 생성
            createDirectoryService.CreateDebatePostImageFolder(path);    //이 아티클 번호가 실제 아티클 번호여야 함

            //이미지 파일 업로드
            for(int i = 0; i <  imageFileList.size(); i++) {

                //이미지 파일 업로드
                if(!imageFileList.get(i).getOriginalFilename().isEmpty()){
                    String fileName = fileService.uploadFile(path, imageFileList.get(i).getOriginalFilename(), imageFileList.get(i).getBytes());
                    imageFileUrlList.add(fileName);

                }else{
                    imageFileUrlList.add(null);
                }
            }
        }

        debatePostDTO.setDebateContentVideoPath(imageFileUrlList.get(0));
        debatePostDTO.setDebateContentImagePath(imageFileUrlList.get(1));
        debatePostDTO.setDebateContentImagePath2(imageFileUrlList.get(2));
        debatePostDTO.setDebateContentImagePath3(imageFileUrlList.get(3));


        debateBoardService.CreateDebatePost(debatePostDTO);


        //컨트롤러 -> 컨트롤러 이동 코드

        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/DebateBoard");
        MAV.addObject("DebateCategoryName", debatePostDTO.getDebateCategory());

        return MAV;
    }
    
    
    @GetMapping("/updateDebatePostPage")
    public String updateDebatePostPage(Long debateArticleNo , Model model){

        DebatePostDTO debatePostDTO = debateBoardService.GetSelectDebatePost(debateArticleNo);

       model.addAttribute("debatePostDTO",debatePostDTO);
       model.addAttribute("DebateCategoryName",debatePostDTO.getDebateCategory());

        return "Board/DebatePostCreatePage";
    }


    @PostMapping("updateDebatePost")
    public ModelAndView updateDebatePost(DebatePostDTO debatePostDTO) throws IOException{

        //변경된 데이터 검사
        debateBoardService.updateDebatePost(debatePostDTO);

        //변경된 이미지 데이터 검사
        debateBoardService.updateDebatePostImage(debatePostDTO);



        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/DebatePostPage");
        MAV.addObject("DebateArticleNo", debatePostDTO.getDebateArticleNo());

        return MAV;
    }
    
    

    
    
    
    
    
    











}
