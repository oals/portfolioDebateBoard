package com.project.Debate.service;

import com.project.Debate.dto.DebateBookMarkDTO;
import com.project.Debate.dto.MyPageDebateListDTO;
import com.project.Debate.dto.PageRequestDTO;
import com.project.Debate.dto.PageResponseDTO;
import com.project.Debate.entity.*;
import com.project.Debate.repository.DebateBoardRepository;
import com.project.Debate.repository.DebateBookMarkRepository;
import com.project.Debate.repository.MemberRepository;
import com.querydsl.core.BooleanBuilder;
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
public class DebateBookMarkServiceImpl implements DebateBookMarkService{


    @PersistenceContext
    EntityManager em;

    private final DebateBookMarkRepository bookMarkRepository;

    @Override
    public void debatePostBookMark(String memberId, Long debateArticleNo) {

        Member member = Member.builder()
                .memberId(memberId)
                .build();

        DebatePost debatePost = DebatePost.builder()
                .debateArticleNo(debateArticleNo)
                .build();

        DebateBookMark debateBookMark = DebateBookMark.builder()
                .member(member)
                .debatePost(debatePost)
                .build();


        bookMarkRepository.save(debateBookMark);



    }

    @Override
    public boolean debatePostBookMarkChk(String memberId, Long debateArticleNo) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebateBookMark qDebateBookMark = QDebateBookMark.debateBookMark;


        boolean chk = queryFactory.selectFrom(qDebateBookMark)
                .where(qDebateBookMark.member.memberId.eq(memberId)
                        .and(qDebateBookMark.debatePost.debateArticleNo.eq(debateArticleNo)))
                .stream().count() == 0;



        return chk;
    }

    @Override
    public List<DebateBookMarkDTO> SelectDebateBookMArk(String memberId, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebateBookMark qDebateBookMark = QDebateBookMark.debateBookMark;

        List<DebateBookMark> query = queryFactory.selectFrom(qDebateBookMark)
                .where(qDebateBookMark.member.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();



        List<DebateBookMarkDTO> debateBookMarkDTOList = new ArrayList<>();


        for(int i = 0; i < query.size(); i++){

            DebateBookMarkDTO debateBookMarkDTO = DebateBookMarkDTO.builder()
                    .debateArticleNo(query.get(i).getDebatePost().getDebateArticleNo())
                    .debateTitle(query.get(i).getDebatePost().getDebateTitle())
                    .debateCategory(query.get(i).getDebatePost().getDebateCategory())
                    .build();

            debateBookMarkDTOList.add(debateBookMarkDTO);

        }


        return debateBookMarkDTOList;
    }
    @Override
    public PageResponseDTO<DebateBookMarkDTO> SelectDebateBookMarkPaging(String memberId, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebateBookMark qDebateBookMark = QDebateBookMark.debateBookMark;


        List<DebateBookMark> query = queryFactory.selectFrom(qDebateBookMark)
                .where(qDebateBookMark.member.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        List<DebateBookMarkDTO> debateBookMarkDTOList = new ArrayList<>();


        for(int i = 0; i < query.size(); i++){

            DebateBookMarkDTO debateBookMarkDTO = DebateBookMarkDTO.builder()
                    .debateArticleNo(query.get(i).getDebatePost().getDebateArticleNo())
                    .debateTitle(query.get(i).getDebatePost().getDebateTitle())
                    .debateCategory(query.get(i).getDebatePost().getDebateCategory())
                    .build();

            debateBookMarkDTOList.add(debateBookMarkDTO);

        }
        Long count = queryFactory
                .select(qDebateBookMark.count())
                .from(qDebateBookMark)
                .where(qDebateBookMark.member.memberId.eq(memberId))
                .fetchOne();



        return PageResponseDTO.<DebateBookMarkDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .total(Integer.parseInt(count.toString()))
                .build();


    }

    @Override
    public void AllDeleteBookMark(String memberId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebateBookMark qDebateBookMark = QDebateBookMark.debateBookMark;


         queryFactory.delete(qDebateBookMark)
                .where(qDebateBookMark.member.memberId.eq(memberId))
                .execute();

    }
    public void DeleteBookMark(Long ArticleNo) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebateBookMark qDebateBookMark = QDebateBookMark.debateBookMark;


        queryFactory.delete(qDebateBookMark)
                .where(qDebateBookMark.debatePost.debateArticleNo.eq(ArticleNo))
                .execute();

    }

}
