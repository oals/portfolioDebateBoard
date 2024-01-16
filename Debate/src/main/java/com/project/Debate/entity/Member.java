package com.project.Debate.entity;

import com.project.Debate.constant.Role;
import com.project.Debate.dto.MemberDTO;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.Name;
import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="member")
public class Member {

    @Id
    @Column(nullable = false,length = 50,name="member_id")
    private String memberId;

    private String memberPswd;
    private String memberName;
    private String memberPhone;
    private String memberAddress;

    private int memberDebateWinCount;       //분쟁의 승리를 맞춘 수
    private int memberDebateLoseCount;      //분쟁의 승리를 맞추지 못한 수
    private int memberDebateDrawCount;      //분쟁의 무승부 수

    private Double memberWinningPercent;    //승률
    private String memberDate;

    @Enumerated(EnumType.STRING)
    private Role role;  //권한 설정




    public static Member createMember(MemberDTO memberDTO, PasswordEncoder passwordEncoder){

        Member member = new Member();
        member.setMemberId(memberDTO.getMemberId());
        member.setMemberName(memberDTO.getMemberName());
        member.setMemberPhone(memberDTO.getMemberPhone());
        member.setMemberAddress(memberDTO.getMemberAddress());
        member.setMemberDebateWinCount(memberDTO.getMemberDebateWinCount());
        member.setMemberDebateLoseCount(memberDTO.getMemberDebateLoseCount());
        member.setMemberDebateDrawCount(memberDTO.getMemberDebateDrawCount());
        member.setMemberWinningPercent(memberDTO.getMemberWinningPercent());
        member.setMemberDate(memberDTO.getMemberDate());
        member.setRole(Role.USER);

        // 암호화
        String password = passwordEncoder.encode(memberDTO.getMemberPswd());
        member.setMemberPswd(password);

        return member;
    }



}
