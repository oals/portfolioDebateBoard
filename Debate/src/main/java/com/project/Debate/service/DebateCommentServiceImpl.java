package com.project.Debate.service;

import com.project.Debate.dto.DebateCommentDTO;
import com.project.Debate.dto.DebatePostDTO;
import com.project.Debate.dto.PageRequestDTO;
import com.project.Debate.dto.PageResponseDTO;
import com.project.Debate.entity.*;
import com.project.Debate.repository.DebateCommentLikeRepository;
import com.project.Debate.repository.DebateCommentRepository;
import com.project.Debate.repository.DebateProsAndConsRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DebateCommentServiceImpl implements DebateCommentService {


    private final DebateProsAndConsRepository debateProsAndConsRepository;

    @PersistenceContext
    EntityManager em;

    private final DebateCommentRepository debateCommentRepository;

    private final DebateCommentLikeRepository debateCommentLikeRepository;
    @Override
    public void InsertDebateComment(DebateCommentDTO debateCommentDTO) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePostProsAndCons qDebatePostProsAndCons = QDebatePostProsAndCons.debatePostProsAndCons;


        Member member = Member.builder()
                .memberId(debateCommentDTO.getMemberId())
                .build();

        DebatePost debatePost = DebatePost.builder()
                .debateArticleNo(debateCommentDTO.getDebateArticleNo())
                .build();


        DebatePostProsAndCons debatePostProsAndCons = queryFactory.selectFrom(qDebatePostProsAndCons)
                .where(qDebatePostProsAndCons.debatePost.debateArticleNo.eq(debateCommentDTO.getDebateArticleNo())
                        .and(qDebatePostProsAndCons.member.memberId.eq(debateCommentDTO.getMemberId())))
                .fetchOne();


        DebateComment debateComment = DebateComment.builder()
                .debatePostComment(debateCommentDTO.getDebatePostComment())
                .debatePostCommentDate(debateCommentDTO.getDebatePostCommentDate())
                .debatePostCommentParent(debateCommentDTO.getDebatePostCommentParent())
                .debatePost(debatePost)
                .debatePostProsAndCons(debatePostProsAndCons)
                .member(member)
                .build();




        debateCommentRepository.save(debateComment);


    }

    @Override
    public List<DebateCommentDTO> SelectDebateComment(Long debateArticleNo, PageRequestDTO pageRequestDTO) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebateComment qDebateComment = QDebateComment.debateComment;

        Pageable pageable = pageRequestDTO.getPageable();

        List<DebateComment> query = queryFactory.selectFrom(qDebateComment)
                .where(qDebateComment.debatePost.debateArticleNo.eq(debateArticleNo))
                .orderBy(qDebateComment.debateCommentNo.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();

        List<DebateCommentDTO> debateCommentDTOS = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){

           DebateCommentDTO debateCommentDTO = DebateCommentDTO.builder()
                   .debateCommentNo(query.get(i).getDebateCommentNo())
                   .debatePostComment(query.get(i).getDebatePostComment())
                   .debatePostCommentParent(query.get(i).getDebatePostCommentParent())
                   .debatePostCommentDate(query.get(i).getDebatePostCommentDate())
                   .memberId(query.get(i).getMember().getMemberId())
                   .debateArticleNo(query.get(i).getDebatePost().getDebateArticleNo())
                   .debateProsAndConsState(query.get(i).getDebatePostProsAndCons().getDebateVoteState())
                   .debateCommentLikeCount(query.get(i).getDebateCommentLikeList().size())
                   .build();

            debateCommentDTOS.add(debateCommentDTO);
        }

       return debateCommentDTOS;
    }


    @Override
    public PageResponseDTO<DebateCommentDTO> SelectDebateCommentPaging(Long debateArticleNo, PageRequestDTO pageRequestDTO) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebateComment qDebateComment = QDebateComment.debateComment;

        Pageable pageable = pageRequestDTO.getPageable();

        List<DebateComment> query = queryFactory.selectFrom(qDebateComment)
                .where(qDebateComment.debatePost.debateArticleNo.eq(debateArticleNo))
                .orderBy(qDebateComment.debateCommentNo.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();


        Long count = queryFactory
                .select(qDebateComment.count())
                .from(qDebateComment)
                .where(qDebateComment.debatePost.debateArticleNo.eq(debateArticleNo))
                .fetchOne();

        return PageResponseDTO.<DebateCommentDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .total(Integer.parseInt(count.toString()))
                .build();
    }


    @Override
    public boolean updateCommentLike(Long debateCommentNo, String memberId) {

        DebateComment debateComment = debateCommentRepository.findById(debateCommentNo).orElseThrow();

        Member member = Member.builder()
                .memberId(memberId)
                .build();

        for(int i = 0; i < debateComment.getDebateCommentLikeList().size(); i++){

            if(debateComment.getDebateCommentLikeList().get(i).getMember().getMemberId().equals(memberId)){
                log.info("중복 공감 감지");
                return false;
            }

        }

        DebateCommentLike debateCommentLike = DebateCommentLike.builder()
                .debateComment(debateComment)
                .member(member)
                .build();


        debateCommentLikeRepository.save(debateCommentLike);


        debateComment.getDebateCommentLikeList().add(debateCommentLike);


        debateCommentRepository.save(debateComment);



        return true;
    }

    @Override
    public void deleteComment(Long debateCommentNo) {

        debateCommentRepository.deleteById(debateCommentNo);
    }
}
