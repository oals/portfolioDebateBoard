package com.project.Debate.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DebatePostDTO {

    private String memberId;

    private Long debateArticleNo;
    private String debateTitle;
    private String debateSituation;
    private String  debateCategory;
    private String debateContentTextA;
    private String debateContentTextB;
    private String debateStartDate;
    private String debateSelectTime;

    private String debateRunningTime;
    private int debatePostView;
    private String debatePostDate;
    private String debateResult;
    private Boolean debateState;


    private int aVotes;
    private int bVotes;
    private String debateWinner;


    private MultipartFile debateContentVideoData;
    private MultipartFile  debateContentImageData;
    private MultipartFile  debateContentImageData2;
    private MultipartFile  debateContentImageData3;

    private String debateContentVideoPath;
    private String  debateContentImagePath;
    private String  debateContentImagePath2;
    private String  debateContentImagePath3;


}
