package com.project.Debate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DebatePostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debatePostImageNo;

    private String debateContentVideoPath;  //영상 주소
    private String debateContentImagePath;  //이미지 주소
    private String debateContentImagePath2; //이미지 주소2
    private String debateContentImagePath3; //이미지 주소3

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debate_article_no")
    private DebatePost debatePost;      //해당 포스트



}
