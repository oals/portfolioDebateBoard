package com.project.Debate.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LatestDebateDTO {

    private Long debateArticleNo;
    private String debateCategory;
    private String debateTitle;



}
