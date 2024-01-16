package com.project.Debate.service;

import com.project.Debate.dto.*;
import com.project.Debate.entity.*;
import com.project.Debate.repository.DebateBoardRepository;
import com.project.Debate.repository.DebatePostImageRepository;
import com.project.Debate.repository.DebateProsAndConsRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class DebateBoardServiceImpl implements DebateBoardService{

    @PersistenceContext
    EntityManager em;

    @Value("${itemImgLocation}")  //application-properties속성의 변수 값 가져오기
    private String itemImgLocation;  //blogNo 넘겨서 폴더만들기 후 경로 지정

    @Value("${uploadPath}")  //application-properties속성의 변수 값 가져오기
    private String uploadPath;  //blogNo 넘겨서 폴더만들기 후 경로 지정

    private final DebateBoardRepository debateBoardRepository;
    private final DebatePostImageRepository debatePostImageRepository;

    private final DebateProsAndConsRepository debateProsAndConsRepository;

    private final FileService fileService;


    private final MemberService memberService;

    public class MyRunnable implements Runnable {
        private final Long debateArticleNo;
        private boolean threadChk;


        public MyRunnable(Long debateArticleNo) {
            this.debateArticleNo = debateArticleNo;
            this.threadChk = true;
        }

        @Override
        public void run() {
            while(threadChk) {

                try {
                    Thread.sleep(60000);

                    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
                    QDebatePost qDebatePost = QDebatePost.debatePost;

                    DebatePost debatePost = queryFactory.selectFrom(qDebatePost)
                            .leftJoin(qDebatePost.postProsAndCons).fetchJoin()
                            .where(qDebatePost.debateArticleNo.eq(debateArticleNo))
                            .fetchOne();

                    if(debatePost == null){
                        log.info("삭제된 글");
                        threadChk = false;
                        return;
                    }

                    LocalTime time = LocalTime.parse(debatePost.getDebateRunningTime());

                    if(time.toString().equals("00:00")){

                        threadChk = false;
                        log.info("스레드 종료");

                        int a = 0;
                        int b = 0;

                        Map<String,String> memberNameMap = new HashMap<>();


                        for(int i = 0; i < debatePost.getPostProsAndCons().size(); i++ ){
                            if(debatePost.getPostProsAndCons().get(i).getDebateVoteState().toString().equals("A")){
                                a += 1;
                                memberNameMap.put(debatePost.getPostProsAndCons().get(i).getMember().getMemberId(),debatePost.getPostProsAndCons().get(i).getDebateVoteState().toString());
                            }else{
                                b += 1;
                                memberNameMap.put(debatePost.getPostProsAndCons().get(i).getMember().getMemberId(),debatePost.getPostProsAndCons().get(i).getDebateVoteState().toString());
                            }
                        }

                        //투표한 유저들의 승률 작업

                        String result =  a > b ? "A" : a < b ? "B" : "D";
                        memberService.updateMemberDebateWinningPercent(memberNameMap,result);



                        //투표 결과 저장
                        debatePost.setAVotes(a);
                        debatePost.setBVotes(b);
                        debatePost.setDebateWinner(a > b ? "A의 승리 입니다." : a < b ? "B의 승리 입니다." : "무승부 입니다.");
                        debatePost.setDebateResult("종료");
                        debateBoardRepository.save(debatePost);





                    }else {
                        time = time.minusMinutes(1);
                        debatePost.setDebateRunningTime(String.valueOf(time));
                        debateBoardRepository.save(debatePost);
                    }



                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Long GetDebateBoardArticleNo() {

        return debateBoardRepository.GetDebatePostCount();
    }

    @Override
    public void CreateDebatePost(DebatePostDTO debatePostDTO) {

        Member member = Member.builder()
                .memberId(debatePostDTO.getMemberId())
                .build();


        //포스트 저장
        DebatePost debatePost = DebatePost.builder()
                .debateTitle(debatePostDTO.getDebateTitle())
                .debateSituation(debatePostDTO.getDebateSituation())
                .debateCategory(debatePostDTO.getDebateCategory())
                .debateContentTextA(debatePostDTO.getDebateContentTextA())
                .debateContentTextB(debatePostDTO.getDebateContentTextB())
                .debateStartDate(debatePostDTO.getDebateStartDate())
                .debateSelectTime(debatePostDTO.getDebateSelectTime())
                .debateRunningTime(debatePostDTO.getDebateSelectTime())
                .debatePostView(debatePostDTO.getDebatePostView())
                .debatePostDate(debatePostDTO.getDebatePostDate())
                .debateResult(debatePostDTO.getDebateResult())
                .debateState(debatePostDTO.getDebateState())
                .aVotes(0)
                .bVotes(0)
                .debateWinner("진행중")
                .member(member)
                .build();

        debateBoardRepository.save(debatePost);


        //이미지 저장
        DebatePostImage debatePostImage = DebatePostImage.builder()
                .debateContentVideoPath(debatePostDTO.getDebateContentVideoPath())
                .debateContentImagePath(debatePostDTO.getDebateContentImagePath())
                .debateContentImagePath2(debatePostDTO.getDebateContentImagePath2())
                .debateContentImagePath3(debatePostDTO.getDebateContentImagePath3())
                .debatePost(debatePost)
                .build();

        debatePostImageRepository.save(debatePostImage);




        Long message = debatePost.getDebateArticleNo();
        Thread thread = new Thread(new MyRunnable(message));
        thread.start(); // 스레드를 실행합니다.


    }

    @Override
    @Transactional
    public void UpdateDebatePostViewCount(Long DebateArticleNo) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost = QDebatePost.debatePost;

        queryFactory.update(qDebatePost)
                .set(qDebatePost.debatePostView,
                        queryFactory.select(qDebatePost.debatePostView).from(qDebatePost)
                                .where(qDebatePost.debateArticleNo.eq(DebateArticleNo))
                                .fetchOne().intValue() + 1)
                .where(qDebatePost.debateArticleNo.eq(DebateArticleNo)).execute();

    }

    @Override
    public List<DebatePostDTO> SelectSearchDebatePost(String debateSearchOpt,String debateSearchData,String debateCategoryName, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable();
        BooleanBuilder builder = new BooleanBuilder();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost = QDebatePost.debatePost;

        if(debateSearchOpt.equals("전체 검색")){
            builder.and(qDebatePost.debateTitle.contains(debateSearchData)
                    .or(qDebatePost.debateSituation.contains(debateSearchData)));
        }else if(debateSearchOpt.equals("제목")) {
            builder.and(qDebatePost.debateTitle.contains(debateSearchData));
        }else{
            builder.and(qDebatePost.debateSituation.contains(debateSearchData));
        }


        List<DebatePost> query = queryFactory.selectFrom(qDebatePost)
                .where(builder
                        .and(qDebatePost.debateCategory.eq(debateCategoryName)))
                .orderBy(qDebatePost.debatePostDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();

        List<DebatePostDTO> debatePostDTOList = new ArrayList<>();



        for(int i = 0; i < query.size(); i++){

            DebatePostDTO debateCommentDTO = DebatePostDTO.builder()
                    .memberId(query.get(i).getMember().getMemberId())
                    .debateArticleNo(query.get(i).getDebateArticleNo())
                    .debateTitle(query.get(i).getDebateTitle())
                    .debateCategory(query.get(i).getDebateCategory())
                    .debateRunningTime(query.get(i).getDebateRunningTime())
                    .debateSituation(query.get(i).getDebateSituation().length() < 100
                            ? query.get(i).getDebateSituation() : query.get(i).getDebateSituation().substring(0,100) + "...")
                    .debatePostView(query.get(i).getDebatePostView())
                    .debatePostDate(query.get(i).getDebatePostDate())
                    .debateResult(query.get(i).getDebateResult())
                    .debateState(query.get(i).getDebateState())
                    .build();

            debatePostDTOList.add(debateCommentDTO);
        }


        return debatePostDTOList;



    }
    @Override
    public PageResponseDTO<DebatePostDTO> SelectSearchDebatePostPaging(String debateSearchOpt,String debateSearchData,String debateCategoryName, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable();
        BooleanBuilder builder = new BooleanBuilder();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost = QDebatePost.debatePost;

        if(debateSearchOpt.equals("전체 검색")){
            builder.and(qDebatePost.debateTitle.contains(debateSearchData)
                    .or(qDebatePost.debateSituation.contains(debateSearchData)));
        }else if(debateSearchOpt.equals("제목")) {
            builder.and(qDebatePost.debateTitle.contains(debateSearchData));
        }else{
            builder.and(qDebatePost.debateSituation.contains(debateSearchData));
        }


        List<DebatePost> query = queryFactory.selectFrom(qDebatePost)
                .where(builder
                        .and(qDebatePost.debateCategory.eq(debateCategoryName)))
                .orderBy(qDebatePost.debatePostDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();



        Long count = queryFactory
                .select(qDebatePost.count())
                .from(qDebatePost)
                .where(builder)
                .fetchOne();



        return PageResponseDTO.<DebatePostDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .total(Integer.parseInt(count.toString()))
                .build();





    }
    @Override
    public List<DebatePostPreviewDTO> SelectDebatePostPreview() {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost = QDebatePost.debatePost;

       List<DebatePost> query = queryFactory.selectFrom(qDebatePost)
                .orderBy(qDebatePost.debatePostDate.desc())
                .limit(20)
                .fetch();


       List<DebatePostPreviewDTO> debatePostPreviewDTOS = new ArrayList<>();


       for(int i = 0; i< query.size(); i++){

           DebatePostPreviewDTO debatePostPreviewDTO = DebatePostPreviewDTO.builder()
                   .debateArticleNo(query.get(i).getDebateArticleNo())
                   .debateTitle(query.get(i).getDebateTitle())
                   .debateSituation(query.get(i).getDebateSituation().length() < 60
                           ? query.get(i).getDebateSituation() : query.get(i).getDebateSituation().substring(0,60) + "...")
                   .debatePostView(query.get(i).getDebatePostView())
                   .debateCategory(query.get(i).getDebateCategory())
                   .build();

           debatePostPreviewDTOS.add(debatePostPreviewDTO);
       }


        return debatePostPreviewDTOS;
    }


    @Override
    public DebatePostDTO GetSelectDebatePost(Long DebateArticleNo) {


       DebatePost debatePost = debateBoardRepository.findById(DebateArticleNo).orElseThrow();



        DebatePostDTO debatePostDTO = DebatePostDTO.builder()
                .memberId(debatePost.getMember().getMemberId())
                .debateArticleNo(debatePost.getDebateArticleNo())
                .debateTitle(debatePost.getDebateTitle())
                .debateSituation(debatePost.getDebateSituation())
                .debateCategory(debatePost.getDebateCategory())
                .debateContentTextA(debatePost.getDebateContentTextA())
                .debateContentTextB(debatePost.getDebateContentTextB())
                .debateStartDate(debatePost.getDebateStartDate())
                .debateSelectTime(debatePost.getDebateSelectTime())
                .debateRunningTime(debatePost.getDebateRunningTime())
                .debatePostView(debatePost.getDebatePostView())
                .debatePostDate(debatePost.getDebatePostDate())
                .debateResult(debatePost.getDebateResult())
                .debateState(debatePost.getDebateState())
                .aVotes(debatePost.getAVotes())
                .bVotes(debatePost.getBVotes())
                .debateWinner(debatePost.getDebateWinner())
                .debateContentVideoPath(debatePost.getDebatePostImage().getDebateContentVideoPath())
                .debateContentImagePath(debatePost.getDebatePostImage().getDebateContentImagePath())
                .debateContentImagePath2(debatePost.getDebatePostImage().getDebateContentImagePath2())
                .debateContentImagePath3(debatePost.getDebatePostImage().getDebateContentImagePath3())
                .build();




        return debatePostDTO;
    }

    @Override
    public List<MyPageDebateListDTO> SelectMyPageDebatePost(String memberId,  PageRequestDTO pageRequestDTO) {



        Pageable pageable = pageRequestDTO.getPageable();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost = QDebatePost.debatePost;

        List<DebatePost> query = queryFactory.selectFrom(qDebatePost)
                .where(qDebatePost.member.memberId.eq(memberId))
                .orderBy(qDebatePost.debatePostDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();



        List<MyPageDebateListDTO> myPageDebateListDTOS = new ArrayList<>();


        for(int i = 0; i< query.size(); i++){

            MyPageDebateListDTO myPageDebateListDTO = MyPageDebateListDTO.builder()
                    .memberId(query.get(i).getMember().getMemberId())
                    .debateArticleNo(query.get(i).getDebateArticleNo())
                    .debateTitle(query.get(i).getDebateTitle())
                    .debatePostView(query.get(i).getDebatePostView())
                    .debateCategory(query.get(i).getDebateCategory())
                    .debatePostDate(query.get(i).getDebatePostDate())
                    .debateResult(query.get(i).getDebateResult())
                    .debateRunningTime(query.get(i).getDebateRunningTime())
                    .build();

            myPageDebateListDTOS.add(myPageDebateListDTO);
        }



        return myPageDebateListDTOS;
    }

    @Override
    public PageResponseDTO<MyPageDebateListDTO> SelectMyPageDebatePostPaging(String memberId,  PageRequestDTO pageRequestDTO) {



        Pageable pageable = pageRequestDTO.getPageable();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost = QDebatePost.debatePost;

        List<DebatePost> query = queryFactory.selectFrom(qDebatePost)
                .where(qDebatePost.member.memberId.eq(memberId))
                .orderBy(qDebatePost.debatePostDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();



        List<MyPageDebateListDTO> myPageDebateListDTOS = new ArrayList<>();


        for(int i = 0; i< query.size(); i++){

            MyPageDebateListDTO myPageDebateListDTO = MyPageDebateListDTO.builder()
                    .memberId(query.get(i).getMember().getMemberId())
                    .debateArticleNo(query.get(i).getDebateArticleNo())
                    .debateTitle(query.get(i).getDebateTitle())
                    .debatePostView(query.get(i).getDebatePostView())
                    .debateCategory(query.get(i).getDebateCategory())
                    .debatePostDate(query.get(i).getDebatePostDate())
                    .debateResult(query.get(i).getDebateResult())
                    .debateRunningTime(query.get(i).getDebateRunningTime())
                    .build();

            myPageDebateListDTOS.add(myPageDebateListDTO);
        }

        Long count = queryFactory
                .select(qDebatePost.count())
                .from(qDebatePost)
                .where(qDebatePost.member.memberId.eq(memberId))
                .fetchOne();



        return PageResponseDTO.<MyPageDebateListDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .total(Integer.parseInt(count.toString()))
                .build();

    }


    @Override
    public List<DebatePostDTO> SelectDebatePost(String DebateCategoryName,String debateResult, PageRequestDTO pageRequestDTO) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost= QDebatePost.debatePost;

        Pageable pageable = pageRequestDTO.getPageable();

        List<DebatePost> query = queryFactory.selectFrom(qDebatePost)
                .where(qDebatePost.debatePost.debateCategory.eq(DebateCategoryName).and(qDebatePost.debateResult.eq(debateResult)))
                .orderBy(qDebatePost.debateArticleNo.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();

        List<DebatePostDTO> debatePostDTOList = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){

            DebatePostDTO debateCommentDTO = DebatePostDTO.builder()
                    .memberId(query.get(i).getMember().getMemberId())
                    .debateArticleNo(query.get(i).getDebateArticleNo())
                    .debateTitle(query.get(i).getDebateTitle())
                    .debateCategory(query.get(i).getDebateCategory())
                    .debateSelectTime(query.get(i).getDebateSelectTime())
                    .debateRunningTime(query.get(i).getDebateRunningTime())
                    .debateSituation(query.get(i).getDebateSituation().length() < 100
                            ? query.get(i).getDebateSituation() : query.get(i).getDebateSituation().substring(0,100) + "...")
                    .debatePostView(query.get(i).getDebatePostView())
                    .debatePostDate(query.get(i).getDebatePostDate())
                    .debateResult(query.get(i).getDebateResult())
                    .debateState(query.get(i).getDebateState())
                    .build();

            debatePostDTOList.add(debateCommentDTO);
        }

        return debatePostDTOList;

    }


    @Override
    public PageResponseDTO<DebatePostDTO> SelectDebatePostPaging(String DebateCategoryName,String debateResult, PageRequestDTO pageRequestDTO) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost= QDebatePost.debatePost;

        Pageable pageable = pageRequestDTO.getPageable();

        List<DebatePost> query = queryFactory.selectFrom(qDebatePost)
                .where(qDebatePost.debatePost.debateCategory.eq(DebateCategoryName).and(qDebatePost.debateResult.eq(debateResult)))
                .orderBy(qDebatePost.debateArticleNo.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();



        Long count = queryFactory
                .select(qDebatePost.count())
                .from(qDebatePost)
                .where(qDebatePost.debateCategory.eq(DebateCategoryName).and(qDebatePost.debateResult.eq(debateResult)))
                .fetchOne();



        return PageResponseDTO.<DebatePostDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .total(Integer.parseInt(count.toString()))
                .build();

    }

    @Override
    public List<LatestDebateDTO> SelectLatestDebate(String memberId) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePostProsAndCons qDebatePostProsAndCons= QDebatePostProsAndCons.debatePostProsAndCons;


        List<DebatePostProsAndCons> query = queryFactory.selectFrom(qDebatePostProsAndCons)
                .where(qDebatePostProsAndCons.member.memberId.eq(memberId))
                .orderBy(qDebatePostProsAndCons.debateVoteDate.desc())
                .limit(5) //조회 갯수
                .fetch();

        List<LatestDebateDTO> latestDebateDTOS = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){


            LatestDebateDTO latestDebateDTO = LatestDebateDTO.builder()
                    .debateArticleNo(query.get(i).getDebatePost().getDebateArticleNo())
                    .debateCategory(query.get(i).getDebatePost().getDebateCategory())
                    .debateTitle(query.get(i).getDebatePost().getDebateTitle())
                    .build();

            latestDebateDTOS.add(latestDebateDTO);
        }




        return latestDebateDTOS;
    }


    @Override
    public List<MyPageDebateListDTO> SelectMyPageLatestDebate(String memberId,  PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePostProsAndCons qDebatePostProsAndCons= QDebatePostProsAndCons.debatePostProsAndCons;

        List<DebatePostProsAndCons> query = queryFactory.selectFrom(qDebatePostProsAndCons)
                .where(qDebatePostProsAndCons.member.memberId.eq(memberId))
                .orderBy(qDebatePostProsAndCons.debateVoteDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();


        List<MyPageDebateListDTO> latestDebateDTOS = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){


            MyPageDebateListDTO latestDebateDTO = MyPageDebateListDTO.builder()
                    .debateArticleNo(query.get(i).getDebatePost().getDebateArticleNo())
                    .debateCategory(query.get(i).getDebatePost().getDebateCategory())
                    .debateTitle(query.get(i).getDebatePost().getDebateTitle())
                    .debateResult(query.get(i).getDebatePost().getDebateResult())
                    .debateRunningTime(query.get(i).getDebatePost().getDebateRunningTime())
                    .debateVoteDate(query.get(i).getDebateVoteDate())
                    .debatePostView(query.get(i).getDebatePost().getDebatePostView())
                    .build();

            latestDebateDTOS.add(latestDebateDTO);
        }




        return latestDebateDTOS;
    }


    @Override
    public PageResponseDTO<MyPageDebateListDTO> SelectMyPageLatestDebatePaging(String memberId,  PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePostProsAndCons qDebatePostProsAndCons= QDebatePostProsAndCons.debatePostProsAndCons;

        List<DebatePostProsAndCons> query = queryFactory.selectFrom(qDebatePostProsAndCons)
                .where(qDebatePostProsAndCons.member.memberId.eq(memberId))
                .orderBy(qDebatePostProsAndCons.debateVoteDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();


        List<MyPageDebateListDTO> latestDebateDTOS = new ArrayList<>();

        for(int i = 0; i < query.size(); i++){


            MyPageDebateListDTO latestDebateDTO = MyPageDebateListDTO.builder()
                    .debateArticleNo(query.get(i).getDebatePost().getDebateArticleNo())
                    .debateCategory(query.get(i).getDebatePost().getDebateCategory())
                    .debateTitle(query.get(i).getDebatePost().getDebateTitle())
                    .debateResult(query.get(i).getDebatePost().getDebateResult())
                    .debateRunningTime(query.get(i).getDebatePost().getDebateRunningTime())
                    .debatePostDate(query.get(i).getDebatePost().getDebatePostDate())
                    .debatePostView(query.get(i).getDebatePost().getDebatePostView())
                    .build();

            latestDebateDTOS.add(latestDebateDTO);
        }


        Long count = queryFactory
                .select(qDebatePostProsAndCons.count())
                .from(qDebatePostProsAndCons)
                .where(qDebatePostProsAndCons.member.memberId.eq(memberId))
                .fetchOne();


        return PageResponseDTO.<MyPageDebateListDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .total(Integer.parseInt(count.toString()))
                .build();




    }


    @Override
    public List<PopularDebateDTO> SelectPopularDebate() {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost= QDebatePost.debatePost;


        List<Tuple> query = queryFactory.select(qDebatePost.debateArticleNo,
                        qDebatePost.debateCategory,
                        qDebatePost.debateTitle,
                        qDebatePost.debatePostImage.debateContentImagePath)
                .from(qDebatePost)
                .orderBy(qDebatePost.debatePostView.desc())
                .limit(10) //조회 갯수
                .fetch();


        List<PopularDebateDTO> popularDebateDTOS = new ArrayList<>();

        for (Tuple row : query) {
            PopularDebateDTO dto = new PopularDebateDTO();
            dto.setDebateArticleNo(row.get(qDebatePost.debateArticleNo));
            dto.setDebateCategory(row.get(qDebatePost.debateCategory));
            dto.setDebateTitle(row.get(qDebatePost.debateTitle));
            dto.setDebateContentImagePath(row.get(qDebatePost.debatePostImage.debateContentImagePath));
            popularDebateDTOS.add(dto);

        }



        return popularDebateDTOS;


    }

    @Override
    public List<PopularCategoryDebateDTO> SelectPopularCategoryDebate(String debateCategoryName) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost= QDebatePost.debatePost;

        List<Tuple> query = queryFactory.select(qDebatePost.debateArticleNo,
                        qDebatePost.debateTitle,
                        qDebatePost.debatePostView,
                        qDebatePost.debateRunningTime,
                        qDebatePost.debateResult,
                        qDebatePost.member.memberId)
                .from(qDebatePost)
                .where(qDebatePost.debateCategory.eq(debateCategoryName))
                .orderBy(qDebatePost.debatePostView.desc())
                .limit(5) //조회 갯수
                .fetch();


        List<PopularCategoryDebateDTO> popularCategoryDebateDTOS = new ArrayList<>();

        for (Tuple row : query) {
            PopularCategoryDebateDTO dto = new PopularCategoryDebateDTO();
            dto.setDebateArticleNo(row.get(qDebatePost.debateArticleNo));
            dto.setDebateTitle(row.get(qDebatePost.debateTitle));
            dto.setDebatePostView(row.get(qDebatePost.debatePostView));
            dto.setDebateRunningTime(row.get(qDebatePost.debateRunningTime));
            dto.setDebateResult(row.get(qDebatePost.debateResult));
            dto.setMemberId(row.get(qDebatePost.member.memberId));
            popularCategoryDebateDTOS.add(dto);

            log.info(dto.getDebateTitle());

        }

        return popularCategoryDebateDTOS;
    }

    @Override
    public void deleteDebatePost(Long debateArticleNo) {

        debateBoardRepository.deleteById(debateArticleNo);

    }

    @Override
    public void updateDebatePost(DebatePostDTO debatePostDTO) {


        DebatePost debatePost = debateBoardRepository.findById(debatePostDTO.getDebateArticleNo())
                .orElseThrow(() -> new EntityNotFoundException());

        // 변경된 데이터만 저장
        if (!debatePost.getDebateTitle().equals(debatePostDTO.getDebateTitle())) {
            debatePost.setDebateTitle(debatePostDTO.getDebateTitle());
        }

        if (!debatePost.getDebateSituation().equals(debatePostDTO.getDebateSituation())) {
            debatePost.setDebateSituation(debatePostDTO.getDebateSituation());
        }

        if (!debatePost.getDebateContentTextA().equals(debatePostDTO.getDebateContentTextA())) {
            debatePost.setDebateContentTextA(debatePostDTO.getDebateContentTextA());
        }

        if (!debatePost.getDebateContentTextB().equals(debatePostDTO.getDebateContentTextB())) {
            debatePost.setDebateContentTextB(debatePostDTO.getDebateContentTextB());
        }

        if (!debatePost.getDebateStartDate().equals(debatePostDTO.getDebateStartDate())) {
            debatePost.setDebateStartDate(debatePostDTO.getDebateStartDate());
        }

        if (!debatePost.getDebateCategory().equals(debatePostDTO.getDebateCategory())) {
            debatePost.setDebateCategory(debatePostDTO.getDebateCategory());
        }

        if (!debatePost.getDebateSelectTime().equals(debatePostDTO.getDebateSelectTime())) {
            debatePost.setDebateSelectTime(debatePostDTO.getDebateSelectTime());
        }


        debateBoardRepository.save(debatePost);

    }

    @Override
    public void updateDebatePostImage(DebatePostDTO debatePostDTO) throws IOException {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePostImage qDebatePostImage= QDebatePostImage.debatePostImage;

        DebatePostImage debatePostImage = queryFactory.selectFrom(qDebatePostImage)
                .where(qDebatePostImage.debatePost.debateArticleNo.eq(debatePostDTO.getDebateArticleNo()))
                .fetchOne();

        String path = itemImgLocation + "/" + debatePostDTO.getDebateArticleNo();


        if (!debatePostDTO.getDebateContentVideoData().isEmpty()) { // 데이터가 변경되었다

            if(debatePostImage.getDebateContentVideoPath() != null) { //기존의 이미지가 있으면 삭제
                // c경로의 이미지 파일 삭제
                fileService.deleteFile(path
                        + "/" + debatePostImage.getDebateContentVideoPath().replace("/images/"
                        + debatePostDTO.getDebateArticleNo() + "/",""));

            }

            //c경로의 이미지 파일 생성
            String fileUrl =  fileService.uploadFile(path,debatePostDTO.getDebateContentVideoData().getOriginalFilename(),debatePostDTO.getDebateContentVideoData().getBytes());


            //반환 받은 경로를 데이터베이스에 수정 저장
            debatePostImage.setDebateContentVideoPath(fileUrl);

        }


        if (!debatePostDTO.getDebateContentImageData().isEmpty()) { // 데이터가 변경되었다

            if(debatePostImage.getDebateContentImagePath() != null) { //기존의 이미지가 있으면 삭제
                // c경로의 이미지 파일 삭제
                fileService.deleteFile(path
                        + "/" + debatePostImage.getDebateContentImagePath().replace("/images/"
                        + debatePostDTO.getDebateArticleNo() + "/",""));

            }

            //c경로의 이미지 파일 생성
            String fileUrl =  fileService.uploadFile(path,debatePostDTO.getDebateContentImageData().getOriginalFilename(),debatePostDTO.getDebateContentImageData().getBytes());


            //반환 받은 경로를 데이터베이스에 수정 저장
            debatePostImage.setDebateContentImagePath(fileUrl);

        }

        if (!debatePostDTO.getDebateContentImageData2().isEmpty()) { // 데이터가 변경되었다

            if(debatePostImage.getDebateContentImagePath2() != null) { //기존의 이미지가 있으면 삭제
                // c경로의 이미지 파일 삭제
                fileService.deleteFile(path
                        + "/" + debatePostImage.getDebateContentImagePath2().replace("/images/"
                        + debatePostDTO.getDebateArticleNo() + "/",""));

            }

            //c경로의 이미지 파일 생성
            String fileUrl =  fileService.uploadFile(path,debatePostDTO.getDebateContentImageData2().getOriginalFilename(),debatePostDTO.getDebateContentImageData2().getBytes());


            //반환 받은 경로를 데이터베이스에 수정 저장
            debatePostImage.setDebateContentImagePath2(fileUrl);

        }

        if (!debatePostDTO.getDebateContentImageData3().isEmpty()) { // 데이터가 변경되었다

            if(debatePostImage.getDebateContentImagePath3() != null) { //기존의 이미지가 있으면 삭제
                // c경로의 이미지 파일 삭제
                fileService.deleteFile(path
                        + "/" + debatePostImage.getDebateContentImagePath3().replace("/images/"
                        + debatePostDTO.getDebateArticleNo() + "/",""));

            }

            //c경로의 이미지 파일 생성
            String fileUrl =  fileService.uploadFile(path,debatePostDTO.getDebateContentImageData3().getOriginalFilename(),debatePostDTO.getDebateContentImageData3().getBytes());


            //반환 받은 경로를 데이터베이스에 수정 저장
            debatePostImage.setDebateContentImagePath3(fileUrl);

        }


        debatePostImageRepository.save(debatePostImage);

    }

    @Override
    public boolean ProsAndConsCheck(Long debateArticleNo, String memberId) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePostProsAndCons qDebatePostProsAndCons= QDebatePostProsAndCons.debatePostProsAndCons;

        boolean chk = queryFactory.selectFrom(qDebatePostProsAndCons)
                .where(qDebatePostProsAndCons.debatePost.debateArticleNo.eq(debateArticleNo)
                        .and(qDebatePostProsAndCons.member.memberId.eq(memberId)))
                .fetchOne() != null;


        return chk;
    }



    @Override
    public void ProsAndCons(Long debateArticleNo,String DebateVoteState, String memberId) {

        Member member = Member.builder()
                .memberId(memberId)
                .build();

        DebatePost debatePost = DebatePost.builder()
                .debateArticleNo(debateArticleNo)
                .build();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String ProsAndConsTime = now.format(formatter);


        DebatePostProsAndCons debatePostProsAndCons = DebatePostProsAndCons.builder()
                .debateVoteState(DebateVoteState)
                .debateVoteDate(ProsAndConsTime)
                .member(member)
                .debatePost(debatePost)
                .build();

        debateProsAndConsRepository.save(debatePostProsAndCons);


    }
}
