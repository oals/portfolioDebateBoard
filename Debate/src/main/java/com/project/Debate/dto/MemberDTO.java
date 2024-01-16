package com.project.Debate.dto;

import com.project.Debate.constant.Role;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {

    private String memberId;
    private String memberPswd;
    private String memberName;
    private String memberPhone;
    private String memberAddress;

    private int memberDebateWinCount;

    private int memberDebateLoseCount;
    private int memberDebateDrawCount;
    private Double memberWinningPercent ;
    private String memberDate;
    private Role role;  //권한 설정

}
