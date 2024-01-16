package com.project.Debate.service;

import com.project.Debate.dto.DebateCombatStatsDTO;
import com.project.Debate.dto.DebateCommentDTO;
import com.project.Debate.dto.MemberDTO;
import com.project.Debate.entity.Member;
import com.project.Debate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    @Override
    public boolean searchId(String memberId) {


        Long count =  memberRepository.findById(memberId).stream().count();
        boolean chk = false;


        if(count == 0){
            chk =true;

        }


        return chk;
    }

    @Override
    public void updateMemberDebateWinningPercent(Map<String,String> memberNameMap,String result) {

        List<Member> memberList = new ArrayList<>();



        for (Map.Entry<String, String> entry : memberNameMap.entrySet()) {
            String memberId = entry.getKey();
            String prosAndCons = entry.getValue();

            Member member =  memberRepository.findById(memberId).orElseThrow();

            int memberWinCount = member.getMemberDebateWinCount();
            int memberLoseCount = member.getMemberDebateLoseCount();
            int memberDrawCount = member.getMemberDebateDrawCount();

            if(result.equals("D")){
                memberDrawCount = memberDrawCount + 1;
                member.setMemberDebateDrawCount(memberDrawCount+ 1);

            }else{
                if(result.equals(prosAndCons)){
                    member.setMemberDebateWinCount(memberWinCount + 1);
                    memberWinCount = memberWinCount + 1;
                }else {
                    member.setMemberDebateLoseCount(memberLoseCount + 1);
                    memberLoseCount = memberLoseCount + 1;
                }
            }


            int totalGames = memberWinCount + memberLoseCount + memberDrawCount;
            double winRate = (double) memberWinCount / totalGames * 100;

            member.setMemberWinningPercent(Math.round(winRate * 100.0) / 100.0);
            memberList.add(member);

        }

        memberRepository.saveAll(memberList);


    }

    @Override
    public MemberDTO findByMember(String memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow();

        MemberDTO memberDTO = MemberDTO.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .memberAddress(member.getMemberAddress())
                .memberDate(member.getMemberDate())
                .memberPhone(member.getMemberPhone())
                .build();

        return memberDTO;
    }

    @Override
    public DebateCombatStatsDTO SelectMemberWinRate(String memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow();

        DebateCombatStatsDTO debateCombatStatsDTO =  DebateCombatStatsDTO.builder()
                .memberDebateWinCount(member.getMemberDebateWinCount())
                .memberDebateLoseCount(member.getMemberDebateLoseCount())
                .memberDebateDrawCount(member.getMemberDebateDrawCount())
                .memberWinningPercent(member.getMemberWinningPercent())
                .build();


        return debateCombatStatsDTO;
    }
}









